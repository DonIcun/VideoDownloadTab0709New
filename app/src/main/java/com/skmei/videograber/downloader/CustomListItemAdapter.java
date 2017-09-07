package com.skmei.videograber.downloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skmei.videograber.R;
import com.skmei.videograber.VideoItem;
import com.skmei.videograber.download_windows;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

//import com.skmei.videograber.download_windows;

public class CustomListItemAdapter extends ArrayAdapter<VideoItem> {

	Context context;
	LinkedList<VideoItem> drawerItemList;
	int layoutResID;

	public CustomListItemAdapter(Context context, int layoutResourceID, LinkedList<VideoItem> listItems) {
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub


		VideoItemHolder drawerHolder;
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new VideoItemHolder();

			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.imgThumb = (ImageView) view.findViewById(R.id.imageView1);
			drawerHolder.txtTitle = (TextView) view.findViewById(R.id.textView1);

			drawerHolder.txtDuration = (TextView) view.findViewById(R.id.textView3);
			//drawerHolder.txtSize = (TextView) view.findViewById(R.id.textView4);

			view.setTag(drawerHolder);

		} else {
			drawerHolder = (VideoItemHolder) view.getTag();

		}

		final VideoItem dItem = (VideoItem) this.drawerItemList.get(position);


		try {
			if (dItem.getImgThumb()!="") {
				Picasso.with(this.context)
						.load(dItem.getImgThumb())
						.into(drawerHolder.imgThumb);
			}

			drawerHolder.txtTitle.setText(dItem.getTittle());
			//drawerHolder.txtDescription.setText(dItem.getDescription());
			drawerHolder.txtDuration.setText("duration " + dItem.getDuration());
			//drawerHolder.txtSize.setText("size " + (String.valueOf(String.format("%.2f", (dItem.getSize() / 1000000)))) + " MB");
			//drawerHolder.txtSize.setText("Size SD320 LD420 HD720");
		} catch (Exception e) {
			return null;
		}


		//drawerHolder.imgTipo.setImageResource(R.drawable.ic_launcher);
		return view;
	}
	

	private class GenerateDownloadLinkAsyncTask extends AsyncTask<Void, Void, String> {
		
		private VideoItem mVideoItem;
		private ProgressDialog mProgressDialog;
		private Activity activity;

        public GenerateDownloadLinkAsyncTask(Activity activity){
            this.activity = activity;
        }

		public GenerateDownloadLinkAsyncTask(VideoItem pVideoItem) {
			mVideoItem = pVideoItem;
		}
		
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Void... params) {
            try{
                return mVideoItem.getVIDEO_ID();
            } catch (Exception e){
                return null;
            }
		}
		
		@Override
		protected void onPostExecute(String downloadLink) {
            Intent I = new Intent(activity,download_windows.class);
            activity.startActivity(I);
            //activity.startActivity(new Intent(activity, download_windows.class));
		}
	}
		
	private static class VideoItemHolder {
		ImageView imgThumb;
		TextView txtTitle;
		TextView txtDescription;
		TextView txtDuration;
		TextView txtSize;
		ImageView imgDownload;
		ImageView imgTipo;
	}
}