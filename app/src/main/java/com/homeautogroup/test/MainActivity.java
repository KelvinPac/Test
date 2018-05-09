package com.homeautogroup.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    WebView webView;
    String htmlPrivacy;
    Toolbar toolbar;

    //Firestore
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Privacy Policy");
        setSupportActionBar(toolbar);


        webView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //FireStore
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);

        mProgressBar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        loadAboutAppData();
    }

    //Load the About Data From Firestore
    private void loadAboutAppData() {
        DocumentReference docRef = firebaseFirestore.collection("AppDetails").document("Privacy");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                mProgressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("privacy")) {
                        String privacy = documentSnapshot.getString("privacy");
                        setResult(privacy);
                    }


                } else {
                    showToast("data is not available");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                showToast(e.toString());
            }
        });
    }

    private void setResult(String privacy) {

        String mimeType = "text/html";
        String encoding = "utf-8";
        //String htmlText = htmlPrivacy;

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/custom.ttf\")}body{font-family: MyFont;color: #525252;text-align:justify}"
                + "</style></head>"
                + "<body>"
                + privacy
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
    }


    public void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_activity_2:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
