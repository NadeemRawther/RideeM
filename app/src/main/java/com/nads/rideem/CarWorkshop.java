package com.nads.rideem;


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

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarWorkshop extends Fragment {
    private static final Double Lats = 1.0;
    private static final Double Logs = 1.0;
    public static final String LATS = "gh";
    public static final String LOGS = "fd";
    public static final String ARRAY_LIST2 = "inworkshop";
    RecyclerView carrecyclerview;
    BroadcastReceiver broadcastReceiver2;
    WorkshopAdapter workshopAdapter;
    static ArrayList<objforRest> arrayList13= new ArrayList<>();
    public CarWorkshop() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final double lats = args.getDouble(LATS,Lats);
        final double logs = args.getDouble(LOGS,Logs);
      final   Bundle bundle = new Bundle();
        bundle.putDouble(LodgingAdapter.LATS,lats);
        bundle.putDouble(LodgingAdapter.LOGS,logs);
        carrecyclerview = (RecyclerView) inflater.inflate(
                R.layout.fragment_restaurant2, container, false);
        carrecyclerview.setHasFixedSize(true);
        broadcastReceiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
                if (resultCode == RESULT_OK) {
                    arrayList13 = intent.getParcelableArrayListExtra(ARRAY_LIST2);
                    refreshrecyclerincar(arrayList13,bundle,CarWorkshop.this.getContext());
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        carrecyclerview.setLayoutManager(layoutManager);
        return carrecyclerview;
    }
    public void refreshrecyclerincar(ArrayList<objforRest> arrayList, Bundle bundle,Context context){
        workshopAdapter = new WorkshopAdapter(bundle, context,arrayList);
        carrecyclerview.setAdapter(workshopAdapter);
        workshopAdapter.notifyItemRangeChanged(0,arrayList13.size(),arrayList13);
    }
    @Override
    public void onStart(){
        super.onStart();
        IntentFilter filter = new IntentFilter(IntentForWorkshop.QACTION);
        LocalBroadcastManager.getInstance(CarWorkshop.this.getContext()).registerReceiver(broadcastReceiver2, filter);
        Bundle args = getArguments();
        double lats = args.getDouble(LATS, Lats);
        double logs = args.getDouble(LOGS, Logs);
        Intent intent = new Intent(CarWorkshop.this.getContext(),IntentForWorkshop.class);
        intent.putExtra(IntentForWorkshop.QATS,lats);
        intent.putExtra(IntentForWorkshop.QONS,logs);
        getActivity().startService(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(IntentForWorkshop.QACTION);
        LocalBroadcastManager.getInstance(CarWorkshop.this.getContext()).registerReceiver(broadcastReceiver2, filter);
    }
    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        arrayList13.clear();
        WorkshopAdapter.arraylist14.clear();
        LocalBroadcastManager.getInstance(CarWorkshop.this.getContext()).unregisterReceiver(broadcastReceiver2);
        // or `unregisterReceiver(testReceiver)` for a normal broadcast
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        WorkshopAdapter.arraylist14.clear();
        arrayList13.clear();
    }


}
