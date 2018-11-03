package com.nads.rideem;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class LodgingFragment extends Fragment {
    private static final Double Lats = 1.0;
    private static final Double Logs = 1.0;
    public static final String LATS = "fd";
    public static final String LOGS = "yt";
    public static final String ARRAY_LIST1 = "whatis";
    private static final String LOG_TAG ="df";
    BroadcastReceiver broadcastReceiver;
    RecyclerView lodgerecycler;
    static ArrayList<objforRest> arrayList3 = new ArrayList<>();
    LodgingAdapter lodgingAdapter;

    public LodgingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final double lats = args.getDouble(LATS,Lats);
        final double logs = args.getDouble(LOGS,Logs);
        final Bundle bundle = new Bundle();
        bundle.putDouble(LodgingAdapter.LATS,lats);
        bundle.putDouble(LodgingAdapter.LOGS,logs);
        lodgerecycler = (RecyclerView) inflater.inflate(
                R.layout.fragment_restaurant2, container, false);
        lodgerecycler.setHasFixedSize(true);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
                if (resultCode == RESULT_OK) {
                    arrayList3 = intent.getParcelableArrayListExtra(ARRAY_LIST1);
                    refreshrecyclerinlodg(arrayList3,bundle,LodgingFragment.this.getContext());
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lodgerecycler.setLayoutManager(layoutManager);
        return lodgerecycler;
    }
      public void refreshrecyclerinlodg(ArrayList<objforRest> arrayList,Bundle bundle,Context context){
          lodgingAdapter = new LodgingAdapter(bundle, context,arrayList);
          lodgerecycler.setAdapter(lodgingAdapter);
          lodgingAdapter.notifyItemRangeChanged(0,arrayList3.size(),arrayList3);

      }
    @Override
    public void onStart(){
        super.onStart();
        IntentFilter filter = new IntentFilter(IntentserForLodg.MACTION);
        LocalBroadcastManager.getInstance(LodgingFragment.this.getContext()).registerReceiver(broadcastReceiver, filter);
        Bundle args = getArguments();
        double lats = args.getDouble(LATS, Lats);
        double logs = args.getDouble(LOGS, Logs);
        Intent intent = new Intent(LodgingFragment.this.getContext(),IntentserForLodg.class);
        intent.putExtra(IntentserForLodg.KATS,lats);
        intent.putExtra(IntentserForLodg.KONS,logs);
        getActivity().startService(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(IntentserForLodg.MACTION);
        LocalBroadcastManager.getInstance(LodgingFragment.this.getContext()).registerReceiver(broadcastReceiver, filter);
    }
    @Override
    public void onPause() {
        super.onPause();
        arrayList3.clear();
        LodgingAdapter.arrayList4.clear();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(LodgingFragment.this.getContext()).unregisterReceiver(broadcastReceiver);
        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LodgingAdapter.arrayList4.clear();
        arrayList3.clear();
    }
}
