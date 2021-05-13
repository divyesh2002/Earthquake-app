package com.example.earthquake_info;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class Earthquake_Loader extends AsyncTaskLoader<List<Earthquake>> {
    private static final String LOG_TAG = Earthquake_Loader.class.getName();

    /** Query URL */
    private String mUrl;
    public Earthquake_Loader(Context context,String url) {
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl==null)
        {
            return null;
        }
        List<Earthquake> ans=QueryUtils.extractEarthquakes(mUrl);
        return ans;
    }
}
