package com.nads.rideem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.ViewHolder> {

    public static final String LATS ="fds" ;
    public static final String LOGS = "dsf";
    private CardView cardView;
    private Listener listener;
    private static final String LOG_TAG = "d";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 4;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Context context;
    private static final Double Lats = 1.0;
    private static final Double Logs = 1.0;
    private Bundle fragmentBundle;
    static ArrayList<objforRest> arraylist14 = new ArrayList<>();
    public WorkshopAdapter(Bundle bundle, Context context, ArrayList<objforRest> arrayList ) {
        this.context = context;
        this.fragmentBundle = bundle;
        this.arraylist14 = arrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
    public interface Listener {
        public void onClick(int position, String Ways);
    }
    @Override
    public WorkshopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview1, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(WorkshopAdapter.ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;
        final Context context = cardView.getContext();
        final TextView textView = (TextView) cardView.findViewById(R.id.textView9);
        final TextView textView1 = (TextView)cardView.findViewById(R.id.textView10);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = arraylist14.get(position).getGHY();
                String name = arraylist14.get(position).getHyt();
                Double latsi = arraylist14.get(position).getLats();
                Double lngi = arraylist14.get(position).getLngs();
                Uri gmmIntentUri = Uri.parse("geo:"+latsi+","+lngi+"?q="+Uri.encode(name + place));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                    arraylist14.clear();
                    PumpAdapter.arrayList11.clear();
                }
            }
        });
        if(arraylist14.size() != 0){

            String place = arraylist14.get(position).getGHY();
            String name = arraylist14.get(position).getHyt();
            textView.setText(name);
            textView1.setText(place);
        }

    }

    @Override
    public int getItemCount() {
       int siz =  arraylist14.size();
        return siz;
    }
}
