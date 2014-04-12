package com.spacebottle.controllers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.example.spacebottle.R;
import com.microsoft.windowsazure.notifications.NotificationsHandler;

public class PushHandler extends NotificationsHandler {
	private static final int NOTIFICATION_ID = 2014;
	@com.google.gson.annotations.SerializedName("handle")
	private static String mHandle;

	public static final String PREFERENCES_FILE_NAME = "preference";
	private SharedPreferences pref;
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

	@Override
	public void onReceive(Context context, Bundle bundle) {
		super.onReceive(context, bundle);

		pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
		int flag = (int)pref.getInt("background-flag", 0);

		if(flag == 0){
			NotificationManager mNotificationManager;
			mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
			Intent intent = new Intent(context, ReceiveMessageActivity.class);
			intent.putExtra("bundle",bundle);
			PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,
	    		intent,
	    		PendingIntent.FLAG_UPDATE_CURRENT);
			Notification mNotification;
			mNotification = new Notification.BigTextStyle(
	    	    new Notification.Builder(context)
	            .setContentTitle("SpaceBottle")
	            .setSmallIcon(R.drawable.ic_launcher)
	            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
	            .addAction(android.R.drawable.ic_menu_send, "Send message", mPendingIntent)
	    		)
	        .bigText("BigText")
	    //  .setBigContentTitle("BigContentTitle") // Notification.Builder#setContentTitle() ���㏑��
	        .setSummaryText(bundle.getString("message"))
	        .build();

			mNotification.defaults |= Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
			mNotificationManager.notify(NOTIFICATION_ID, mNotification);
		} else {
			Log.d("tet",bundle.getString("message"));
			Intent intent = new Intent(context,ReceiveMessageActivity.class);
			intent.putExtra("bundle",bundle);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
    }
}
