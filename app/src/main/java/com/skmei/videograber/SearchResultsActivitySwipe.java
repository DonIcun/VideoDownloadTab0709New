package com.skmei.videograber;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.skmei.videograber.downloader.CustomListItemAdapter;
import com.skmei.videograber.util.ApplicationController;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;

@SuppressLint("NewApi")
public class SearchResultsActivitySwipe extends Activity implements SwipeRefreshLayout.OnRefreshListener{

	public static int duration;
	public static String durasi_waktu = "0";

	public final static String d_descrip = "descrip";
	public final static String dImgUrl = "img";
	public final static String d_Nama = "name";
	public final static String d_URL = "d_URL";
	public final static String dLd_URL = "Ld_URL";
	public final static String dSd_URL = "Sd_URL";
	public final static String dHd_URL = "Hd_URL";
	public final static String dFhd_URL = "Fhd_URL";

	public final static String dLd_Size = "0";
	public final static String dSd_Size = "0";
	public final static String dHd_Size = "0";
	public final static String dFhd_Size = "0";

	private Double LDfile_size = 0.0 ;
	private Double SDfile_size = 0.0 ;
	private Double HDfile_size = 0.0 ;
	private Double FHDfile_size = 0.0 ;

	private String LDid = "0";
	private String SDid = "0";
	private String HDid = "0";
	private String FHDid ="0";


	private String TAG = SearchResultsActivitySwipe.class.getSimpleName();
	private SwipeRefreshLayout swipeRefreshLayout;
	private TextView txtQuery, textLoading;
	private LinkedList<VideoItem> listItem;
	private CustomListItemAdapter adapter;
	private String query;
	private ProgressBar progress_bar_lista;
	private Menu menuHolder;
	private boolean[] filtro;//0-metacafe,1-daylimotion,2-vimeo

	private String result,result2,result1;
	private ProgressDialog dialog;
	private int page=1;
	private int i,j;
	private ApplicationController My;

