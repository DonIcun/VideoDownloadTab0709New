package com.skmei.videograber;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skmei.videograber.util.VideoplayerActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.io.File;

public class download_windows extends Activity {
    private static Button button_ld, button_sd, button_hd;
    private static ImageView playVideo, button_play;
    private static TextView loading,ddescription;
    public String d_description,img_url,dnama, durl, Ld_url, Sd_url, Hd_url, Fhd_url;
    public Double Ld_size, Sd_size, Hd_size, Fhd_size;
    private ProgressBar progress_bar_lista;
    InterstitialAd mInterstitial;

    //private VideoItem data_download;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {
            setContentView(R.layout.activity_download_windows_tab);
        } else {
            setContentView(R.layout.activity_download_windows);
        }

        button_ld = (Button)findViewById(R.id.btnLd);
        button_sd = (Button)findViewById(R.id.btnSd);
        button_hd = (Button)findViewById(R.id.btnHd);
        progress_bar_lista = (ProgressBar)findViewById(R.id.progressBar);
        loading = (TextView)findViewById(R.id.loading_down);
        ddescription = (TextView)findViewById(R.id.description);

        progress_bar_lista.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);

        //admob
        AdView adView = (AdView) this.findViewById(R.id.adViewDl);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(download_windows.this.getString(R.string.test_device_id))
                .build();
        adView.loadAd(adRequest);
        ////admob

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        d_description = extraBundle.getString(SearchResultsActivitySwipe.d_descrip);
        img_url = extraBundle.getString(SearchResultsActivitySwipe.dImgUrl);
        dnama = extraBundle.getString(SearchResultsActivitySwipe.d_Nama);
        durl = extraBundle.getString(SearchResultsActivitySwipe.d_URL);


        Ld_url = extraBundle.getString(SearchResultsActivitySwipe.dLd_URL);
        Log.i("Check LD URL",Ld_url);
        Sd_url = extraBundle.getString(SearchResultsActivitySwipe.dSd_URL);
        Log.i("Check SD URL",Sd_url);
        Hd_url = extraBundle.getString(SearchResultsActivitySwipe.dHd_URL);
        Log.i("Check HD URL",Hd_url);
        Fhd_url = extraBundle.getString(SearchResultsActivitySwipe.dFhd_URL);

        Ld_size = extraBundle.getDouble(SearchResultsActivitySwipe.dLd_Size);
        Sd_size = extraBundle.getDouble(SearchResultsActivitySwipe.dSd_Size);
        Hd_size = extraBundle.getDouble(SearchResultsActivitySwipe.dHd_Size);
        Fhd_size = extraBundle.getDouble(SearchResultsActivitySwipe.dFhd_Size);

        Log.i("Data LD",Ld_url);

        if (Ld_url != "0"){
            button_ld.setVisibility(View.VISIBLE);
            button_ld.setText("SD 360p " + (String.valueOf(String.format("%.2f", (Sd_size / 1000000)))) + " MB");
        }
        if (Sd_url != "0"){
            button_sd.setVisibility(View.VISIBLE);
            button_sd.setText("LD 480p " + (String.valueOf(String.format("%.2f", (Ld_size / 1000000)))) + " MB");
        }
        if (Hd_url != "0"){
            button_hd.setVisibility(View.VISIBLE);
            button_hd.setText("HD 720p " + (String.valueOf(String.format("%.2f", (Hd_size / 1000000)))) + " MB");
        }


        ddescription.setText(d_description);

        playVideo = (ImageView)findViewById(R.id.playVideoTmb);
        //VideoDowloaderApplication.getInstance("").getImagenVideoDownloader()
        //        .download(img_url, playVideo);

        Picasso.with(this)
                .load(img_url)
                .into(playVideo);

        button_play = (ImageView)findViewById(R.id.imageView_play);
        this.setTitle(dnama);
        OnClickButtonListener();
    }

    public void OnClickButtonListener(){
        button_ld.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("url download","log link");
                        Log.i("url download",Ld_url);
                        Toast.makeText(download_windows.this,R.string.loading_video_down, Toast.LENGTH_LONG).show();
                        interestial_down(Ld_url);

                    }
                }
        );
        button_sd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(download_windows.this,R.string.loading_video_down, Toast.LENGTH_LONG).show();
                        interestial_down(Sd_url);
                    }
                }
        );
        button_hd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(download_windows.this,R.string.loading_video_down, Toast.LENGTH_LONG).show();
                        interestial_down(Hd_url);
                    }
                }
        );
        button_play.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        button_play.setVisibility(View.INVISIBLE);
                        progress_bar_lista.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.VISIBLE);
                        Toast.makeText(download_windows.this,R.string.loading_video_play, Toast.LENGTH_LONG).show();
                        new PlayLinkAsyncTask(durl).execute();
                    }
                }
        );
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //    finish();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:

                Intent intent = new Intent(download_windows.this,SearchResultsActivitySwipe.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


                // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
                break;

        }

        return true;
    }

    private class PlayLinkAsyncTask extends AsyncTask<Void,Void,String> {
        private String playLink;

        public PlayLinkAsyncTask(String link_play){
            playLink = link_play;
        }
        @Override
        protected void onPreExecute(){
            interestial();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                // Log.i("url download",mVideoItem.getDownloadID());
                return playLink;
            }catch(Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String link_play){

            Intent playVideo = new Intent(download_windows.this,VideoplayerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("SOURCE", link_play);
            //bundle.putString("SOURCE", listItem.get(position).getSource());
            playVideo.putExtras(bundle);
            startActivity(playVideo);

        }
    }

    private class GenerateDownloadLinkAsyncTask extends AsyncTask<Void, Void, String> {
        private String downloadLink;
        private ProgressDialog mProgressDialog;
        //context = download_windows.this;

        public GenerateDownloadLinkAsyncTask(String download) {

            downloadLink = download;
        }

        @Override
        protected void onPreExecute() {
              //Log.i("Tag preExecute", downloadLink);
            //interestial();

        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                // Log.i("url download",mVideoItem.getDownloadID());
                return downloadLink;
            }catch(Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String downloadLink) {
            try{

                if(downloadLink==null){
                    Toast.makeText(context, "This video can't be downloaded at this time",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //Toast.makeText(download_windows.this, "PostExecute", Toast.LENGTH_SHORT).show();




                File folder = new File(Environment.getExternalStorageDirectory()+"/VideoDownload/");

                if(!folder.exists()){
                    folder.mkdir();

                }




                DownloadManager downloadmanager;
                downloadmanager = (DownloadManager) download_windows.this.getSystemService(Context.DOWNLOAD_SERVICE);
                System.out.println(downloadLink);
                Uri uri = Uri.parse(downloadLink);
                DownloadManager.Request request = new Request(uri);
                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle("Video Downloader")
                        .setDescription("Downloading Video "+dnama)
                        .setDestinationInExternalPublicDir("/VideoDownload", dnama + ".mp4");

                downloadmanager.enqueue(request);
                Toast.makeText(download_windows.this, "This video download queue", Toast.LENGTH_SHORT).show();
                onBackPressed();
                //Long reference = downloadmanager.enqueue(request);

            }catch(Exception e){
                Log.e("Myapp","Exception",e);
                Toast.makeText(download_windows.this, "This video can't be downloaded at this time", Toast.LENGTH_SHORT).show();
                //return null;
            }
        }

    }




    public void interestial(){
        //interstitial
        final InterstitialAd mInterstitial;
        mInterstitial = new InterstitialAd(download_windows.this);
        mInterstitial.setAdUnitId(download_windows.this.getString(R.string.int_unit_id));
        mInterstitial.loadAd(new AdRequest.Builder()
                .addTestDevice(download_windows.this.getString(R.string.test_device_id))
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
        //interstitial
    }

    public void interestial_down(final String link_down){
        //interstitial
        final InterstitialAd mInterstitial;
        mInterstitial = new InterstitialAd(download_windows.this);
        mInterstitial.setAdUnitId(download_windows.this.getString(R.string.int_unit_id));
        mInterstitial.loadAd(new AdRequest.Builder()
                .addTestDevice(download_windows.this.getString(R.string.test_device_id))
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

            @Override
            public void onAdClosed() {
                new GenerateDownloadLinkAsyncTask(link_down).execute();
            }
        });
        //interstitial
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(download_windows.this.getString(R.string.test_device_id))
                .build();

        mInterstitial.loadAd(adRequest);
    }

}
