package com.pulimoottil.richu.momsmagic_indianfoodrecipe.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pulimoottil.richu.momsmagic_indianfoodrecipe.R;
import com.pulimoottil.richu.momsmagic_indianfoodrecipe.activity.YoutubeFoodRecipeSearch;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    int flag = 1;
    ProgressDialog progress;
    private DatabaseReference mUserDatabase;
    private InterstitialAd mInterstitialAd;

    private OnFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter<Search_Items, SearchItemsViewHolder> firebaseRecyclerAdapter;


    


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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


        mUserDatabase = FirebaseDatabase.getInstance().getReference("Data");



        mSearchField = view.findViewById(R.id.search_field);
        mSearchBtn = view.findViewById(R.id.search_btn);

        mResultList = view.findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query firebaseSearchQuery = mUserDatabase.orderByChild("search");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Search_Items, SearchItemsViewHolder>(

                Search_Items.class,
                R.layout.search_list_layout,
                SearchItemsViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(SearchItemsViewHolder viewHolder, Search_Items model, int position) {
                //viewHolder.setDetails(getContext(), model.getTitle(), model.getYoutubeUrl(), model.getImageUrl());

            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display ad
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

                progress =  new ProgressDialog(getContext());
                progress.setTitle("Searching");
                progress.setMessage("Wait while searching...");
                progress.setCancelable(false);
                progress.show();
                String searchText = mSearchField.getText().toString();
                if(searchText.isEmpty()){
                    Toast.makeText(getActivity(), "Search Field Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    String capitilisedSearchText = capitalize(searchText);
                    firebaseUserSearch(capitilisedSearchText);
                }
                progress.dismiss();
            }
        });
    }
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }





    private void firebaseUserSearch(String searchText) {

        //Toast.makeText(getActivity(), "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mUserDatabase.orderByChild("search").startAt(searchText).endAt(searchText+"\uf8ff");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Search_Items, SearchItemsViewHolder>(

                Search_Items.class,
                R.layout.search_list_layout,
                SearchItemsViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(final SearchItemsViewHolder viewHolder, Search_Items model, final int position) {
                viewHolder.setDetails(getContext(), model.getTitle(),model.getImageUrl(), model.getVideoBy());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flag = 1;
                        mUserDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    final int key = Integer.parseInt(getRef(position).getKey());
                                    if(flag == key){
                                        String YOUTUBE_VIDEO_CODE = snapshot.child("youtubeurl").getValue().toString();
                                        String title = snapshot.child("title").getValue().toString();
                                        String ingredients = snapshot.child("ingredients").getValue().toString().replace("_b","\n");
                                        Intent intent = new Intent(getActivity(), YoutubeFoodRecipeSearch.class);
                                        intent.putExtra("YOUTUBE_VIDEO_CODE",YOUTUBE_VIDEO_CODE);
                                        intent.putExtra("title",title);
                                        intent.putExtra("ingredients",ingredients);
                                        //((Activity)mCtx).finish();
                                        getActivity().startActivity(intent);
                                        break;
                                    }
                                    else {
                                        flag++;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        };


        mResultList.setAdapter(firebaseRecyclerAdapter);

    }



    // View Holder Class

    public static class SearchItemsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public SearchItemsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context ctx, String title, String image,String videoby) {


            TextView user_name = mView.findViewById(R.id.name_text);
            TextView user_status = mView.findViewById(R.id.status_text);
            ImageView user_image = mView.findViewById(R.id.profile_image);

            user_image.setVisibility(View.VISIBLE);
            user_name.setVisibility(View.VISIBLE);
            user_status.setVisibility(View.VISIBLE);

            user_name.setText(title);
            user_status.setText(videoby);
            Picasso.get().load(image).into(user_image);


        }
    }
























    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
