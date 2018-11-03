package com.nads.rideem;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class mapride extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private InterstitialAd mInterstitialAd;
    private static final String API_KEY = "AIzaSyBEN8AHt9wIjZkknOEfOST3rzpvmwkiPp4";
    private static final String API_KEY2 = " AIzaSyA8gCiIV2ZYb2jv2Vs48aZeRcoo8YYCZRA";
    private static final String LOG_TAG = "d";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 4;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GeoDataClient mGeoDataClient;
    GoogleApiClient mGoogleApiClient;
    PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private static final String KEY_LOCATION = "location";
    private boolean mLocationPermissionGranted;

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.mapride);
        if(isOnline()!=true){
            Toast toast = Toast.makeText(this, "There's no network connection Please connect and refresh from menu ", Toast.LENGTH_LONG);
            toast.show();

        }
        if (ActivityCompat.checkSelfPermission(mapride.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mapride.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mapride.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2991702481825090~9700215450");
        // AdView adView = new AdView(this);
        AdView adView = (AdView) findViewById(R.id.adView);
        //adView.setAdSize(AdSize.BANNER);
        // adView.setAdUnitId("ca-app-pub-2991702481825090/1104313900");
        AdRequest request = new AdRequest.Builder()
               // .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                //.addTestDevice("658F3783B85CC8CA35A2B54A49AA11D9")  // An example device ID
                .build();
        adView.loadAd(request);

        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText1 = (EditText) findViewById(R.id.editText2);
        FirebaseUser user = mAuth.getCurrentUser();
        String name = user.getDisplayName();
        final TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(name);
        final TextView textView = (TextView) findViewById(R.id.textView);
        final TextView textView1 = (TextView) findViewById(R.id.textView4);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        CardView cardView = (CardView)findViewById(R.id.cardView2);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CurrentPlace currentPlace = new CurrentPlace();
                currentPlace.execute();
            }
        });
        CardView cardView1 = (CardView)findViewById(R.id.cardView3);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PumpandWorkShops pumpandWorkShops = new PumpandWorkShops();
                      pumpandWorkShops.execute();
            }
        });
        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int PLACE_PICKER_REQUEST = 2;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(mapride.this), PLACE_PICKER_REQUEST);
                    return true;
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int PLACE_PICKER_REQUEST = 1;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(mapride.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView == null && textView1 == null) {
                    Toast toast = Toast.makeText(mapride.this, "please enter the starting and destination", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
                    downloadFilesTask.execute();

                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //if (mMap != null) {
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        //}
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                EditText editText = (EditText) findViewById(R.id.editText);
                editText.setText(place.getAddress());
            }
        }
        else if (requestCode == 2){
        if (resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            EditText editText = (EditText) findViewById(R.id.editText2);
            editText.setText(place.getAddress());
        }
    }
    }

    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    protected void createLocationRequest() {
        final LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(mapride.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                Log.i(LOG_TAG, "Refresh menu item selected");
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseAuth.getInstance().signOut();
                                Toast toast = Toast.makeText(mapride.this, "signed out", Toast.LENGTH_LONG);
                                toast.show();
                                Intent intent = new Intent(mapride.this, ridemap.class);
                                mapride.this.startActivity(intent);
                                mapride.this.finish();
                            }
                        });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Please signout and click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            final EditText editText = (EditText) findViewById(R.id.editText);
            final EditText editText1 = (EditText) findViewById(R.id.editText2);
            final TextView textView7 = (TextView) findViewById(R.id.textView7);
            final TextView textView8 = (TextView) findViewById(R.id.textView8);

            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("https://maps.googleapis.com/maps/api/distancematrix/json?");
                stringBuilder.append("origins=" + URLEncoder.encode(editText.getText().toString(), "utf8"));
                stringBuilder.append("&destinations=" + URLEncoder.encode(editText1.getText().toString(), "utf8"));
                stringBuilder.append("&key=" + API_KEY);
                final RequestQueue requestQueue = Volley.newRequestQueue(mapride.this);
                final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, stringBuilder.toString(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject formArray = response.getJSONArray("rows").getJSONObject(0);
                                    JSONObject jsonArray = formArray.getJSONArray("elements").getJSONObject(0);
                                    JSONObject jsonObject = jsonArray.getJSONObject("duration");
                                    JSONObject jsonObject1 = jsonArray.getJSONObject("distance");

                                    textView8.setText(jsonObject.getString("text"));
                                    textView7.setText(jsonObject1.getString("text"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class CurrentPlace extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {



            try {
                if (ActivityCompat.checkSelfPermission(mapride.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapride.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mapride.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSIONS_REQUEST_CODE);
                    return null;
                }

                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location==null){
                            Toast toast = Toast.makeText(mapride.this,"its null",Toast.LENGTH_LONG);
                            toast.show();
                        }else {
                        double lats = location.getLatitude();
                        double lags = location.getLongitude();
                        Geocoder geocoder = new Geocoder(mapride.this, Locale.getDefault());
                        List<Address> addressList = null;
                        try {
                            addressList = geocoder.getFromLocation(lats, lags, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String str = addressList.get(0).getLocality();
                        String srt = addressList.get(0).getSubLocality();
                        TextView textView4 = (TextView)findViewById(R.id.textView4);
                        textView4.setText(str);
                        EditText editText = (EditText)findViewById(R.id.editText);
                        editText.setText(textView4.getText());

                        Intent intent = new Intent(mapride.this,restaur.class);
                        intent.putExtra(restaur.LATS,lats);
                        intent.putExtra(restaur.LOG,lags);
                        mapride.this.startActivity(intent);
                         }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG,"nadeemsa is gone");
                    }
                });

            }
            catch (Exception e){
                e.printStackTrace();
            }

        return null;
    }
}
    private class PumpandWorkShops extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {



            try {
                if (ActivityCompat.checkSelfPermission(mapride.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapride.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mapride.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSIONS_REQUEST_CODE);
                    return null;
                }

                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        double lats = location.getLatitude();
                        double lags = location.getLongitude();
                        Geocoder geocoder = new Geocoder(mapride.this, Locale.getDefault());
                        List<Address> addressList = null;
                        try {
                            addressList = geocoder.getFromLocation(lats, lags, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        String str = addressList.get(0).getLocality();
                        String srt = addressList.get(0).getSubLocality();
                        TextView textView4 = (TextView)findViewById(R.id.textView4);
                        textView4.setText(str);
                        EditText editText = (EditText)findViewById(R.id.editText);
                        editText.setText(textView4.getText());
                        Intent intent = new Intent(mapride.this,PumpAndWorkshopsPager.class);
                        intent.putExtra(PumpAndWorkshopsPager.LATS,lats);
                        intent.putExtra(PumpAndWorkshopsPager.LOG,lags);
                        mapride.this.startActivity(intent);
                    }
                });

            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
}