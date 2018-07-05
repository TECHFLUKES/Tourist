package com.example.sisirkumarnanda.tourist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sisirkumarnanda.tourist.MapsActivity;
import com.example.sisirkumarnanda.tourist.R;

import java.util.ArrayList;

/**
 * Created by SISIR KUMAR NANDA on 02-07-2018.
 */

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private ArrayList<String> items;
    private Context mContext;
    LayoutInflater inflater;

    public RecyclerViewAdapter(Context mContext,ArrayList<String> items){
        this.mContext = mContext;
        this.items = items;
        inflater = LayoutInflater.from(this.mContext);

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = inflater.inflate(R.layout.travel_single_item,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.titleText.setText(items.get(position));
        String check = items.get(position);

        if(check.equals("Market Place")){
            holder.titleImage.setImageResource(R.drawable.market);
        }else if(check.equals("Hospitals")){
            holder.titleImage.setImageResource(R.drawable.hos);
        }else if(check.equals("Hotels")){
            holder.titleImage.setImageResource(R.drawable.hotels);
        }else if(check.equals("Schools")){
            holder.titleImage.setImageResource(R.drawable.schools);
        }else{
            holder.titleImage.setImageResource(R.drawable.res);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView titleImage;
        public MyViewHolder(final View itemView) {
            super(itemView);
            titleImage = itemView.findViewById(R.id.thumbnail);
            titleText = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, titleText.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MapsActivity.class);
                    if(titleText.getText().toString().equals("Market Place")){
                        intent.putExtra("nearByLocation","market");
                    }
                    else if(titleText.getText().toString().equals("Hotels")){
                        intent.putExtra("nearByLocation","hotel");
                    }
                    else if(titleText.getText().toString().equals("Hospitals")){
                        intent.putExtra("nearByLocation","hospital");
                    }
                    else if(titleText.getText().toString().equals("Schools")){
                        intent.putExtra("nearByLocation","school");
                    }
                    else if(titleText.getText().toString().equals("Restaurants")){
                        intent.putExtra("nearByLocation","restaurant");
                    }


                    mContext.startActivity(intent);

                }
            });

        }
    }


}
