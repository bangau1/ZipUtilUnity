package com.agungpratama.zip.lib;

public interface UnzipProgressListener {
	final int UNZIP_OK = 0;
	final int UNZIP_ERROR_DATA = 1;
	final int UNZIP_UNKNOWN_ERROR = 2;

	void unzipProgressCallback(float percentage);

	void unzipFileDecompressedCallback(String name, int status);

	void unzipFinishedCallback(int status);
}
