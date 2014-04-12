package com.spacebottle.controllers;

import java.util.List;

import com.example.spacebottle.MyHandler;
import com.example.spacebottle.R;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.spacebottle.helper.SBAuthenticateCallback;
import com.spacebottle.models.Device;
import com.spacebottle.models.Devices;
import com.spacebottle.models.Position;
import com.spacebottle.models.Positions;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HomeActivity extends SBActivity implements LocationListener  {
	
	private Devices mDevices;
	private Positions mPositions;
	private Position mPosition;
	private LocationManager mLocationManager;
	private Criteria mCriteria;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		self = this;
		setProgressBar((ProgressBar) findViewById(R.id.loadingProgressBar));
		hideProgressBar();
		NotificationsManager.handleNotifications(this, getString(R.string.sender_id), PushHandler.class);
		connect(new SBAuthenticateCallback(){

			@Override
			public void success() {
				hideProgressBar();
				initialize();
			}

			@Override
			public void error(Exception exception) {
				// TODO Auto-generated method stub
			}});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_refresh) {
			updateCurrentPosition();
		}
		
		return true;
	}
	private void initialize(){
		mDevices = new Devices(mClient);
		mDevices.add(new Device(PushHandler.getHandle()));
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mCriteria = new Criteria();
		mCriteria.setAccuracy(Criteria.ACCURACY_COARSE); // �ᐸ�x
		mCriteria.setPowerRequirement(Criteria.POWER_LOW); // �����d��
		
		mPositions = new Positions(mClient);
		mPositions.read(new TableQueryCallback<Position>(){

			@Override
			public void onCompleted(List<Position> positions, int count,
					Exception exception, ServiceFilterResponse response) {
				if(positions.isEmpty()){
					mPosition = new Position();
				} else {
					mPosition = positions.get(0);
				}
				updateCurrentPosition();
			}} );


	}
	private void updateCurrentPosition(){
		mLocationManager.requestSingleUpdate(mLocationManager.getBestProvider(mCriteria, true), this, Looper.myLooper());
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mPosition.setLatitude(location.getLatitude());
		mPosition.setLogitude(location.getLongitude());
		mPositions.addOrUpdate(mPosition, new TableOperationCallback<Position>(){
			@Override
			public void onCompleted(Position position, Exception exception,
					ServiceFilterResponse response) {
				String text = "Lat:" + position.getLatitude() + ", Long:" + position.getLogitude();
				Toast.makeText(self, text, Toast.LENGTH_LONG).show();
			}
		} );
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}
