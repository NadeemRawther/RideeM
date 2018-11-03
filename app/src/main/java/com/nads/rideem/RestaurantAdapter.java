package com.nads.rideem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.android.gms.internal.zzahn.runOnUiThread;
import static com.nads.rideem.RestaurantFragment.REQUEST_TAG;
import static com.nads.rideem.RestaurantFragment.arrayList1;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    public static final String LATS = "poda";
    static ArrayList<objforRest> arrayList2 = new ArrayList<>();
    public static final String LOGS = "chikku";
    private CardView cardView;
    private Listener listener;
    private static final String LOG_TAG = "d";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 4;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final Double Lats = 1.0;
    private static final Double Logs = 1.0;
    private Bundle fragmentBundle;
    private Context context;
    public static final String REQUEST_TAG = "VolleyBlockingRequestActivity";
    private RequestQueue mQueue;

    public RestaurantAdapter(Bundle bundle, final Context context,ArrayList<objforRest> arrayList)

    {
        this.fragmentBundle = bundle;
        this.context = context;
        this.arrayList2 = arrayList;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public ViewHolder(View view) {

            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);

        }
    }
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview1, parent, false);
        return new ViewHolder(cv);
    }
    public interface Listener {
        public void onClick(int position, String Ways);
    }

    @Override
     public void onBindViewHolder(RestaurantAdapter.ViewHolder holder, final int position) {




         final CardView cardView = holder.cardView;
         final Context context = cardView.getContext();
         final TextView textView = (TextView) cardView.findViewById(R.id.textView9);
         final TextView textView1 = (TextView)cardView.findViewById(R.id.textView10);
         cardView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String place = arrayList2.get(position).getGHY();
                 String name = arrayList2.get(position).getHyt();
                 Double latsi = arrayList2.get(position).getLats();
                 Double lngi = arrayList2.get(position).getLngs();
                 Uri gmmIntentUri = Uri.parse("geo:"+latsi+","+lngi+"?q="+Uri.encode(name + place));
                 Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                 mapIntent.setPackage("com.google.android.apps.maps");
                 if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                    arrayList1.clear();
                    LodgingAdapter.arrayList4.clear();
                 }
             }
         });

        if(arrayList2.size() !=0){

             String place = arrayList2.get(position).getGHY();
             String name = arrayList2.get(position).getHyt();
             textView.setText(name);
             textView1.setText(place);
        }
    }
    @Override
    public int getItemCount() {
     int siz =  arrayList2.size();
        return siz;
    }
}