package com.agungpratama.zip.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

import com.agungpratama.common.lib.CountingInputStream;

public class UnzipHelper {

	public static boolean unzip(String path, String destination,
			UnzipHelperArgs arguments) {
		return unzip(new File(path), new File(destination), arguments);
	}

	public static boolean unzip(String path, String destination) {
		return unzip(new File(path), new File(destination));
	}

	public static boolean unzip(File zipFile, File destinationFolder) {
		return unzip(zipFile, destinationFolder, null);
	}

	public static boolean unzip(File zipFile, File destinationFolder,
			UnzipHelperArgs arguments) {
		boolean result = true;
		CountingInputStream is;
		ZipInputStream zis;

		UnzipHelperArgs arg = arguments;
		if (arg == null)
			arg = getDefaultArgs();

		ZipEntry ze;
		long totalFileSize = zipFile.length();
		int globalStatus = UnzipProgressListener.UNZIP_OK;
		try {
			is = new CountingInputStream(new FileInputStream(zipFile));
			zis = new ZipInputStream(is);
			String destinationFolderName = destinationFolder.getAbsolutePath()
					+ "/";

			while ((ze = zis.getNextEntry()) != null) {
				int unzipIndividualStatus = UnzipProgressListener.UNZIP_OK;
				String fileName = ze.getName();
				try {

					if (ze.isDirectory()) {
						new File(destinationFolderName + fileName).mkdirs();
					} else {
						File f = new File(destinationFolderName + fileName);
						File parentF = f.getParentFile();
						if (parentF != null && !parentF.exists())
							parentF.mkdirs();
						FileOutputStream fos = new FileOutputStream(
								destinationFolderName + fileName);
						while ((arg.bytesRead = zis.read(arg.buffer)) != -1) {
							fos.write(arg.buffer, 0, arg.bytesRead);
							if (arg.unzipListener != null)
								arg.unzipListener
										.unzipProgressCallback((0.0f + is
												.getTotalBytesRead())
												/ totalFileSize);
						}
						fos.close();
					}
					zis.closeEntry();
				} catch (IOException exIo) {
					Log.e("GARITA_AGUNG", "error:" + exIo.getMessage());
					exIo.printStackTrace();
					globalStatus = unzipIndividualStatus = UnzipProgressListener.UNZIP_ERROR_DATA;
				} catch (Exception ex) {
					Log.e("GARITA_AGUNG", "error:" + ex.getMessage());
					ex.printStackTrace();
					globalStatus = unzipIndividualStatus = UnzipProgressListener.UNZIP_UNKNOWN_ERROR;
				}
				if (arg.unzipListener != null) {
					arg.unzipListener.unzipProgressCallback((0.0f + is
							.getTotalBytesRead()) / totalFileSize);
					arg.unzipListener.unzipFileDecompressedCallback(fileName,
							unzipIndividualStatus);
				}
			}

			zis.close();

		} catch (IOException e) {
			e.printStackTrace();
			result = false;
			globalStatus = UnzipProgressListener.UNZIP_ERROR_DATA;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			globalStatus = UnzipProgressListener.UNZIP_UNKNOWN_ERROR;
		}

		if (arg.unzipListener != null) {
			arg.unzipListener.unzipProgressCallback(1);
			arg.unzipListener.unzipFinishedCallback(globalStatus);
		}

		return result;
	}

	private static UnzipHelperArgs getDefaultArgs() {
		return new UnzipHelperArgs(4096);
	}

	public static class UnzipHelperArgs {
		private int bufferSize;
		private byte[] buffer;
		private int bytesRead;
		private UnzipProgressListener unzipListener;

		public UnzipHelperArgs(int bufferSize) {
			this.bufferSize = bufferSize;
			this.buffer = new byte[this.bufferSize];
		}

		public UnzipHelperArgs(int bufferSize, UnzipProgressListener listener) {
			this.bufferSize = bufferSize;
			this.buffer = new byte[this.bufferSize];
			this.unzipListener = listener;
		}
	}

}
