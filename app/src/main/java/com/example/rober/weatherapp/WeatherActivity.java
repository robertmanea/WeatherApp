package com.example.rober.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by rober on 05-Apr-17.
 */

public class WeatherActivity extends AppCompatActivity {

    private ArrayList<Location> mListCustom;
    private SavedLocationArrayAdapter mCustomArrayAdapter;
    public ListView searchListView;
    private LocationsHelper mHelper;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mProgress.setIndeterminate(true);
        mProgress.setVisibility(View.VISIBLE);

        Bundle weatherData = getIntent().getExtras();
        String citySearchMessage = weatherData.getString("name");
        int cityId = weatherData.getInt("id");
        String cityCountry = weatherData.getString("country");

        final TextView cityText = (TextView) findViewById(R.id.cityText);
        cityText.setText(citySearchMessage);
        cityText.append(", ");
        cityText.append(cityCountry);
        // WeatherInformations.Info(citySearchMessage);
        Info(cityId);

    }

    protected void Info(int cityId) {

        final Uri builtUri = Uri.parse(JSONReader.HOST + "weather").buildUpon().appendQueryParameter("id", String.valueOf(cityId)).build();

        new Thread(new Runnable() {
            public void run() {
                try {
                    String json = JSONReader.readJsonFromUri(builtUri);
                    Log.e("json",json);
                    if (json != null) {
                        final JSONObject jsonObj = new JSONObject(json);

                        runOnUiThread(new Runnable() {
                            public void run() {
                                extractInfo(jsonObj);
                            }
                        });
                    }
                    runOnUiThread(
                            new Runnable() {
                                public void run() {
                                    mProgress.setVisibility(View.GONE);
                                }}
                    );
                } catch (IOException e) {
                    runOnUiThread(
                            new Runnable() {
                                public void run() {
                                    mProgress.setVisibility(View.GONE);
                                }}
                    );
                    e.printStackTrace();
                } catch (JSONException e) {
                    runOnUiThread(
                            new Runnable() {
                                public void run() {
                                    mProgress.setVisibility(View.GONE);
                                }}
                    );
                    e.printStackTrace();
                }
            }
        }).start();
    }


    protected void extractInfo(JSONObject jsonObj)
    {
        try {

            JSONArray jsonArray = jsonObj.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);

            final TextView descriptionData = (TextView) findViewById(R.id.descriptionData);
            descriptionData.setText(jsonWeather.getString("description"));

            final TextView tempData = (TextView) findViewById(R.id.tempText);
            tempData.setText(jsonObj.getJSONObject("main").getString("temp") + " F");


            final TextView windData = (TextView) findViewById(R.id.windData);
            windData.setText(jsonObj.getJSONObject("wind").getString("speed") + " km/h");

            final TextView humidityData = (TextView) findViewById(R.id.humidityData);
            humidityData.setText(jsonObj.getJSONObject("main").getString("humidity") + " %");

            final TextView pressureData = (TextView) findViewById(R.id.pressureData);
            pressureData.setText(jsonObj.getJSONObject("main").getString("pressure") + " hPa");

            final TextView visibilityData = (TextView) findViewById(R.id.visibilityData);
            visibilityData.setText(jsonObj.getString("visibility") + " kilometers");


            if(jsonWeather.getInt("id") == 200 || jsonWeather.getInt("id") == 201 || jsonWeather.getInt("id") == 202 || jsonWeather.getInt("id") == 210 || jsonWeather.getInt("id") == 211 || jsonWeather.getInt("id") == 212 || jsonWeather.getInt("id") == 221 || jsonWeather.getInt("id") == 230 || jsonWeather.getInt("id") == 231 || jsonWeather.getInt("id") == 232 )
            {
                ImageView thunder = (ImageView) findViewById(R.id.iconWeather);
                thunder.setImageResource(R.mipmap.thunder);
            }


            if(jsonWeather.getInt("id") == 300 || jsonWeather.getInt("id") == 301 || jsonWeather.getInt("id") == 302 || jsonWeather.getInt("id") == 310 || jsonWeather.getInt("id") == 311 || jsonWeather.getInt("id") == 312 || jsonWeather.getInt("id") == 313 || jsonWeather.getInt("id") == 314 || jsonWeather.getInt("id") == 321 ||
                    jsonWeather.getInt("id") == 500 || jsonWeather.getInt("id") == 501 || jsonWeather.getInt("id") == 502 || jsonWeather.getInt("id") == 503 || jsonWeather.getInt("id") == 504 || jsonWeather.getInt("id") == 511 || jsonWeather.getInt("id") == 520 || jsonWeather.getInt("id") == 521 || jsonWeather.getInt("id") == 522 || jsonWeather.getInt("id") == 531 )
            {
                ImageView rain = (ImageView) findViewById(R.id.iconWeather);
                rain.setImageResource(R.mipmap.rain);
            }

            if(jsonWeather.getInt("id") == 600 || jsonWeather.getInt("id") == 601 || jsonWeather.getInt("id") == 602 ||  jsonWeather.getInt("id") == 611 || jsonWeather.getInt("id") == 612 || jsonWeather.getInt("id") == 315 || jsonWeather.getInt("id") == 316 || jsonWeather.getInt("id") == 620 || jsonWeather.getInt("id") == 621 || jsonWeather.getInt("id") == 622 )
            {
                ImageView snow = (ImageView) findViewById(R.id.iconWeather);
                snow.setImageResource(R.mipmap.snow);
            }


            if(jsonWeather.getInt("id") == 701 || jsonWeather.getInt("id") == 711 || jsonWeather.getInt("id") == 721 || jsonWeather.getInt("id") == 731 || jsonWeather.getInt("id") == 741 || jsonWeather.getInt("id") == 751 || jsonWeather.getInt("id") == 761 || jsonWeather.getInt("id") == 762 || jsonWeather.getInt("id") == 771 ||
                    jsonWeather.getInt("id") == 801 || jsonWeather.getInt("id") == 802 || jsonWeather.getInt("id") == 803 || jsonWeather.getInt("id") == 804)
            {
                ImageView cloud = (ImageView) findViewById(R.id.iconWeather);
                cloud.setImageResource(R.mipmap.cloud);
            }

            if(jsonWeather.getInt("id") == 951 || jsonWeather.getInt("id") == 952 || jsonWeather.getInt("id") == 953 ||  jsonWeather.getInt("id") == 954 || jsonWeather.getInt("id") == 955 || jsonWeather.getInt("id") == 956 || jsonWeather.getInt("id") == 957 || jsonWeather.getInt("id") == 958 || jsonWeather.getInt("id") == 959 || jsonWeather.getInt("id") == 960 || jsonWeather.getInt("id") == 961 || jsonWeather.getInt("id") == 962)
            {
                ImageView wind = (ImageView) findViewById(R.id.iconWeather);
                wind.setImageResource(R.mipmap.wind);
            }


            if(jsonWeather.getInt("id") == 800)
            {
                ImageView sun = (ImageView) findViewById(R.id.iconWeather);
                sun.setImageResource(R.mipmap.sun);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}



  /*
  ////////////// bun bun bun
  protected void Info(String cityName) {

        final Uri builtUri = Uri.parse(JSONReader.HOST + "weather").buildUpon().appendQueryParameter("q", cityName).build();
        //TODO PROGRESSBAR
        new Thread(new Runnable() {
            public void run() {
                try {
                    String json = JSONReader.readJsonFromUri(builtUri);

                    Log.e("json", json);

                    final JSONObject jsonObj = new JSONObject(json);

                    final TextView humidityText = (TextView) findViewById(R.id.humidityText);
                    humidityText.setText(jsonObj.getJSONObject("main").getString("humidity"));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }  */

    /* protected void extractInfo(JSONObject jsonObj)
    {
        try {

            JSONObject sysObj = jsonObj.getJSONObject("sys");
            int sunrise = sysObj.getInt("sunrise");

            final TextView sunriseText = (TextView) findViewById(R.id.sunriseText);
            sunriseText.setText(sunrise);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }  */

//  mListCustom.add(new Location(jsonObj.getString("name"),
//          jsonObj.getJSONObject("sys").getString("country"),
//         jsonObj.getInt("id")));


