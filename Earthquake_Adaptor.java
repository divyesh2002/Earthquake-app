package com.example.earthquake_info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Earthquake_Adaptor extends ArrayAdapter<Earthquake> {
    private static final String LOCATION_SEPARATOR = " of ";

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formateMagnitude(Double magnitude)
    {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    public Earthquake_Adaptor(@NonNull Context context, List<Earthquake> earthquakes) {
        super(context,0,earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitem=convertView;
        if(listitem==null)
        {
            listitem= LayoutInflater.from(getContext()).inflate(R.layout.custom_adaptor,parent,false);
        }
        Earthquake current=getItem(position);
        TextView mMagnitudeview=(TextView) listitem.findViewById(R.id.Magnitude);

        GradientDrawable magnitudecircle=(GradientDrawable) mMagnitudeview.getBackground();

        int magnitude_background_color=getmagnitudeColor(current.getmMagnitude());

        magnitudecircle.setColor(magnitude_background_color);

        Double magnitude=current.getmMagnitude();
        mMagnitudeview.setText(formateMagnitude(magnitude));
        TextView mLocationview=(TextView) listitem.findViewById(R.id.location);
        TextView mLocationproper=(TextView) listitem.findViewById(R.id.locationproper);

        String locate=current.getmLocation();
        String location;
        String proper;

        if(locate.contains(LOCATION_SEPARATOR))
        {
            String[] parts=locate.split(LOCATION_SEPARATOR);
            location=parts[0]+LOCATION_SEPARATOR;
            proper=parts[1];
        }
        else
        {
            location=getContext().getString(R.string.near_the);
            proper=locate;
        }
        mLocationview.setText(location);
        mLocationproper.setText(proper);

        Date  date=new Date(current.getmTimeinmilisec());

        TextView mDateview=(TextView) listitem.findViewById(R.id.Date);
        String dateformat= formatDate(date);
        mDateview.setText(dateformat);

        TextView mTime=(TextView) listitem.findViewById(R.id.time);
        String timeof=formatTime(date);
        mTime.setText(timeof);

        return listitem;
    }

    private int getmagnitudeColor(Double magnitude)
    {
        int colorResource;
        int changeIntmagnitude=(int)Math.floor(magnitude);

        switch (changeIntmagnitude)
        {
            case 0:
            case 1:
                colorResource=R.color.magnitude1;
                break;
            case 2:
                colorResource=R.color.magnitude2;
                break;
            case 3:
                colorResource=R.color.magnitude3;
                break;
            case 4:
                colorResource=R.color.magnitude4;
                break;
            case 5:
                colorResource=R.color.magnitude5;
                break;
            case 6:
                colorResource=R.color.magnitude6;
                break;
            case 7:
                colorResource=R.color.magnitude7;
                break;
            case 8:
                colorResource=R.color.magnitude8;
                break;
            case 9:
                colorResource=R.color.magnitude9;
                break;
            default:
                colorResource=R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),colorResource);
    }
}
