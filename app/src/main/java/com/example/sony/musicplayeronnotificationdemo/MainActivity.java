package com.example.sony.musicplayeronnotificationdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void play(View v)
    {
        Intent in = new Intent(this,MusicService.class);
        in.setAction("play_music");
        startService(in);

    }
}
