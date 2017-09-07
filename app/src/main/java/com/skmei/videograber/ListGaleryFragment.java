package com.skmei.videograber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.skmei.videograber.util.VideoplayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

/**
 * Created by Ratan on 7/29/2015.
 */
public class ListGaleryFragment extends Fragment {
    private AdView adView;
    private ListVideosManager adapter;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabfrag_galery_layout,null);

        mListView = (ListView) rootView.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapterView, View view,
                                        int n, long n2) {
                    File mFile = (File) adapterView.getItemAtPosition(n);
                    final String fileName = mFile.getName();
                    final Intent intent = new Intent(getActivity(), VideoplayerActivity.class);
                    final String string = String.valueOf(Environment
                            .getExternalStorageDirectory().getAbsolutePath())
                            + "/VideoDownload/" + fileName;
                    try {
                        //String d="http://www.osco.in/instavideoold/uploads/155/032810.mp4";

                        Intent i = new Intent(getActivity(), VideoplayerActivity.class);
                        //i.setData(Uri.parse(d));
                        //i.setDataAndType(Uri.parse(string), "video/*");
                        Bundle bundle = new Bundle();
                        bundle.putString("SOURCE", string);
                        i.putExtras(bundle);
                        Log.i("File Location", string);
                        getActivity().startActivity(i);

                    } catch (Exception ex) {
                        intent.setDataAndType(Uri.parse(String.valueOf(Environment
                                .getExternalStorageDirectory().getAbsolutePath())
                                + "/VideoDownload/"), "*/*");
                        intent.setAction("android.intent.action.GET_CONTENT");
                        getActivity().startActivity(intent);
                    }
                }
            });

            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(final AdapterView<?> adapterView,
                                               final View view, final int n, final long n2) {
                    ////Log.i("Error check","Listener");
                    File mFile = (File) adapterView.getItemAtPosition(n);
                    final String fileName = mFile.getName();

                    new AlertDialog.Builder(getActivity())
                            .setTitle(fileName)
                            .setMessage( "Delete this video?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                private final String valurl = String
                                        .valueOf(Environment
                                                .getExternalStorageDirectory()
                                                .getAbsolutePath())
                                        + "/VideoDownload/"
                                        + fileName;

                                public void onClick( DialogInterface dialogInterface,int n) {
                                    new File(this.valurl).delete();
                                    adapter.refresh();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int n) {
                        }
                    }).show();
                    return true;
                }
            });

            creatAdapter();
        load_banner();

        return rootView;
    }


    private void creatAdapter() {

        adapter = new ListVideosManager();
        mListView.setAdapter(adapter);

    }


    public void load_banner(){
        //admob
        adView = (AdView) getActivity().findViewById(R.id.adViewTab);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(this.getString(R.string.test_device_id))
                .build();

        adView.loadAd(adRequest);
        Random rand = new Random();
        int X = rand.nextInt(10);
        Log.i("Rand", ""+X);
        if ( X % 2 == 0) {
            adView.setVisibility(View.VISIBLE);
        } else {
            adView.setVisibility(View.INVISIBLE);
        }


        ////admob
    }

    class ListVideosManager extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<File> videos;

        public ListVideosManager() {
            super();
            ////Log.i("Error check","List video manager");
            this.videos = new ArrayList<File>();
            final File externalStoragePublicDirectory = Environment
                    .getExternalStoragePublicDirectory("/VideoDownload");
            externalStoragePublicDirectory.mkdirs();
            this.inflater = LayoutInflater.from(getActivity());
            final File[] listFiles = externalStoragePublicDirectory.listFiles();
            ////Log.i("Error check","List video manager2");
            try {
                for (int i = 0; i < listFiles.length; ++i) {
                    this.videos.add(listFiles[i]);
                }

                // para order un ArrayList de Objetos
                Collections.sort(videos, new Comparator<File>() {
                    @Override
                    public int compare(File lhs, File rhs) {
                        Date mDate = new Date(rhs.lastModified());
                        return  mDate.compareTo(new Date(lhs.lastModified()));
                    }
                });

            } catch (Exception e) {

                Toast.makeText(getActivity(), "No Videos Found", Toast.LENGTH_SHORT).show();
            }
            ////Log.i("Error check","List video manager3");

        }
        public int getCount() {
            return this.videos.size();
        }

        public Object getItem(final int n) {
            return this.videos.get(n);
        }

        public long getItemId(final int n) {
            return n;
        }

        public View getView(final int n, final View view,
                            final ViewGroup viewGroup) {
            ////Log.i("Error check","Get view point1");
            View inflate = view;
            if (inflate == null) {
                Configuration config = getResources().getConfiguration();
                /*
                if (config.smallestScreenWidthDp >= 600) {
                    inflate = this.inflater.inflate(R.layout.item_list_download_tab, viewGroup, false);
                } else {
                    inflate = this.inflater.inflate(R.layout.item_list_download, viewGroup, false);
                } */

                inflate = this.inflater.inflate(R.layout.item_list_download, viewGroup, false);



                final VideoListHolder tag = new VideoListHolder();
                tag.imagen = (ImageView) inflate.findViewById(R.id.imagen);
                tag.nombre = (TextView) inflate.findViewById(R.id.text_nombre);
                tag.size = (TextView) inflate.findViewById(R.id.text_size);
                tag.fecha = (TextView) inflate.findViewById(R.id.text_fecha);
                tag.duration = (TextView) inflate.findViewById(R.id.text_dur);
                inflate.setTag((Object) tag);
            }

            File mFile = videos.get(n);

            Date lastModDate = new Date(mFile.lastModified());

            ((VideoListHolder) inflate.getTag()).nombre	.setText(mFile.getName());

            //String fecha =  (String) lastModDate.toString();
            //String fecha =  (String) DateUtils.getRelativeTimeSpanString(mFile.lastModified());
            String fecha =  (String) DateUtils.getRelativeTimeSpanString(mFile.lastModified());
            ((VideoListHolder) inflate.getTag()).fecha.setText(fecha);


            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            ////Log.i("Error check","Get view point2");
            retriever.setDataSource(String.valueOf(Environment
                    .getExternalStorageDirectory().getAbsolutePath())
                    + "/VideoDownload/" + mFile.getName());

            ////Log.i("Error check","Get view point3");

            Bitmap mBitmap=	retriever.getFrameAtTime(10*1000000);
            if(mBitmap!=null)
                ((VideoListHolder) inflate.getTag()).imagen.setImageBitmap(mBitmap);
            else{
                ((VideoListHolder) inflate.getTag()).imagen.setImageResource(R.drawable.imgthumb);
            }

            //((VideoListHolder) inflate.getTag()).duration.setText(mFile.getDuration());


            long size = mFile.length()/1024;
            if(size>1000){
                size = size/1000;
            }

            //long ukuran= mFile.getTotalSpace();

            ((VideoListHolder) inflate.getTag()).size
                    .setText(Long.toString(size) + " MB");

            long duration = 0;
            String dur = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (dur != null) {
                //duration = Integer.parseInt(dur);
                duration = Long.parseLong(dur );
                duration = duration/1000;
            }
            long h = duration / 3600;
            long m = (duration - h * 3600) / 60;
            long s = duration - (h * 3600 + m * 60);




            ((VideoListHolder) inflate.getTag()).duration.setText(h +" : "+ m + " : " + s );
            //((VideoListHolder) inflate.getTag()).duration.setText(Long.toString(duration));

            ////Log.i("Error check","Get view point4");
            return inflate;
        }

        public void refresh() {
            this.videos.clear();
            final File[] listFiles = Environment
                    .getExternalStoragePublicDirectory("VideoDownload")
                    .listFiles();
            for (int i = 0; i < listFiles.length; ++i) {
                this.videos.add(listFiles[i]);
            }
            //Log.i("Error check","Get view refresh ");
            // para order un ArrayList de Objetos
            Collections.sort(videos, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    Date mDate = new Date(rhs.lastModified());
                    return  mDate.compareTo(new Date(lhs.lastModified()));
                }
            });
            this.notifyDataSetChanged();
            //Log.i("Error check","Get view refresh 2");
        }
        class VideoListHolder {
            ImageView imagen;
            TextView nombre;
            TextView size;
            TextView fecha;
            TextView duration;
        }
    }

    @Override
    public void onResume() {
        try {
            if(adapter!=null)
                adapter.refresh();
        } catch (Exception e) {
            // TODO: handle exception
        }
        super.onResume();
    }

}


