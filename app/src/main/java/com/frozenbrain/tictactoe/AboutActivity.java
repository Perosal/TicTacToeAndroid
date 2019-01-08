package com.frozenbrain.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.r0adkll.slidr.Slidr;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Glide.with(this).load(R.drawable.duck).into((ImageView)findViewById(R.id.duck));
        Glide.with(this).load(R.drawable.clock).into((ImageView)findViewById(R.id.clock));
        Glide.with(this).load(R.drawable.android).into((ImageView)findViewById(R.id.android));

        Slidr.attach(this);
    }
}
