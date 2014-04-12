package com.spacebottle.controllers;

import java.util.List;

import com.example.spacebottle.MyHandler;
import com.example.spacebottle.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.spacebottle.helper.SBAuthenticateCallback;
import com.spacebottle.models.Device;
import com.spacebottle.models.Devices;
import com.spacebottle.models.Messages;
import com.spacebottle.models.Position;
import com.spacebottle.models.Positions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HomeActivity extends SBActivity implements LocationListener  {
	public static final String PREFERENCES_FILE_NAME = "preference";
	private Devices mDevices;
	private Positions mPositions;
	private Position mPosition;
	private LocationManager mLocationManager;
	private Criteria mCriteria;
	private SharedPreferences pref;
	private GoogleCloudMessaging gcm;
	private NotificationHub hub;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		self = this;
		setProgressBar((ProgressBar) findViewById(R.id.loadingProgressBar));
		hideProgressBar();


//		ImageView eisei = (ImageView) findViewById(R.id.eisei);
//		eisei.setOnClickListener(new OnClickListener(){

		Button btn = (Button)findViewById(R.id.buttonAddToDo);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				//Intent intent = new Intent(getApplicationContext(),PostActivity.class);
				Intent intent = new Intent(getApplicationContext(),ReceiveMessageActivity.class);
				intent.putExtra("position", mPosition);
				startActivity(intent);
			}
		});
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

	@SuppressWarnings("unchecked")
	private void registerWithNotificationHubs() {
	   new AsyncTask() {
	      @Override
	      protected Object doInBackground(Object... params) {
	         try {
	            String regid = gcm.register("879711313152");
	            //hub.register(regid);
	            mDevices = new Devices(mClient);
	    		mDevices.add(new Device(hub.register(regid).getRegistrationId()));
	         } catch (Exception e) {
	            return e;
	         }
	         return null;
	     }
	   }.execute(null, null, null);
	}
	@Override
	public void onResume(){
		super.onResume();
		pref = getSharedPreferences(PREFERENCES_FILE_NAME,0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("background-flag", 1);
		editor.commit();
	}

	@Override
	public void onPause(){
		super.onPause();
		pref = getSharedPreferences(PREFERENCES_FILE_NAME,0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("background-flag", 0);
		editor.commit();
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
		NotificationsManager.handleNotifications(this, "879711313152", PushHandler.class);
		gcm = GoogleCloudMessaging.getInstance(this);
		hub = new NotificationHub(getString(R.string.hub_name), getString(R.string.connection_string), this);
		registerWithNotificationHubs();


		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mCriteria = new Criteria();
		mCriteria.setAccuracy(Criteria.ACCURACY_COARSE); // �ｽ瘰ｸ�ｽx
		mCriteria.setPowerRequirement(Criteria.POWER_LOW); // �ｽ�ｽ�ｽ�ｽ�ｽd�ｽ�ｽ

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
