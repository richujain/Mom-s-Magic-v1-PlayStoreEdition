package com.pulimoottil.richu.momsmagic_indianfoodrecipe.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pulimoottil.richu.momsmagic_indianfoodrecipe.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.ValueEventListener;



public class HomeFragment extends Fragment {



    //the recyclerview
    RecyclerView recyclerView;

    //a list to store all the products
    List<Item> itemList;
    ProgressDialog progress;
    //firebase variables
    String title,imageurl,videoby,views;

    ItemAdapter adapter;

    private OnFragmentInteractionListener mListener;






    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FrameLayout ff = (FrameLayout)inflater.inflate(R.layout.fragment_home, container, false);
        //View ff = inflater.inflate(R.layout.fragment_home, container, false);
        //getting the recyclerview from xml

        progress =  new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);







        //initializing the productlist
        itemList = new ArrayList<>();
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("Data");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Item item = snapshot.getValue(Item.class);
                    //itemList.add(item);
                    try{
                        title = snapshot.child("title").getValue().toString();
                        imageurl = snapshot.child("imageurl").getValue().toString();
                        videoby = snapshot.child("videoby").getValue().toString();
                        views = snapshot.child("views").getValue().toString();
                    }catch (NullPointerException e){
                        Log.e("Exception",e.toString());
                    }

                        itemList.add(
                                new Item(
                                        title,
                                        imageurl,
                                        videoby));
                }

                progress.dismiss();
                //creating recyclerview adapter
                adapter = new ItemAdapter(getActivity(), itemList);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+databaseError, Toast.LENGTH_SHORT).show();
                Log.e("oncancelled",databaseError.toString());
            }
        });




        //creating recyclerview adapter
        adapter = new ItemAdapter(getActivity(), itemList);
        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
