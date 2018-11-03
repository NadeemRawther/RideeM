package com.nads.rideem;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pump extends Fragment {
    private static final Double Lats = 1.0;
    private static final Double Logs = 1.0;
    public static final String LATS = "gh";
    public static final String LOGS = "fd";
    RecyclerView pumprecycler;
    ProgressDialog progressDialog;
    BroadcastReceiver broadcastReceiver1;
    PumpAdapter pumpAdapter;
    static ArrayList<objforRest> arrayList10 = new ArrayList<>();
    public static final String ARRAY_LIST2 = "inpump";
    public Pump() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final double lats = args.getDouble(LATS,Lats);
        final double logs = args.getDouble(LOGS,Logs);
        final Bundle bundle = new Bundle();
        bundle.putDouble(PumpAdapter.LATS,lats);
        bundle.putDouble(PumpAdapter.LOGS,logs);

        pumprecycler = (RecyclerView) inflater.inflate(
        R.layout.fragment_restaurant2, container, false);
        pumprecycler.setHasFixedSize(true);
        broadcastReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
                if (resultCode == RESULT_OK) {
                    arrayList10 = intent.getParcelableArrayListExtra(ARRAY_LIST2);
                    refreshrecyclerinpump(arrayList10,bundle,Pump.this.getContext());
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        pumprecycler.setLayoutManager(layoutManager);
        return pumprecycler;
    }
    public  void  refreshrecyclerinpump(ArrayList<objforRest> arrayList,Bundle bundle,Context context){
        pumpAdapter = new PumpAdapter(bundle,context,arrayList);
        pumprecycler.setAdapter(pumpAdapter);
        pumpAdapter.notifyItemRangeChanged(0,arrayList10.size(),arrayList10);

    }
    @Override
    public void onStart(){
        super.onStart();


        IntentFilter filter = new IntentFilter(IntentserforPump.TACTION);
        LocalBroadcastManager.getInstance(Pump.this.getContext()).registerReceiver(broadcastReceiver1, filter);
        Bundle args = getArguments();
        double lats = args.getDouble(LATS, Lats);
        double logs = args.getDouble(LOGS, Logs);
        Intent intent = new Intent(Pump.this.getContext(),IntentserforPump.class);
        intent.putExtra(IntentserforPump.TATS,lats);
        intent.putExtra(IntentserforPump.TONS,logs);
        getActivity().startService(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(IntentserforPump.TACTION);
        LocalBroadcastManager.getInstance(Pump.this.getContext()).registerReceiver(broadcastReceiver1, filter);
    }
    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        arrayList10.clear();
        PumpAdapter.arrayList11.clear();
        LocalBroadcastManager.getInstance(Pump.this.getContext()).unregisterReceiver(broadcastReceiver1);

        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        PumpAdapter.arrayList11.clear();
        arrayList10.clear();
    }
}
