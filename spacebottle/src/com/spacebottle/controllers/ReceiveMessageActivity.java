package com.spacebottle.controllers;

import android.app.Activity;
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

public class ReceiveMessageActivity extends Activity {

	private ImageView satelite;
	private ImageView bottle;
	private Display disp;

	private SequentialAnimationsRunner anim_runner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_message);
		satelite = (ImageView)findViewById(R.id.satelite);
		bottle = (ImageView)findViewById(R.id.bottle);

		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		disp = wm.getDefaultDisplay();
		sateliteAnim();
		Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bottleAnim();
            }
        }, 5000);
	}
	public void sateliteAnim(){
		satelite.setVisibility(View.VISIBLE);
		Animation anim = new TranslateAnimation(Animation.ABSOLUTE,disp.getWidth(),Animation.ABSOLUTE,0,
				Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
		anim.setDuration(5000);
		satelite.startAnimation(anim);
	}

	public void bottleAnim(){
		bottle.setVisibility(View.VISIBLE);
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
            	finish();
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
