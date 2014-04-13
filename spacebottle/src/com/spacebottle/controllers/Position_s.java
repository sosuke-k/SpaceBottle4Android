package com.spacebottle.controllers;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.spacebottle.helper.SBAuthenticateCallback;
import com.spacebottle.models.Devices;
import com.spacebottle.models.Position;
import com.spacebottle.models.Positions;

public class Position_s {
	private static Position mPosition;
	private Devices mDevices;
	private static Positions mPositions;
	private static LocationManager mLocationManager;
	private Criteria mCriteria;
	private SharedPreferences pref;
	private GoogleCloudMessaging gcm;
	private NotificationHub hub;
	private Position_s(){}

	public static Position position(Context context){
		if(mPosition == null){
			try{
				mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

				mPositions = new Positions(Azure.Client(context, new SBAuthenticateCallback(){

					@Override
					public void success() {
						// TODO 自動生成されたメソッド・スタブ

					}

					@Override
					public void error(Exception exception) {
						// TODO 自動生成されたメソッド・スタブ

					}}));
				mPositions.read(new TableQueryCallback<Position>(){

					@Override
					public void onCompleted(List<Position> positions, int count,
							Exception exception, ServiceFilterResponse response) {
						if(positions.isEmpty()){
							mPosition = new Position();
						} else {
							mPosition = positions.get(0);
						}
					}} );
			} catch(Exception e){

			}
		}
		return mPosition;
	}

}
