package com.example.sisirkumarnanda.tourist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class DiscoverActivity extends AppCompatActivity {

    CardView wildLIfe,temples,palaces,museums,lakes,forts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        wildLIfe = (CardView)findViewById(R.id.wildLifeCard);
        temples = (CardView)findViewById(R.id.templeCard);
        palaces = (CardView)findViewById(R.id.palaceCard);
        museums = (CardView)findViewById(R.id.museumCard);
        lakes = (CardView)findViewById(R.id.lakeCard);
        forts = (CardView)findViewById(R.id.fortCard);

        wildLIfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscoverActivity.this,TopPlacesActivity.class);
                intent.putExtra("discover","wildLifeCard");
                startActivity(intent);
            }
        });
        temples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscoverActivity.this,TopPlacesActivity.class);
                intent.putExtra("discover","templeCard");
                startActivity(intent);
            }
        });
        palaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscoverActivity.this,TopPlacesActivity.class);
                intent.putExtra("discover","palaceCard");
                startActivity(intent);
            }
        });
        museums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscoverActivity.this,TopPlacesActivity.class);
                intent.putExtra("discover","museumCard");
                startActivity(intent);
            }
        });
        lakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscoverActivity.this,TopPlacesActivity.class);
                intent.putExtra("discover","lakeCard");
                startActivity(intent);
            }
        });
        forts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscoverActivity.this,TopPlacesActivity.class);
                intent.putExtra("discover","fortCard");
                startActivity(intent);
            }
        });


    }
}
