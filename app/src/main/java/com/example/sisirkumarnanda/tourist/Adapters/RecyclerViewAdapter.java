package com.example.sisirkumarnanda.tourist.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sisirkumarnanda.tourist.Common;
import com.example.sisirkumarnanda.tourist.MapsActivity;
import com.example.sisirkumarnanda.tourist.Model.DetailOfPlace;
import com.example.sisirkumarnanda.tourist.R;
import com.example.sisirkumarnanda.tourist.RemoteServices.MyGoogleAPIService;
import com.example.sisirkumarnanda.tourist.ViewTopPlaceDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SISIR KUMAR NANDA on 02-07-2018.
 */

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private ArrayList<String> items;
    private Context mContext;
    LayoutInflater inflater;
    private ArrayList<String> photoReference;
    Context mPicassoContext;

    public RecyclerViewAdapter(Context mContext,ArrayList<String> items,ArrayList<String> photoReference){
        this.mContext = mContext;
        this.items = items;
        this.photoReference = photoReference;
        inflater = LayoutInflater.from(this.mContext);

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = inflater.inflate(R.layout.travel_single_item,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, final int position) {
        if(photoReference!=null&&items!=null){



            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build().load(getPhotoPlace(photoReference.get(position),400)).into(holder.titleImage);

            holder.titleText.setText(items.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     Intent intent = new Intent(mContext, ViewTopPlaceDetailsActivity.class);
                    intent.putExtra("rating",Common.currentTopPlaceResult[position].getRating());
                    intent.putExtra("name",items.get(position));


                    Toast.makeText(mContext, Common.currentTopPlaceResult[position].getName(), Toast.LENGTH_SHORT).show();


                    intent.putExtra("place_id",Common.currentTopPlaceResult[position].getPlace_id());

                    intent.putExtra("photoAddress",getPhotoPlace(photoReference.get(position),500));
                    if(Common.currentTopPlaceResult[position].getOpening_hours()!=null) {
                        intent.putExtra("opening_hours", Common.currentTopPlaceResult[position].getOpening_hours().getOpen_now());
                    }else{
                        intent.putExtra("opening_hours", "Not available");
                    }

                    Common.currentResult = Common.currentTopPlaceResult[position];

                    mContext.startActivity(intent);



                }
            });


        }




    }

    private String getPhotoPlace(String photoReference,int maxWidth) {
        StringBuilder photoUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        photoUrl.append("?maxwidth=" + maxWidth);
        photoUrl.append("&photoreference=" + photoReference);
        photoUrl.append("&key=AIzaSyAT2NHL3pzEluwdY5GfqyE3R8CGakn-Pbo");
        return photoUrl.toString();

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





        }

    }


}
