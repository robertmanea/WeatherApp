package com.example.rober.weatherapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Location> locationList = new ArrayList<Location>();
    private ListView locationsListView;
    private SavedLocationArrayAdapter mCustomArrayAdapter;
    private LocationsHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                    }
                }
        );

        locationsListView = (ListView) findViewById(R.id.locationsList);

        mHelper = new LocationsHelper(this);

        mCustomArrayAdapter = new SavedLocationArrayAdapter(this, R.layout.item_location, locationList);
        locationsListView.setAdapter(mCustomArrayAdapter);

        getFromDB();

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location location = locationList.get(position);
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name",location.getLocationName());
                extras.putString("country",location.getLocationCountry());
                extras.putInt("id",location.getLocationID());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFromDB();
    }

    public void getFromDB() {
        locationList.clear();
        try {
            mHelper.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCustomArrayAdapter.notifyDataSetChanged();
    }
}
