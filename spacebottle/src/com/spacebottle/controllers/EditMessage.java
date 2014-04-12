package com.spacebottle.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.spacebottle.R;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.spacebottle.helper.SBAuthenticateCallback;
import com.spacebottle.models.Message;
import com.spacebottle.models.Messages;
import com.spacebottle.models.Position;

public class EditMessage extends SBActivity {

	public static final String PREFERENCES_FILE_NAME = "preference";
	private EditText editText;
	private InputMethodManager imm;
	private ImageView imageView;
	private double latitude;
	private double longitude;
	private Position mPosition;
	private SharedPreferences pref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_message);
		self = this;
		editText = (EditText)findViewById(R.id.editTextMessage);
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

		Intent intent = getIntent();
		mPosition = (Position)intent.getSerializableExtra("position");
		connect(new SBAuthenticateCallback(){

			@Override
			public void success() {
				hideProgressBar();
			}

			@Override
			public void error(Exception exception) {
				// TODO Auto-generated method stub
			}});
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_send) {
			sendMessage();
		}

		return true;
	}

	public void sendMessage(){
		Message msg = new Message();
		msg.setText(editText.getText().toString());
		latitude = mPosition.getLatitude();
		longitude = mPosition.getLogitude();
		msg.setLatitude(latitude);
		msg.setLongitude(longitude);
		msg.setSatelliteId("003419e4-185d-41b7-9263-96fbcf086fb1");
		msg.setTicketId("003419e4-185d-41b7-9263-96fbcf086fb1");
		msg.setAnonymous(false);
		Messages msgs = new Messages(mClient);
		msgs.add(msg,new TableOperationCallback<Message>(){
			@Override
			public void onCompleted(Message arg0, Exception arg1,
					ServiceFilterResponse arg2) {
				// TODO 自動生成されたメソッド・スタブ
				Toast.makeText(getApplicationContext(), arg0.getText(), Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}
}
