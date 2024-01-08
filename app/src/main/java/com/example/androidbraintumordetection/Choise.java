package com.example.androidbraintumordetection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Choise extends AppCompatActivity{

    Button info;
    ImageButton back;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choise);
        back = (ImageButton) findViewById(R.id.analyser);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lorsque btn1 est cliqué, démarrer Activity1
                Intent intent = new Intent(Choise.this, MainActivity.class);
                startActivity(intent);
            }
        });


        info = (Button) findViewById(R.id.propos);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lorsque btn1 est cliqué, démarrer Activity1
                Intent intent = new Intent(Choise.this, about.class);
                startActivity(intent);
            }
        });

    }
}
