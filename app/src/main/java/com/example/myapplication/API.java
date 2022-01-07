package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class API extends AppCompatActivity  {
    static LatLng start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = new LatLng(40.82467, 29.92163);
        end = new LatLng(40.82914552445229, 29.911804904992778);
        new DurationTask(this).execute();

    }
    @NonNull
    public static JSONObject getJSONObjectFromURL(String urlString, JSONObject body) throws IOException, JSONException {
        HttpURLConnection urlConnection;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setRequestProperty("Authorization","5b3ce3597851110001cf62480922f657e9094169a07fa7a62f09d545");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        Log.i("JSONN", body.toString());
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
        os.writeBytes(body.toString());

        os.flush();
        os.close();

        Log.i("STATUSS", String.valueOf(urlConnection.getResponseCode()));
        Log.i("MSGG" , urlConnection.getResponseMessage());


        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        String jsonString = sb.toString();
        Log.i("RSPP" , jsonString);

        urlConnection.disconnect();

        return new JSONObject(jsonString);
    }


    private static class DurationTask extends AsyncTask<Void,Void,String>{

        private WeakReference<API> activityReference;
        private String text;

        String duration;

        DurationTask(API context) {
            activityReference = new WeakReference<>(context);
        }



        @Override
        protected String doInBackground(Void... args) {
            try {
                JSONObject jsonParam = new JSONObject();
                JSONArray coordinates = new JSONArray();
                JSONArray star = new JSONArray();
                star.put(start.getLongitude());
                star.put(start.getLatitude());
                JSONArray en = new JSONArray();
                en.put(end.getLongitude());
                en.put(end.getLatitude());
                coordinates.put(star);
                coordinates.put(en);
                jsonParam.put("locations", coordinates);
                jsonParam.put("metrics", new JSONArray().put("distance").put("duration"));
                jsonParam.put("units", "km");

                JSONObject response = getJSONObjectFromURL("https://api.openrouteservice.org/v2/matrix/driving-car", jsonParam);
                Object durations =response.getJSONArray("durations").getJSONArray(1).get(0);
                Object distance =response.getJSONArray("distances").getJSONArray(1).get(0);

                System.out.println(distance);



            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            return duration;
        }


    }






}