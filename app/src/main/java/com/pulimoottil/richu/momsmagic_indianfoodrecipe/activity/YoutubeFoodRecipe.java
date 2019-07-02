package com.pulimoottil.richu.momsmagic_indianfoodrecipe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pulimoottil.richu.momsmagic_indianfoodrecipe.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class YoutubeFoodRecipe extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    int flag = 0;
    String YOUTUBE_VIDEO_CODE,title,ingredients;
    TextView textViewTitle,textViewIngredients;
    ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_youtube_food_recipe);



        textViewTitle = (TextView)findViewById(R.id.textViewTitle);
        textViewIngredients = (TextView)findViewById(R.id.textViewIngredients);
        mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        // Initializing video player with developer key
        youTubeView.initialize(Config.DEVELOPER_KEY, this);
        Intent intent = getIntent();
        //Toast.makeText(this, ""+intent.getIntExtra(), Toast.LENGTH_SHORT).show();
        final int clickedPosition = Integer.parseInt(intent.getStringExtra("clickedPostion"));
        final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("Data");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(flag == clickedPosition){
                       YOUTUBE_VIDEO_CODE = snapshot.child("youtubeurl").getValue().toString();
                        title = snapshot.child("title").getValue().toString();
                        ingredients = snapshot.child("ingredients").getValue().toString().replace("_b","\n");
                        textViewTitle.setText(title);
                        textViewIngredients.setText(ingredients);
                        scrollToBottom();
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

    private void scrollToBottom(){
        mScrollView.post(new Runnable()
        {
            public void run()
            {
                mScrollView.fullScroll(View.FOCUS_UP);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            //zString YOUTUBE_VIDEO_CODE = "_oEA18Y8gM0";
            youTubePlayer.loadVideo(YOUTUBE_VIDEO_CODE);
            //youTubePlayer.setFullscreen(true);

            // Hiding player controls
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }


}
