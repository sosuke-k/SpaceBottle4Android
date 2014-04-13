package com.spacebottle.controllers;

import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.spacebottle.R;

public class ReceiveMessageActivity extends SBActivity {

	public static final String PREFERENCES_FILE_NAME = "preference";
	private ImageView satelite;
	private ImageView satelite_empty;
	private ImageView bottle;
	private Display disp;
	private SharedPreferences pref;

	private Intent intent;
	private Bundle bundle;
	private String ticketId;
	private Date limit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_message);
		satelite = (ImageView)findViewById(R.id.satelite);
		satelite_empty = (ImageView)findViewById(R.id.satelite_empty_r);
		bottle = (ImageView)findViewById(R.id.bottle);
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		disp = wm.getDefaultDisplay();
		intent = getIntent();
		pref = getSharedPreferences(PREFERENCES_FILE_NAME, 0);
		Long limit = (Long)pref.getLong("limit", (new Date()).getTime());
		Date now = new Date();

		if(now.getTime() < limit + 600000){
			sateliteAnim();
			Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                bottleAnim();
	            }
	        }, 5000);
		} else {
			sateliteGone();
		}
	}

	public void sateliteGone(){
		satelite.setVisibility(View.VISIBLE);
		Animation anim = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,
				Animation.ABSOLUTE,disp.getHeight()/2,Animation.ABSOLUTE,disp.getHeight() + 20);
		anim.setDuration(5000);
		satelite.startAnimation(anim);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            	Intent intent2 = new Intent(getApplicationContext(),HomeActivity.class);
            	intent2.putExtra("message_text",intent.getStringExtra("message_text"));
    			intent2.putExtra("satellite_id", intent.getStringExtra("satellite_id"));
    			intent2.putExtra("ticket_id",intent.getStringExtra("ticket_id"));
    			startActivity(intent2);
            }
        }, 4500);
	}
	public void sateliteAnim(){
		satelite.setVisibility(View.VISIBLE);
		Animation anim = new TranslateAnimation(Animation.ABSOLUTE,-disp.getWidth(),Animation.ABSOLUTE,0,
				Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
		anim.setDuration(5000);
		satelite.startAnimation(anim);
	}

	public void bottleAnim(){
		satelite.setVisibility(View.GONE);
		bottle.setVisibility(View.VISIBLE);
		satelite_empty.setVisibility(View.VISIBLE);
		Animation anim1 = new TranslateAnimation(Animation.ABSOLUTE,disp.getWidth()/2 + 500,Animation.ABSOLUTE,0,Animation.ABSOLUTE,1000,Animation.ABSOLUTE,disp.getHeight()/2
				);
		Animation anim2 = new ScaleAnimation(0.1f,1,0.1f,1);
		AnimationSet anim_set = new AnimationSet(true);
		anim_set.setDuration(5000);
		anim_set.addAnimation(anim1);
		anim_set.addAnimation(anim2);
		bottle.startAnimation(anim_set);
		Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            	Intent intent2 = new Intent(getApplicationContext(),PostActivity.class);
            	intent2.putExtra("message_text",intent.getStringExtra("message_text"));
    			intent2.putExtra("satellite_id", intent.getStringExtra("satellite_id"));
    			intent2.putExtra("ticket_id",intent.getStringExtra("ticket_id"));
    			startActivity(intent2);
            }
        }, 4500);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receive_message, menu);
		return true;
	}

}
