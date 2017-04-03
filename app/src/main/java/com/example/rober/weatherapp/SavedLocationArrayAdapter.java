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
        TextView locationName;

    }

    public CustomArrayAdapter(Context context, int resource, ArrayList<Angajat> objects) {
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
            mHolder.tv_first_name = (TextView) mView.findViewById(R.id.tv_first_name);
            mHolder.tv_last_name = (TextView) mView.findViewById(R.id.tv_last_name);
            mHolder.tv_position = (TextView) mView.findViewById(R.id.tv_position);
            mHolder.tv_birthday = (TextView) mView.findViewById(R.id.tv_birthday);

            mView.setTag(mHolder);
        }

        ViewHolder holderView = (ViewHolder) mView.getTag();

        Angajat mAngajat = getItem(position);

        holderView.tv_first_name.setText(mAngajat.getFirst_name());
        holderView.tv_last_name.setText(mAngajat.getLast_name() + "; poz = " + position);
        holderView.tv_position.setText(mAngajat.getPosition());
        holderView.tv_birthday.setText(mAngajat.getBirthday());

        return mView;
    }

}