	private InterstitialAd mInterstitial;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_search_layout);
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
		
		//admob

		//AdView adView = (AdView) this.findViewById(R.id.adViewSearchSwipe);
	  	//AdRequest adRequest = new AdRequest.Builder()
	  	//	.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	  	//	.addTestDevice(this.getString(R.string.test_device_id))
	  	//	.build();
	  	//adView.loadAd(adRequest);



		////admob

		// get the action bar
		//ActionBar actionBar = getActionBar();
		// Enabling Back navigation on Action Bar icon
		//actionBar.setDisplayHomeAsUpEnabled(true);

		//txtQuery = (TextView) findViewById(R.id.txtQuery);

		textLoading = (TextView) findViewById(R.id.texto_cargando);

		handleIntent(getIntent());

		ListView listViewItem = (ListView) findViewById(R.id.list);
		listItem = new LinkedList<VideoItem>();

		Configuration config = getResources().getConfiguration();
		if (config.smallestScreenWidthDp >= 600) {
			adapter = new CustomListItemAdapter(SearchResultsActivitySwipe.this,
					R.layout.item_list_videosearch, listItem);
		} else {
			adapter = new CustomListItemAdapter(SearchResultsActivitySwipe.this,
					R.layout.item_list_videosearch, listItem);
		}

        		// Swipe

		swipeRefreshLayout.setOnRefreshListener(this);

		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
				fetchMovies();
			}
		});


		listViewItem.setAdapter(adapter);
		listViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Log.i("Posisition Click", listItem.get(position).toString());
				new GenerateDownloadLinkAsyncTask(listItem.get(position)).execute();    //download


			}
		});



	}

	@Override
	public void onRefresh() {
		fetchMovies();
	}

	private void fetchMovies() {

		// showing refresh animation before making http call
		swipeRefreshLayout.setRefreshing(true);

		Random rand = new Random();
		int X = rand.nextInt(10);
		Log.i("Rand", ""+X);

		AdView adViewA = (AdView) findViewById(R.id.adViewSearchSwipeAtas);
		AdView adViewB = (AdView) findViewById(R.id.adViewSearchSwipeBawah);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice(this.getString(R.string.test_device_id))
				.build();
		adViewA.setVisibility(View.INVISIBLE);
		adViewB.setVisibility(View.INVISIBLE);

		if ( X % 3 == 0) {

			adViewA.loadAd(adRequest);
			adViewA.setVisibility(View.VISIBLE);
		} else {

			adViewB.loadAd(adRequest);
			adViewB.setVisibility(View.VISIBLE);
		}
		//admob



		/// comment tambahan
		// tambah 2



		////admob

		// Volley's json array request object

		String url = "http://api.vidible.tv/search/"+query.replace(" ", "%20")+
				"/videos.json?num_of_videos=6&page="+page+"&show_renditions=true&sid=142";
        //Log.i("URL",url);

		JSONObject response = new JSONObject();
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,url, response,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject object) {
						try {
							Double file_size;
							//JSONObject movieObj = response.getJSONObject(i);
							//JSONObject object = new JSONObject(result);
							//Log.i("response", object.toString());

							String data = object.optString("items");
							//Log.i("response 2", data);

							JSONArray array = new JSONArray(data);

							if (array.length() > 0) {
								for (int i = 0; i < (array.length()); i++) {
									try {
										String idPrincipal = array.getJSONObject(i).optString("id");
										String title = array.getJSONObject(i).optString("title");
										String description = array.getJSONObject(i).optString("description");
										String url_thumb = array.getJSONObject(i).optString("image");
										String dUrl = array.getJSONObject(i).optString("videoUrl");

										//durasi_waktu = array.getJSONObject(j).optString("duration");
										duration = Integer.parseInt(array.getJSONObject(i).optString("duration"));

										int[] time = convertSeconds(duration);
										String hours = String.valueOf(time[0]);
										String minuts = String.valueOf(time[1]);
										String seconds = String.valueOf(time[2]);


										JSONArray jarray = new JSONArray(array.getJSONObject(i).optString("renditionDetails"));
										//Log.i("renditionDetails", url_thumb);
										//arr_duration.add((duration/60)+":"+(duration%60));
										for (int j = 0; j < jarray.length(); j++) {
											//// Log.i("5min loop",String.valueOf(j));
											String typeName = jarray.getJSONObject(j).optString("typeName");
											String formatFile = jarray.getJSONObject(j).optString("format"); // 1


											if ((typeName.equals("LD")) && (formatFile.equals("mp4"))) {
												LDid = jarray.getJSONObject(j).optString("url");
												LDfile_size = Double.parseDouble(jarray.getJSONObject(j).optString("fileSize"));
												//Log.i("URL LD",LDid);

											} else if ((typeName.equals("SD")) && (formatFile.equals("mp4"))) {
												SDid = jarray.getJSONObject(j).optString("url");
												SDfile_size = Double.parseDouble(jarray.getJSONObject(j).optString("fileSize"));
												//Log.i("URL SD",SDid);

											} else if (typeName.equals("HD")) {
												HDid = jarray.getJSONObject(j).optString("url");
												HDfile_size = Double.parseDouble(jarray.getJSONObject(j).optString("fileSize"));
												//Log.i("URL HD",HDid);

											} else if ((typeName.equals("FHD")) && (formatFile.equals("mp4"))) {
												FHDid = jarray.getJSONObject(j).optString("url");
												FHDfile_size = Double.parseDouble(jarray.getJSONObject(j).optString("fileSize"));
												//Log.i("URL SD",FHDid);
											}
										}


									//int duration = Integer.valueOf(durasi_waktu);



									VideoItem item = new VideoItem(url_thumb, title,
											description, dUrl, LDid, SDid, HDid, FHDid,
											LDfile_size, LDfile_size, SDfile_size, HDfile_size, FHDfile_size,
											hours + ":" + minuts + ":" + seconds, idPrincipal);
									item.setPrivateID(idPrincipal);

									//Log.i("Check Duration", hours + ":" + minuts + ":" + seconds );

									listItem.add(0, item);
									} catch (Exception e){
										Log.i("Error", e.toString());
									};
								}
								++page;
								if (page % 5 == 0){
									//interstitial
									mInterstitial = new InterstitialAd(SearchResultsActivitySwipe.this);
									mInterstitial.setAdUnitId(SearchResultsActivitySwipe.this.getString(R.string.int_unit_id));
									mInterstitial.loadAd(new AdRequest.Builder()
											.addTestDevice(SearchResultsActivitySwipe.this.getString(R.string.test_device_id))
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
								};
							}

							swipeRefreshLayout.setRefreshing(false);
						} catch (JSONException e) {
							e.printStackTrace();
						}

						adapter.notifyDataSetChanged();

						// stopping swipe refresh
						swipeRefreshLayout.setRefreshing(false);

						if(adapter.getCount()>0){
							//progress_bar_lista.setVisibility(View.INVISIBLE);
							textLoading.setVisibility(View.INVISIBLE);
						}
						else{
							//progress_bar_lista.setVisibility(View.INVISIBLE);
							textLoading.setText(R.string.empty_text);
						}

					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//Log.i("cher error ","Server Error: " + error.getMessage());

				Toast.makeText(getApplicationContext(), R.string.Connection , Toast.LENGTH_LONG).show();
				//progress_bar_lista.setVisibility(View.INVISIBLE);
				textLoading.setText(R.string.empty_text);
				// stopping swipe refresh
				swipeRefreshLayout.setRefreshing(false);
			}
		});
				// Adding request to request queue
		VideoDowloaderApplication.getInstance("req").addToRequestQueue(req);

		adapter.notifyDataSetChanged();
	}


	protected String getText(String url) throws Exception {
		String response = "";
				
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();

			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	
	private class GenerateDownloadLinkAsyncTask extends AsyncTask<Void, Void, String> {
		
		private VideoItem mVideoItem;
		private ProgressDialog mProgressDialog;
		
		public GenerateDownloadLinkAsyncTask(VideoItem pVideoItem) {
			mVideoItem = pVideoItem;

		}
		
		@Override
		protected void onPreExecute() {

			mProgressDialog = ProgressDialog.show(SearchResultsActivitySwipe.this,
					null,
					getResources().getString(R.string.loading_video), true, true,
					new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							cancel(true);							
						}
					});
		}

		@Override
		protected String doInBackground(Void... params) {

			try{
				return mVideoItem.getVIDEO_ID();
			}catch(Exception e){
				return null;
			}

		}
		
		@Override
		protected void onPostExecute(String downloadLink) {

			try{

				Intent I = new Intent(SearchResultsActivitySwipe.this,download_windows.class);
				Bundle bun = new Bundle();
				String d_description = mVideoItem.getDescription();
				String ImgUrl = mVideoItem.getImgThumb();
				String dNama = mVideoItem.getDownloadName();
				String d_url = mVideoItem.getVIDEO_ID();


				String LdUrl = mVideoItem.getVIDEO_ID_LD();
				Double LdSize = mVideoItem.getSizeLD();

				String SdUrl = mVideoItem.getVIDEO_ID_SD();
				Double SdSize = mVideoItem.getSizeSD();

				String HdUrl = mVideoItem.getVIDEO_ID_HD();
				Double HdSize = mVideoItem.getSizeHD();

				String FhdUrl = mVideoItem.getVIDEO_ID_FHD();
				Double FhdSize = mVideoItem.getSizeFHD();

				bun.putString(d_descrip,d_description);
				bun.putString(dImgUrl,ImgUrl);
				bun.putString(d_Nama,dNama);
				bun.putString(d_URL,d_url);

				bun.putString(dLd_URL,LdUrl);
				bun.putString(dSd_URL,SdUrl);
				bun.putString(dHd_URL,HdUrl);
				bun.putString(dFhd_URL,FhdUrl);

				bun.putDouble(dLd_Size, LdSize);
				bun.putDouble(dSd_Size, SdSize);
				bun.putDouble(dHd_Size, HdSize);
				bun.putDouble(dFhd_Size, FhdSize);

				I.putExtras(bun);
				startActivity(I);

				mProgressDialog.dismiss();
			}catch(Exception e){

				mProgressDialog.dismiss();
				Log.e("Error Open",e.toString());
				Toast.makeText(SearchResultsActivitySwipe.this, "This video can't be downloaded at this time", Toast.LENGTH_SHORT).show();

			}
		}
		
	}
	
	private int[] convertSeconds(int seconds) {

		int hours = seconds / 3600;
		int remainder = seconds - hours * 3600;
		int mins = remainder / 60;
		remainder = remainder - mins * 60;
		int secs = remainder;

		int[] ints = { hours, mins, secs };
		return ints;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}


	/**
	 * Handling intent data
	 */
	private void handleIntent(Intent intent) {
		query = intent.getStringExtra(SearchManager.QUERY);

		Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_search);
		mActionBarToolbar.setTitle("Search for "+query);

	}
}
