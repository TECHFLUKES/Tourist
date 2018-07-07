package com.example.sisirkumarnanda.tourist;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class ServicesActivity extends AppCompatActivity {

    CardView olaCard,uberCard,mmtCard,irctcCard,oyoCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        olaCard = (CardView)findViewById(R.id.olaCard);
        oyoCard = (CardView)findViewById(R.id.oyoCard);
        uberCard = (CardView)findViewById(R.id.uberCard);
        irctcCard = (CardView)findViewById(R.id.irctcCard);
        mmtCard = (CardView)findViewById(R.id.makeMyTripCard);


        olaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.olacabs.customer");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }else
                {
                    Uri uri = Uri.parse("market://details?id=com.olacabs.customer");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.olacabs.customer")));
                    }
                }
            }
        });
        mmtCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.makemytrip");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }else
                {
                    Uri uri = Uri.parse("market://details?id=com.makemytrip");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.makemytrip")));
                    }
                }
            }
        });
        oyoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.oyo.consumer");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }else
                {
                    Uri uri = Uri.parse("market://details?id=com.oyo.consumer");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.oyo.consumer")));
                    }
                }
            }
        });
        uberCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.ubercab");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }else
                {
                    Uri uri = Uri.parse("market://details?id=com.ubercab");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")));
                    }
                }
            }
        });





    }
}
