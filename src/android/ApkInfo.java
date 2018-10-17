package com.cloudtopo.apkInfo;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin1 on 2017/8/16.
 */

public class ApkInfo extends CordovaPlugin {
	private Activity activity;
	private CallbackContext callbackContext;
	@Override
	protected void pluginInitialize() {
		activity = this.cordova.getActivity();
		super.pluginInitialize();
	}
	@Override
	public boolean execute(String action, final CordovaArgs args, CallbackContext callbackContext) throws JSONException {

		this.callbackContext = callbackContext;
		if("getInfo".equals(action)){
			getInfo(args.getString(0));
			return true;
		}

		return false;
	}

	private void getInfo(final String absPath){
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				String path = null;
				if(absPath.startsWith("file://")){
					path = absPath.substring(7);
				} else {
					path = absPath;
				}
				PackageManager pm = activity.getPackageManager();
				PackageInfo pkgInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
				if (pkgInfo != null) {
					ApplicationInfo appInfo = pkgInfo.applicationInfo;
					String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("path", absPath);
						jsonObject.put("versionName", pkgInfo.versionName);
						jsonObject.put("appName", appName);
						jsonObject.put("packageName", pkgInfo.packageName);
						jsonObject.put("versionCode", pkgInfo.versionCode);
						callbackContext.success(jsonObject);
					} catch (Exception e){
						callbackContext.error(1);
					}
				} else {
					callbackContext.error(0);
				}
			}
		});
	}
}
