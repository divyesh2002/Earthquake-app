package com.example.earthquake_info;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    /** Sample JSON response for a USGS query */

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Earthquake> extractEarthquakes(String requesturl) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        URL url=createURL(requesturl);

        String jsonresponse="";
        try {
            jsonresponse=makehttprequest(url);
        } catch (IOException e) {

        }

        earthquakes=extractData(jsonresponse);
        return earthquakes;
    }

    private static URL createURL(String s)
    {
        URL url=null;
        try {
            url=new URL(s);
        } catch (MalformedURLException e) {
            //
        }
        return url;
    }

    private static String makehttprequest(URL url) throws IOException
    {
        String jsonresponse="";
        if(url==null)
        {
            return jsonresponse;
        }
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;
        try {
            httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode()==200)
            {
                inputStream=httpURLConnection.getInputStream();
                jsonresponse=readfrominput(inputStream);
            }

        } catch (IOException e) {

        }finally {
            if(httpURLConnection!=null)
            {
                httpURLConnection.disconnect();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
        }
        return jsonresponse;
    }

    private static String readfrominput(InputStream inputStream) throws IOException {
        StringBuilder builder=new StringBuilder();
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String Line=reader.readLine();
            while (Line!=null)
            {
                builder.append(Line);
                Line=reader.readLine();
            }
        }
        return builder.toString();
    }

    private static List<Earthquake> extractData(String jsonresponse)
    {
        if(TextUtils.isEmpty(jsonresponse))
        {
            return null;
        }
        List<Earthquake> ans=new ArrayList<Earthquake>();

        try {
            JSONObject baseresponse=new JSONObject(jsonresponse);
            JSONArray featurearray=baseresponse.getJSONArray("features");

            if(featurearray.length()>0)
            {
                for(int i=0;i<featurearray.length();i++)
                {
                    JSONObject firstFeature = featurearray.getJSONObject(i);
                    JSONObject properties = firstFeature.getJSONObject("properties");

                    Double magnitude=properties.getDouble("mag");
                    String location=properties.getString("place");
                    Long time=properties.getLong("time");
                    String weburl=properties.getString("url");

                    ans.add(new Earthquake(magnitude,location,time,weburl));
                }
            }
        } catch (JSONException e) {

        }
        return ans;
    }
}
