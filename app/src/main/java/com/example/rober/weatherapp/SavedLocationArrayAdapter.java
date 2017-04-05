package com.example.rober.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rober on 03-Apr-17.
 */

public class SavedLocationArrayAdapter extends ArrayAdapter<Location> {

    private int mResource;
    private Context mContext;

    public static class ViewHolder{
        TextView location;
    }

    public SavedLocationArrayAdapter(Context context, int resource, ArrayList<Location> objects) {
        super(context, resource, objects);

        this.mResource = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View mView = convertView;
        if(mView == null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            mView = inflater.inflate(mResource, null);

            ViewHolder mHolder = new ViewHolder();
            mHolder.location = (TextView) mView.findViewById(R.id.locationCell);

            mView.setTag(mHolder);
        }

        ViewHolder holderView = (ViewHolder) mView.getTag();

        Location mLocation = getItem(position);

        holderView.location.setText(mLocation.getLocationName()+','+mLocation.getLocationCountry());

        return mView;
    }

}
