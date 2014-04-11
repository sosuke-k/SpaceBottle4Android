package com.example.spacebottle;

import android.content.Context;
import com.microsoft.windowsazure.notifications.NotificationsHandler;

public class MyHandler extends NotificationsHandler {
	@com.google.gson.annotations.SerializedName("handle")
	private static String mHandle;

	public static String getHandle() {
	    return mHandle;
	}

	public static final void setHandle(String handle) {
	    mHandle = handle;
	}
	
	@Override
	public void onRegistered(Context context, String gcmRegistrationId) {
	    super.onRegistered(context, gcmRegistrationId);

	    setHandle(gcmRegistrationId);
	}
}
