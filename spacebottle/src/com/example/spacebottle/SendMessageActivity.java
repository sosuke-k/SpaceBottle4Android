package com.example.spacebottle;

import android.app.Activity;
import android.content.Intent;
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

import com.spacebottle.controllers.HomeActivity;

public class SendMessageActivity extends Activity {

	private ImageView satelite;
	private ImageView bottle;
	private Display disp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message);
		satelite = (ImageView)findViewById(R.id.satelite_send);
		bottle = (ImageView)findViewById(R.id.bottle_send);
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		disp = wm.getDefaultDisplay();
		bottleAnim();
		Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            	sateliteAnim();
            }
        }, 5000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_message, menu);
		return true;
	}

	public void sateliteAnim(){
		bottle.setVisibility(View.GONE);
		satelite.setVisibility(View.VISIBLE);
		Animation anim = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,-disp.getWidth(),
				Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
		anim.setDuration(5000);
		satelite.startAnimation(anim);
		Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            	Intent intent2 = new Intent(getApplicationContext(),HomeActivity.class);
    			startActivity(intent2);
            }
        }, 4500);
	}

	public void bottleAnim(){
		bottle.setVisibility(View.VISIBLE);
		Animation anim1 = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,disp.getWidth()/2 + 500,Animation.ABSOLUTE,disp.getHeight()/2,Animation.ABSOLUTE,1000
				);
		Animation anim2 = new ScaleAnimation(1,0.1f,1,0.1f);
		AnimationSet anim_set = new AnimationSet(true);
		anim_set.setDuration(5000);
		anim_set.addAnimation(anim1);
		anim_set.addAnimation(anim2);
		bottle.startAnimation(anim_set);
	}

}
