package com.pulimoottil.richu.momsmagic_indianfoodrecipe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pulimoottil.richu.momsmagic_indianfoodrecipe.R;
import com.github.chrisbanes.photoview.PhotoView;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.credits);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
