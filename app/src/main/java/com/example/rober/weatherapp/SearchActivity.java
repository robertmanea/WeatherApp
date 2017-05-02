package com.example.rober.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rober on 03-Apr-17.
 */

public class SearchActivity extends AppCompatActivity {

    private ArrayList<Location> mListCustom;
    private SavedLocationArrayAdapter mCustomArrayAdapter;
    public ListView searchListView;
    private EditText searchTextView;
    private LocationsHelper mHelper;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mProgress.setIndeterminate(true);
        mProgress.setVisibility(View.GONE);

        searchTextView = (EditText) findViewById(R.id.searchTextView);

        mHelper = new LocationsHelper(this);

        searchListView = (ListView) findViewById(R.id.searchLocationsList);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Location location = mListCustom.get(position);
                mHelper.insert(location.getLocationName(),location.getLocationCountry(),location.getLocationID());
                Intent intent = new Intent(SearchActivity.this, WeatherActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name",location.getLocationName());
                extras.putString("country",location.getLocationCountry());
                extras.putInt("id",location.getLocationID());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        mListCustom = new ArrayList<>();
        mCustomArrayAdapter = new SavedLocationArrayAdapter(this, R.layout.item_location, mListCustom);
        searchListView.setAdapter(mCustomArrayAdapter);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchTextView.getWindowToken(), 0);
                        searchByName();
                    }
                }
        );
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    protected void searchByName() {
        mProgress.setVisibility(View.VISIBLE);
        final Uri builtUri = Uri.parse(JSONReader.HOST+"weather").buildUpon().appendQueryParameter("q",searchTextView.getText().toString()).build();

        new Thread(new Runnable() {
            public void run() {
                try {
                    String json = JSONReader.readJsonFromUri(builtUri);
                    Log.e("json",json);
                    if (json != null) {
                        if (json.charAt(0) != '[') {
                            final JSONObject jsonObj = new JSONObject(json);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    addLocationToList(jsonObj);
                                }
                            });
                        } else {
                            final JSONArray jArray = new JSONArray(json);
                            for(int i=0; i<jArray.length(); i++)
                            {
                                final JSONObject jsonObj = jArray.getJSONObject(i);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        addLocationToList(jsonObj);
                                    }
                                });
                            }
                        }
                    }
                    runOnUiThread(
                            new Runnable() {
                                public void run() {
                                    mProgress.setVisibility(View.GONE);
                                }}
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void addLocationToList(JSONObject jsonObj) {
        mListCustom.clear();
        try {
            mListCustom.add(new Location(jsonObj.getString("name"),
                                         jsonObj.getJSONObject("sys").getString("country"),
                                         jsonObj.getInt("id")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mCustomArrayAdapter.notifyDataSetChanged();
    }
}

