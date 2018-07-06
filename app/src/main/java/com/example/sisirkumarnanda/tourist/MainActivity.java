package com.example.sisirkumarnanda.tourist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sisirkumarnanda.tourist.Adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //resturants hotels schools hospitals market
//        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
//        items = new ArrayList<String>();
//
//        items.add("Market Place");
//        items.add("Hospitals");
//        items.add("Hotels");
//        items.add("Schools");
//        items.add("Restaurants");

//        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,items);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(recyclerViewAdapter);







    }
}
