package com.homeautogroup.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class AboutUsActivity extends AppCompatActivity {

    TextView txtAppName, txtVersion, txtCompany, txtEmail, txtWebsite, txtContact;
    ImageView imgAppLogo;
    ArrayList<ItemAbout> mListItem;
    ScrollView mScrollView;
    ProgressBar mProgressBar;
    WebView webView;
    Toolbar toolbar;

    //Firestore
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About Us");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtAppName = (TextView) findViewById(R.id.text_app_name);
        txtVersion = (TextView) findViewById(R.id.text_version);
        txtCompany = (TextView) findViewById(R.id.text_company);
        txtEmail = (TextView) findViewById(R.id.text_email);
        txtWebsite = (TextView) findViewById(R.id.text_website);
        txtContact = (TextView) findViewById(R.id.text_contact);
        imgAppLogo = (ImageView) findViewById(R.id.image_app_logo);
        webView = (WebView) findViewById(R.id.webView);

        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mListItem = new ArrayList<>();

        //FireStore
        firebaseFirestore = FirebaseFirestore.getInstance();

        //show loading progress and hide scroll view
        mProgressBar.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
        loadAboutAppData();


    }

    //Load the About Data From Firestore
    private void loadAboutAppData() {
        final DocumentReference docRef = firebaseFirestore
                .collection("AppDetails")
                .document("About");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);

                if (documentSnapshot.exists()) {
                    //Map data to class then show results
                    ItemAbout about = documentSnapshot.toObject(ItemAbout.class);
                    setResult(about);
                } else {
                    showToast("data is not available");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
                showToast(e.toString());
            }
        });
    }


    private void setResult(ItemAbout itemAbout) {

        txtAppName.setText(itemAbout.getAppName());
        txtVersion.setText(itemAbout.getAppVersion());
        txtCompany.setText(itemAbout.getAppAuthor());
        txtEmail.setText(itemAbout.getAppEmail());
        txtWebsite.setText(itemAbout.getAppWebsite());
        txtContact.setText(itemAbout.getAppContact());
        //TODO ABOUT US ACTIVITY (2)  Add Image Loading Library (Picasso)
        // Picasso.with(AboutUsActivity.this).load(Constant.IMAGE_PATH + itemAbout.getAppLogo()).into(imgAppLogo);

        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";
        String htmlText = itemAbout.getAppDescription();

        String text = "<html><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/custom.ttf\")}body{font-family: MyFont;color: #8b8b8b;text-align:justify}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
    }


    public void showToast(String msg) {
        Toast.makeText(AboutUsActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // onBackPressed();

                finish();    //lets finish this activity
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
