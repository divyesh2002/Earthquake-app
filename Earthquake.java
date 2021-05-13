package com.example.earthquake_info;

public class Earthquake {
    private Double mMagnitude;
    private String mLocation;
    private Long mTimeinmilisec;
    private String murl;

    Earthquake(Double Magnitude,String Location,Long time,String url)
    {
        mMagnitude=Magnitude;
        mLocation=Location;
        mTimeinmilisec=time;
        murl=url;
    }

    public Double getmMagnitude()
    {
        return mMagnitude;
    }
    public String getmLocation()
    {
        return mLocation;
    }
    public Long getmTimeinmilisec()
    {
        return mTimeinmilisec;
    }
    public String getmurl()
    {
        return murl;
    }

}
