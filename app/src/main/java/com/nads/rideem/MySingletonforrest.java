package com.nads.rideem;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
/**
 * Created by Admin on 2/14/2018.
 */
public class MySingletonforrest {
    private static MySingletonforrest mySingleTon;
    private RequestQueue requestQueue;
    private static Context mctx;
    private MySingletonforrest(Context context){
        this.mctx=context;
        this.requestQueue=getRequestQueue();
    }
    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingletonforrest getInstance(Context context){
        if (mySingleTon==null){
            mySingleTon=new MySingletonforrest(context);
        }
        return mySingleTon;
    }
    public<T> void addToRequestQue(Request<T> request){
        requestQueue.add(request);
    }
}