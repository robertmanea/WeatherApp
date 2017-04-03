package com.example.rober.weatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import co.justnow.justnow.activities.ActivityController;
import co.justnow.justnow.core.data.Constants;
import co.justnow.justnow.core.data.Data;
import co.justnow.justnow.core.utils.MainActivityController;

public class RequestHandler {
    private static final String baseUrl = "http://api.justnow.co/api/v1/";

    static String paginatorURL;
    private static RequestHandler singleton;

    public static RequestHandler getInstance() {
        if (singleton == null) {
            singleton = new RequestHandler();
        }
        return singleton;

    }

    public static String Get(String path, HashMap<String, String> queryParameters, HashMap<String, String> filters) {
        String baseURL = baseUrl + path + "?";
        int i = 0;
        try {

            for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                i++;
                baseURL += entry.getKey() + "=" + entry.getValue();
                if (i != queryParameters.size()) {
                    baseURL += "&";
                }
            }
            if (filters != null) {
                i = 0;
                baseURL += "&";
                for (Map.Entry<String, String> entry : filters.entrySet()) {
                    i++;
                    String value = "";
                    try {
                        value = URLEncoder.encode(entry.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        value = entry.getValue();
                    }
                    baseURL += entry.getKey() + "=" + value;
                    if (i != filters.size()) {
                        baseURL += "&";
                    }
                }
            }
        } catch (Exception e) {
            MainActivityController.stopThread();
        }
        return baseURL;
    }

    static JSONObject performPostCall(final String path,
                                      final HashMap<String, String> postDataParams,
                                      final Activity context, boolean isPutRequest) {

        final String[] response = {""};
        final JSONObject[] json = {null};
        URL url;

        try {
            url = new URL(baseUrl + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            if (isPutRequest) {
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Authorization", "Bearer " + Data.user.getRequest_token());
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            InputStream readStream = null;

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response[0] += line;
                }
            } else {
                String line;
                if (conn.getErrorStream() != null)
                    readStream = conn.getErrorStream();
                else if (conn.getInputStream() != null)
                    readStream = conn.getInputStream();
                if (readStream != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(readStream));
                    while ((line = br.readLine()) != null) {
                        response[0] += line;
                    }

                }
            }
            json[0] = new JSONObject(response[0]);
            String status = "";
            status = json[0].getString("status");
            if (!path.contains("notifications"))
                if (!status.equals("ok")) {
                    if (status.equals("error")) {
                        showMessage(json[0].getString("message"), context);
                    } else {
                        showMessage(Constants.somethingWrong, context);
                    }
                    json[0] = null;
                }


        } catch (Exception e) {
            json[0] = null;
            String message = "";
            if (context != null) {
                if (ActivityController.isNetworkAvailable(context))
                    message = Constants.somethingWrong;
                else
                    message = Constants.NO_INTERNET;
                showMessage(message, context);
            }
        }

        return json[0];
    }

    private static void showMessage(final String message, final Activity activity) {
        //show error message
        activity.runOnUiThread(new Runnable() {

            public void run() {
                ActivityController.createAlertDialog(message,
                        activity, false);

            }
        });
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    static ProgressDialog setProgressDialog(final Activity a) {
        //set progress bar when loading something
        final ProgressDialog[] mProgressDialog = new ProgressDialog[1];

        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog[0] = new ProgressDialog(a);
                mProgressDialog[0].setIndeterminate(true);
                mProgressDialog[0].setCancelable(false);
                mProgressDialog[0].setMessage("Loading..");
                mProgressDialog[0].setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog[0].show();
            }
        });

        return mProgressDialog[0];
    }

    public static ProgressBar setProgressBar(final Activity a, final int idLayout) {
        //set progress bar when loading something
        final ProgressBar progressBar = new ProgressBar(a, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.VISIBLE);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(110, 110);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (a.findViewById(idLayout) != null)
                    ((RelativeLayout) a.findViewById(idLayout)).addView(progressBar, params);
            }
        });
        return progressBar;
    }
}
