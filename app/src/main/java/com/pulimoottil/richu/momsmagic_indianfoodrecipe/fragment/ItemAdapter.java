package com.pulimoottil.richu.momsmagic_indianfoodrecipe.fragment;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pulimoottil.richu.momsmagic_indianfoodrecipe.R;
import com.pulimoottil.richu.momsmagic_indianfoodrecipe.activity.YoutubeFoodRecipe;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{

    //this context we will use to inflate the layout
    private Context mCtx;
    //adMob
    private InterstitialAd mInterstitialAd;
    int views;
    final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("Data");


    //we are storing all the products in a list
    private List<Item> itemList;

    //getting the context and product list with constructor
    public ItemAdapter(Context mCtx, List<Item> itemList) {
        this.mCtx = mCtx;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_items, null);


        mInterstitialAd = new InterstitialAd(view.getContext());
        //ca-app-pub-3940256099942544/1033173712 is the test id
        //ca-app-pub-8475605926979175/6024608569 is the original one
        mInterstitialAd.setAdUnitId("ca-app-pub-8475605926979175/6024608569");
        //load ad
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //initializing admob
        //my id ca-app-pub-8475605926979175~3307231298
        MobileAds.initialize(view.getContext(),
                "ca-app-pub-8475605926979175~3307231298");




        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        //getting the product of the specified position
        Item item = itemList.get(position);

        //recyclerview onclick
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display ad
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                final String clickedPostion = String.valueOf(holder.getAdapterPosition());
                Intent intent = new Intent(view.getContext(), YoutubeFoodRecipe.class);
                intent.putExtra("clickedPostion",clickedPostion);
                //((Activity)mCtx).finish();
                view.getContext().startActivity(intent);


            }
        });

        //binding the data with the viewholder views
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewVideoBy.setText(item.getVideoBy());

        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(item.getImage()));
        Picasso.get().load(item.getImage()).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle,textViewVideoBy;
        ImageView imageView;
        View view;
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("Data");

        public ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageView = itemView.findViewById(R.id.imageView);
            textViewVideoBy = itemView.findViewById(R.id.textViewVideoBy);
        }
    }
}


