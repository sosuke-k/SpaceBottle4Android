package com.spacebottle.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.spacebottle.R;

public class ReceiveMessageActivity extends Activity {

	private ImageView satelite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_message);
		satelite = (ImageView)findViewById(R.id.satelite);
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation a = new TranslateAnimation(
				Animation.ABSOLUTE,50,Animation.ABSOLUTE,0,
				Animation.ABSOLUTE,200,Animation.ABSOLUTE,0);
		a.setDuration(5000);
		a.setFillAfter(true);
		animationSet.addAnimation(a);
		satelite.startAnimation(animationSet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receive_message, menu);
		return true;
	}

}
