//package com.agungpratama.ziputil;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.URL;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.widget.TextView;
//
//import com.agungpratama.zip.lib.UnzipHelper;
//import com.agungpratama.zip.lib.UnzipProgressListener;
//import com.agungpratama.zip.lib.UnzipHelper.UnzipHelperArgs;
//import com.agungpratama.ziputil.R;
//
//public class MainActivity extends Activity {
//	private TextView tvStatus;
//	private TextView tvStatusPercentage;
//	private final String url = "http://dtyu6obd3mi6d.cloudfront.net/assets/2.4.0/android/all_data-1389765609.zip";
//	private final String pathToZip = "/mnt/sdcard/Downloads/assetbundle.zip";
//	private final String pathToAssets = "/mnt/sdcard/Downloads/assets/";
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		tvStatus = (TextView)findViewById(R.id.tvStatus);
//		tvStatusPercentage = (TextView)findViewById(R.id.tvStatusPercentage);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	public void onDownloadStartClick(View state) {
//		final UnzipHelperArgs arg = new UnzipHelperArgs(4096,
//				new UnzipProgressListener() {
//
//					public void unzipProgressCallback(final float percentage) {
//						runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() {
//								tvStatusPercentage.setText("Status percentage:"
//										+ (percentage * 100));
//							}
//						});
//					}
//
//					public void unzipFinishedCallback(final int status) {
//						runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() {
//								tvStatus.setText("Status unzip is finished with status:"
//										+ status);
//							}
//						});
//
//					}
//
//					public void unzipFileDecompressedCallback(
//							final String name, final int status) {
//						runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() {
//								tvStatus.setText("Status '" + name
//										+ "'のstatus:" + status);
//							}
//						});
//
//					}
//				});
//
//		final File zipFile = new File(pathToZip);
//		InitPathToAssets();
//		final Runnable downloadRunnable = new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					URL u = new URL(url);
//
//					DataInputStream stream = new DataInputStream(u.openStream());
//					DataOutputStream fos = new DataOutputStream(
//							new FileOutputStream(pathToZip));
//					
//					byte[] buffer = new byte[4096];
//					int read = -1;
//					while((read = stream.read(buffer))!=-1){
//						fos.write(buffer, 0, read);
//						fos.flush();
//					}
//					stream.close();
//					fos.close();
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//			}
//		};
//		
//		final Runnable unzipRunnable = new Runnable() {
//			
//			@Override
//			public void run() {
//				UnzipHelper.unzip(pathToZip, pathToAssets, arg);
//			}
//		};
//
//		Thread newThread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// first downlaod
//				if(!zipFile.exists())
//					downloadRunnable.run();
//				long start = System.currentTimeMillis();
//				unzipRunnable.run();
//				final long total = System.currentTimeMillis() - start;
//				runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						tvStatusPercentage.setText("100% done, with time:"+total);
//					}
//				});
//			}
//		});
//		newThread.start();
//	}
//
//	void InitPathToAssets() {
//		File folderAssets = new File(pathToAssets);
//		if (folderAssets.exists()) {
//			DeleteRecursive(folderAssets);
//		}
//		folderAssets.mkdirs();
//	}
//
//	private static void DeleteRecursive(File dir) {
//		// Log.d("DeleteRecursive", "DELETEPREVIOUS TOP" + dir.getPath());
//		if (dir.isDirectory()) {
//			String[] children = dir.list();
//			for (int i = 0; i < children.length; i++) {
//				File temp = new File(dir, children[i]);
//				DeleteRecursive(temp);
//			}
//
//		}
//
//		if (dir.delete() == false) {
//			Log.d("DeleteRecursive", "DELETE FAIL");
//		}
//	}
//
//}
