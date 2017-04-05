package com.example.rober.weatherapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

public class JSONReader {

    final static String HOST="http://api.openweathermap.org/data/2.5/";
    final static String APIKEY="a90002748eb88ba3fda7a0a8376667ed";
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readJsonFromUri(Uri uri) throws IOException, JSONException {
        uri = Uri.parse(uri.toString()).buildUpon().appendQueryParameter("APPID",APIKEY).build();
        InputStream is = new URL(uri.toString()).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return jsonText;
        } finally {
            is.close();
        }
    }

}
