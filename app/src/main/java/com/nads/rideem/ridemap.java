package com.nads.rideem;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class ridemap extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInClient mGoogleSignInClient;
    private static final String User = "text";
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 123;
    private static final String LOG_TAG = "d";
    public void onCreate(Bundle SavedInstanceState){
    super.onCreate(SavedInstanceState);
    setContentView(R.layout.ridemap);
    if(isOnline()!=true){
        Toast toast = Toast.makeText(this, "There's no network connection Please connect and refresh from menu ", Toast.LENGTH_LONG);
        toast.show();

    }

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2991702481825090~9700215450");


        // AdView adView = new AdView(this);
        AdView adView = (AdView) findViewById(R.id.adView);
       // adView.setAdSize(AdSize.BANNER);
        //adView.setAdUnitId("ca-app-pub-2991702481825090/1104313900");
        AdRequest request = new AdRequest.Builder()
                // .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
              // .addTestDevice("658F3783B85CC8CA35A2B54A49AA11D9")  // An example device ID
                .build();
        adView.loadAd(request);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

}
   private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.default_web_client_id))
               .requestEmail()
               .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
public void onStart(){
        super.onStart();
       try{ if(GoogleSignIn.getLastSignedInAccount(this)!= null){
   GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
   if(account != null){
   firebaseAuthWithGoogle(account);
   }
   return;
        }
       }catch (Exception e){
           e.printStackTrace();
       }
}
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
  /*  @Override
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
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                               // FirebaseAuth.getInstance().signOut();
                                Toast toast = Toast.makeText(ridemap.this, "signed out", Toast.LENGTH_LONG);
                               toast.show();
                                //android.os.Process.killProcess(android.os.Process.myPid());
                                //System.exit(1);
                            }
                        });
            case R.id.deleteid:
                Log.i(LOG_TAG,"delete menu item selected");

                FirebaseAuth.getInstance().getCurrentUser()
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast toast = Toast.makeText(ridemap.this,"clear id",Toast.LENGTH_LONG);
                            toast.show();
                            }
                        });

                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                Log.w(LOG_TAG, "Google sign in failed", e);
            }
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(LOG_TAG, "signInResult:failed code=" + e.getStatusCode());
          //  updateUI(null);
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "signInWithCredential:success");
                           // FirebaseUser user = mAuth.getCurrentUser();
                           /*String name = user.getDisplayName();
                            TextView textView = (TextView) findViewById(R.id.textView);
                            textView.setText(name);*/
                           Intent intent = new Intent(ridemap.this,mapride.class);
                           ridemap.this.startActivity(intent);
                           progressDialog.dismiss();
                        } else {
                            Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    public void onBackPressed(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
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

}