package com.skmei.videograber.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.skmei.videograber.R;


public class VideoplayerActivity extends Activity {

	private ProgressBar loading_circle;
	private TextView loading_text;
	private InterstitialAd mInterstitial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.videoplay);
		//Log.i("video player","bundle"+Uri.parse(getIntent().getStringExtra("SOURCE")));

		final VideoView video = (VideoView) findViewById(R.id.videoview);
		video.setVideoURI(Uri.parse(getIntent().getStringExtra("SOURCE").trim()));
		video.setMediaController(new MediaController(this));
		//Log.i("test play","player information");

		loading_circle = (ProgressBar) findViewById(R.id.progressBar_load_video);
		loading_text = (TextView)findViewById(R.id.loading_text);


		//admob

		AdView adView = (AdView) this.findViewById(R.id.adViewPlay);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice(this.getString(R.string.test_device_id))
				.build();
		adView.loadAd(adRequest);
		///admob




		video.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				Toast.makeText(VideoplayerActivity.this,"Fetch video", Toast.LENGTH_LONG);


				/*interstitial
				mInterstitial = new InterstitialAd(VideoplayerActivity.this);
				mInterstitial.setAdUnitId(VideoplayerActivity.this.getString(R.string.int_unit_id));
				mInterstitial.loadAd(new AdRequest.Builder()
						.addTestDevice(VideoplayerActivity.this.getString(R.string.test_device_id))
						.build());
				mInterstitial.setAdListener(new AdListener() {
					@Override
					public void onAdLoaded() {
						// TODO Auto-generated method stub
						super.onAdLoaded();
						//Pemanggilan Interestririal
						mInterstitial.show();
						//Pemanggilan Interestririal
					}
				});
				*/
				//interstitial
				video.start();
				loading_circle.setVisibility(View.INVISIBLE);
				loading_text.setVisibility(View.INVISIBLE);
			}
		});

		video.setOnCompletionListener(
				new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						//Log.i("VideoView", "onCompletion()");
						finish();
						//onBackPressed();
					}
				}
		);



	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			Log.d("CDA", "onKeyDown Called");
			onBackPressed();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onBackPressed() {
		Log.d("CDA", "onBackPressed Called");

		finish();
	}
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}





}


