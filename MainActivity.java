package com.example.earthquake_info;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String USGS_REQUEST_URL ="https://earthquake.usgs.gov/fdsnws/event/1/query";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private ListView listView;
    private Earthquake_Adaptor madaptor;
    private TextView emptyview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.list);
        emptyview=findViewById(R.id.empty_view);

        madaptor=new Earthquake_Adaptor(this,new ArrayList<Earthquake>());
        listView.setAdapter(madaptor);
        listView.setEmptyView(emptyview);

//        EarthQuake_Asynctask task = new EarthQuake_Asynctask();
//        task.execute(USGS_REQUEST_URL);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected)
        {
            LoaderManager loaderManager;
            loaderManager = getLoaderManager();

            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else
        {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            emptyview.setText("No INTERNET connection");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake current=madaptor.getItem(position);

                Uri go_to_website=Uri.parse(current.getmurl());

                Intent get_web=new Intent(Intent.ACTION_VIEW,go_to_website);

                startActivity(get_web);
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, @Nullable Bundle bundle) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String minmag=sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderby=sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseuri=Uri.parse(USGS_REQUEST_URL);
        Uri.Builder builder=baseuri.buildUpon();

        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("limit","10");
        builder.appendQueryParameter("minmag",minmag);
        builder.appendQueryParameter("orderby",orderby);


        return new Earthquake_Loader(this,builder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        madaptor.clear();
        emptyview.setText(R.string.no_earthquakes);
        if(data!=null&&!data.isEmpty())
        {
            madaptor.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
         madaptor.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
            Intent settingintent=new Intent(this,settingsActivity.class);
            startActivity(settingintent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    private class EarthQuake_Asynctask extends AsyncTask<String,Void, List<Earthquake>>
//    {
//
//        @Override
//        protected List<Earthquake> doInBackground(String... urls) {
//            if(urls[0]==null||urls.length<0)
//            {
//                return null;
//            }
//            List<Earthquake> result=QueryUtils.extractEarthquakes(urls[0]);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<Earthquake> earthquakes) {
//            madaptor.clear();
//
//            if(!earthquakes.isEmpty()&&earthquakes!=null)
//            {
//                madaptor.addAll(earthquakes);
//            }
//        }
//    }
}