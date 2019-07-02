package com.pulimoottil.richu.momsmagic_indianfoodrecipe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pulimoottil.richu.momsmagic_indianfoodrecipe.R;

public class SecondTutorialActivity extends AppCompatActivity {
    Button response;
    TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_tutorial);
        response = (Button)findViewById(R.id.response);
        skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondTutorialActivity.this, HomeActivity.class));
                finish();
            }
        });
        response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondTutorialActivity.this, ThirdTutorialActivity.class));
                finish();
            }
        });
    }
}
