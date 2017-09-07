package com.skmei.videograber;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.skmei.videograber.R.id.btSearch;

/**
 * Created by Ratan on 7/29/2015.
 */
public class SearchFragment extends Fragment{
    private AdView adView;
    private EditText edittext;
    private SearchView searchView;
    private static Button bt_search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.tabfrag_search_layout,null);

        searchView = (SearchView)rootview.findViewById(R.id.searchview_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Search submit", query);

                if (query.length() > 3)
                {
                    Intent mIntent = new Intent(getContext(), SearchResultsActivitySwipe.class);
                    mIntent.putExtra(SearchManager.QUERY, query);
                    startActivity(mIntent);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.i("change sumbit", newText);


                return false;
            }
        });

        bt_search = (Button) rootview.findViewById(btSearch);
        bt_search.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String query_input = searchView.getQuery().toString();

                        if (query_input.length() > 3)
                        {
                            Intent mIntent = new Intent(getContext(), SearchResultsActivitySwipe.class);
                                mIntent.putExtra(SearchManager.QUERY, query_input);
                            startActivity(mIntent);

                        }
                    }
                }
        );
        load_banner();
        return rootview;
    }

    public void load_banner(){
        //admob
        adView = (AdView) getActivity().findViewById(R.id.adViewTab);

        AdRequest adRequest = new AdRequest.Builder()

                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(this.getString(R.string.test_device_id))
                .build();

        adView.loadAd(adRequest);
        ////admob
    }

    /*
    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        Log.i("Search submit", query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.i("change sumbit", query);
        if (query.length() > 3)
        {
            Intent mIntent = new Intent(getContext(), SearchResultsActivitySwipe.class);
            mIntent.putExtra(SearchManager.QUERY, query);
            //mIntent.putExtra("Filtro", filtro);
            startActivity(mIntent);


            //searchResults.setVisibility(myFragmentView.VISIBLE);
            //myAsyncTask m= (myAsyncTask) new myAsyncTask().execute(newText);
        }
        else
        {

            //searchResults.setVisibility(myFragmentView.INVISIBLE);
        }
        return false;
    }
    */

}
