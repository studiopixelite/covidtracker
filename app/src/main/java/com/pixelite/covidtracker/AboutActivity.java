package com.pixelite.covidtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialToolbar toolbar;

    private ImageButton fbButton;
    private ImageButton twButton;
    private ImageButton igButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = findViewById(R.id.toolbarAbout);

        fbButton = findViewById(R.id.facebook_Button);
        twButton = findViewById(R.id.twitter_Button);
        igButton = findViewById(R.id.instagram_Button);

        fbButton.setOnClickListener(this);
        twButton.setOnClickListener(this);
        igButton.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.facebook_Button:
                Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/studiopixelite"));
                startActivity(fbIntent);
                break;

            case R.id.twitter_Button:
                Intent twIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/StudioPixelite"));
                startActivity(twIntent);
                break;

            case R.id.instagram_Button:
                Intent igIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/studiopixelite"));
                startActivity(igIntent);
                break;
        }
    }
}
