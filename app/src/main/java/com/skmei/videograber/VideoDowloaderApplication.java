package com.skmei.videograber;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.skmei.videograber.util.ImagenDownloader;

public class VideoDowloaderApplication extends Application {
	private static VideoDowloaderApplication videoDowloaderCulturalApplication;
	private ImagenDownloader mImagenVideoDownloader;

	/*
	public static VideoDowloaderApplication getInstance(String pilihan) {
			return videoDowloaderCulturalApplication;
	}*/

	public VideoDowloaderApplication() {
		videoDowloaderCulturalApplication = this;
	}


    //tambahan
	public static final String TAG = VideoDowloaderApplication.class.getSimpleName();
	private RequestQueue mRequestQueue;
	private static VideoDowloaderApplication mInstance;
	//tambahan
	
	
	@Override
	public void onCreate() {
		mImagenVideoDownloader = new ImagenDownloader(getResources(),
				BitmapFactory.decodeResource(getResources(),
						R.drawable.imgthumb));
		super.onCreate();
		mInstance = this;
	}
	
	/**
	 * @return the mImagenVideoDownloader
	 */
	public ImagenDownloader getImagenVideoDownloader() {
		return mImagenVideoDownloader;
	}


	public static synchronized VideoDowloaderApplication getInstance(String pilihan) {
		if (pilihan == "req")
		{
			return mInstance;
		}
		else
		{
			return videoDowloaderCulturalApplication;
		}

	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}



}
