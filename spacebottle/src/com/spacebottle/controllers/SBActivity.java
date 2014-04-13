package com.spacebottle.controllers;

import java.net.MalformedURLException;

import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.spacebottle.helper.*;

abstract class SBActivity extends Activity {
	protected MobileServiceClient mClient;
	private ProgressBar mProgressBar;
	protected Context self;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
	}

	protected final void setProgressBar(ProgressBar progressBar){
		mProgressBar = progressBar;
	}

	protected void connect(final SBAuthenticateCallback callback){
		try {
			// Create the Mobile Service Client instance, using the provided
			// Mobile Service URL and key
			mClient = new MobileServiceClient(
					"https://spacebottle.azure-mobile.net/",
					"NIxTTzFiVmtUptmvETPKInerCKgRub76",
					this).withFilter(new ProgressFilter());

			authenticate(callback);

		} catch (MalformedURLException e) {
			//createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
	}

	protected void authenticate(final SBAuthenticateCallback callback) {

	    mClient.login(MobileServiceAuthenticationProvider.Facebook,
	            new UserAuthenticationCallback() {

	                @Override
	                public void onCompleted(MobileServiceUser user,
	                        Exception exception, ServiceFilterResponse response) {

	                    if (exception == null) {
	                        /*createAndShowDialog(String.format(
	                                        "You are now logged in - %1$2s",
	                                        user.getUserId()), "Success");*/
	                        callback.success();
	                    } else {
	                        //createAndShowDialog("You must log in. Login Required", "Error");
	                        callback.error(exception);
	                    }
	                }
	            });
	}

	protected void showProgressBar(){
		if(mProgressBar != null){
			mProgressBar.setVisibility(ProgressBar.VISIBLE);
		}
	}

	protected void hideProgressBar(){
		if(mProgressBar != null){
			mProgressBar.setVisibility(ProgressBar.GONE);
		}
	}

	protected void createAndShowDialog(Exception exception, String title) {
		Throwable ex = exception;
		if(exception.getCause() != null){
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title);
	}

	protected void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}

	private class ProgressFilter implements ServiceFilter {

		@Override
		public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
				final ServiceFilterResponseCallback responseCallback) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					showProgressBar();
				}
			});

			nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {

				@Override
				public void onResponse(ServiceFilterResponse response, Exception exception) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							hideProgressBar();
						}
					});

					if (responseCallback != null)  responseCallback.onResponse(response, exception);
				}
			});
		}
	}
}
