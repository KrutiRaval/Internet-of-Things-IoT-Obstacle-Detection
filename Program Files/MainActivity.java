package com.starc.dev.obtacledetection;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button current_location;
    private int responseCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current_location = (Button) findViewById(R.id.current_location);

        current_location.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.current_location) {
            GetLatLong getLatLong = new GetLatLong();
            getLatLong.execute();
        }
    }

    private class GetLatLong extends AsyncTask<Object, Void, String> {
        String responseData;

        @Override
        protected String doInBackground(Object... objects) {
            try {
                URL url = new URL("http://myandroidhost.16mb.com/api/python/send2.php");
                HttpURLConnection cn = (HttpURLConnection) url.openConnection();
                cn.connect();

                responseCode = cn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = cn.getInputStream();
                    Reader reader = new InputStreamReader(inputStream);
                    int contentLength = cn.getContentLength();
                    char[] charArray = new char[contentLength];
                    reader.read(charArray);
                    responseData = new String(charArray);
                    Log.e("response", "" + responseData);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseData;
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String latitude = jsonObject.getString("latitude");
                String longitude = jsonObject.getString("longitude");

                String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + "(" + "Here" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
