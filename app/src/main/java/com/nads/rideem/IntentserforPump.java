package com.nads.rideem;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 2/19/2018.
 */

public class IntentserforPump extends IntentService {
    public static final String TACTION = "com.nads.rideem.IntentserForPump";
    public static final String TATS = "cpik";
    public static final String TONS = "theto";
    Double lst = 1.5;
    Double lng = 1.8;
    static ArrayList<objforRest> arrayList9 = new ArrayList<>();
    // Must create a default constructor
    public IntentserforPump() {
        // Used to name the worker thread, important only for debugging.
        super("test-service3");
    }
    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }
    @Override
    protected void onHandleIntent(final Intent intent) {
        // This describes what will happen when service is triggered

        Double lats = intent.getDoubleExtra(TATS,lst);
        Double logs = intent.getDoubleExtra(TONS,lng);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        stringBuilder.append("&location=" + lats + ",");
        stringBuilder.append(logs + "&radius=1000&type=gas_station&key=AIzaSyA8gCiIV2ZYb2jv2Vs48aZeRcoo8YYCZRA");
        RequestQueue requestQueues = Volley.newRequestQueue(IntentserforPump.this.getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, stringBuilder.toString()
                , null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonObject = response.getJSONArray("results");
                            for (int i = 0; i <= response.length(); i++) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(i);
                                String name = jsonObject1.get("name").toString();
                                String place = jsonObject1.get("formatted_address").toString();
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("geometry");
                                JSONObject jsonArray = jsonObject2.getJSONObject("location");
                                Double latsi = jsonArray.getDouble("lat");
                                Double longis = jsonArray.getDouble("lng");
                                objforRest objforRest = new objforRest(latsi,longis,name, place);
                                arrayList9.add(objforRest);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueues.add(objectRequest);
        requestQueues.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JSONObject>() {
            @Override
            public void onRequestFinished(Request<JSONObject> request) {

                Intent broadcast = new Intent(TACTION);
                broadcast.putExtra("resultCode", Activity.RESULT_OK);
                broadcast.putParcelableArrayListExtra(Pump.ARRAY_LIST2,arrayList9);
                LocalBroadcastManager.getInstance(IntentserforPump.this).sendBroadcast(broadcast);
            }
        });
    }



}
