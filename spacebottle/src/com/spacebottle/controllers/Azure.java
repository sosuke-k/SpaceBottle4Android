package com.spacebottle.controllers;

import java.net.MalformedURLException;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;
import com.spacebottle.helper.SBAuthenticateCallback;

public class Azure {

	private static MobileServiceClient mClient;

	private Azure(){}
	public static MobileServiceClient Client(Context context, SBAuthenticateCallback callback){
		if(mClient == null){
			try {
				// Create the Mobile Service Client instance, using the provided
				// Mobile Service URL and key
				mClient = new MobileServiceClient(
						"https://spacebottle.azure-mobile.net/",
						"NIxTTzFiVmtUptmvETPKInerCKgRub76",
						context);
				authenticate(callback);
			} catch (MalformedURLException e) {
				Log.d("MBaaS", e.toString());
			}
		}
		return mClient;
	}
	private static void authenticate(final SBAuthenticateCallback callback) {

	    mClient.login(MobileServiceAuthenticationProvider.Facebook,
	            new UserAuthenticationCallback() {

	                @Override
	                public void onCompleted(MobileServiceUser user,
	                        Exception exception, ServiceFilterResponse response) {
	                    if (exception == null) {
	                        callback.success();
	                    } else {
	                        callback.error(exception);
	                    }
	                }
	            });
	}
}
