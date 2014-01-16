package com.agungpratama.ziputil;

import android.util.Log;

import com.agungpratama.zip.lib.UnzipHelper;
import com.agungpratama.zip.lib.UnzipProgressListener;
import com.agungpratama.zip.lib.UnzipHelper.UnzipHelperArgs;
import com.unity3d.player.UnityPlayer;

public class UnzipUnityInterface {
	public static int startUnzip(String zipFile, String tempPath){
		final String zipPath = zipFile;
		final String destinationPath = tempPath;
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				final UnzipHelperArgs args = new UnzipHelperArgs(4096,
						new UnzipProgressListener() {
							public void unzipProgressCallback(final float percentage) {
								UnzipUnityInterface.unzipProgressCallback(percentage);
							}

							public void unzipFinishedCallback(final int status) {
								UnzipUnityInterface.unzipFinishedCallback(status);
							}

							public void unzipFileDecompressedCallback(
									final String name, final int status) {
								UnzipUnityInterface.unzipFileDecompressedCallback(name, status);
							}
				});
				UnzipHelper.unzip(zipPath, destinationPath, args);
			}
		});
		thread.start();
		return 1;
	}
	
	public static void unzipProgressCallback(final float progress){
		//Log.d("GARITA_AGUNG", "unzipProgressCallback:"+"progress:"+(progress*100));
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				UnityPlayer.UnitySendMessage("UnzipManager", "OnUnzipProgressCallback", progress+"");
			}
		});
		
	}
	
	public static void unzipFinishedCallback(final int status){
		//Log.d("GARITA_AGUNG", "unzipFinishedCallback:"+status);
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				UnityPlayer.UnitySendMessage("UnzipManager", "OnUnzipFinishedCallback", status+"");
			}
		});
	}
	
	public static void unzipFileDecompressedCallback(final String name, final int status){
		//Log.d("GARITA_AGUNG", "unzipFileDecompressedCallback:"+name+" status:"+status);
		UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
		UnityPlayer.UnitySendMessage("UnzipManager", "OnUnzipFileDecompressedCallback", name+";;;"+status);
			}
});
	}
}
