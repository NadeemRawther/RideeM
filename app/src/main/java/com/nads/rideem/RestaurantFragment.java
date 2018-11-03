package com.nads.rideem;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.internal.gmsg.HttpClient;
import com.google.android.gms.tagmanager.Container;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.Inflater;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.internal.zzahn.runOnUiThread;
import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
public class RestaurantFragment extends Fragment {
    private static final Double Lats = 1.0;
    private static final Double Logs = 1.0;
    public static final String LATS = "fd";
    public static final String LOGS = "yt";
    private static final String LOG_TAG = "fw";
    private static final String ARG_SECTION_NUMBER = "section_number";
    static ArrayList<objforRest> arrayList1 = new ArrayList<objforRest>();
    public static final String REQUEST_TAG = "VolleyBlockingRequestActivity";
    public static final String ARRAY_LIST = "igotit";
    private RequestQueue mQueue;
    RecyclerView videoRecycler ;
    LinearLayoutManager layoutManager;
    BroadcastReceiver testReceiver;
    Bundle bundle;
    RestaurantAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
      Bundle args = getArguments();
      double lats = args.getDouble(LATS, Lats);
      double logs = args.getDouble(LOGS, Logs);
      final Bundle bundle = new Bundle();
       bundle.putDouble(RestaurantAdapter.LATS, lats);
       bundle.putDouble(RestaurantAdapter.LOGS, logs);
        videoRecycler = (RecyclerView) inflater.inflate(
                R.layout.fragment_restaurant2, container, false);
        testReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
                if (resultCode == RESULT_OK) {
                    arrayList1 = intent.getParcelableArrayListExtra(ARRAY_LIST);
                    refreshrecycler(arrayList1,bundle,RestaurantFragment.this.getContext());
                }
            }
        };
        layoutManager = new LinearLayoutManager(getActivity());
        videoRecycler.setLayoutManager(layoutManager);
        return videoRecycler;
            }
    @Override
    public void onStart(){
        super.onStart();
              IntentFilter filter = new IntentFilter(IntentserForRest.ACTION);
             LocalBroadcastManager.getInstance(RestaurantFragment.this.getContext()).registerReceiver(testReceiver, filter);
              Bundle args = getArguments();
              double lats = args.getDouble(LATS, Lats);
              double logs = args.getDouble(LOGS, Logs);
              Intent intent = new Intent(RestaurantFragment.this.getContext(),IntentserForRest.class);
              intent.putExtra(IntentserForRest.PATS,lats);
              intent.putExtra(IntentserForRest.PONS,logs);
              getActivity().startService(intent);
          }
    @Override
    public void onResume() {
        super.onResume();
       IntentFilter filter = new IntentFilter(IntentserForRest.ACTION);
        LocalBroadcastManager.getInstance(RestaurantFragment.this.getContext()).registerReceiver(testReceiver, filter);
    }
    @Override
    public void onPause() {
        super.onPause();
        arrayList1.clear();
        RestaurantAdapter.arrayList2.clear();
        // Unregister the listener when the application is paused
       LocalBroadcastManager.getInstance(RestaurantFragment.this.getContext()).unregisterReceiver(testReceiver);
        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }

public void refreshrecycler(ArrayList<objforRest> arrayList,Bundle bundle,Context context){
    adapter = new RestaurantAdapter(bundle, RestaurantFragment.this.getContext(),arrayList1);
    videoRecycler.setAdapter(adapter);
    adapter.notifyItemRangeChanged(0,arrayList1.size(),arrayList1);

}
    @Override
    public void onDestroy() {
        super.onDestroy();
        arrayList1.clear();
        RestaurantAdapter.arrayList2.clear();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }
}