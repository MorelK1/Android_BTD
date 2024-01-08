package com.example.androidbraintumordetection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity{

    Button analyser;

    TextView propos;
    ImageButton back;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        analyser = findViewById(R.id.analyser);
        propos = findViewById(R.id.propos);
        analyser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MainActivity.class);

                // Démarrer l'activité destination
                startActivity(intent);
            }
        });

        propos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickTextView(v);
            }
        });
    }

    public void onClickTextView(View view) {
        // Action à effectuer lors du clic sur le TextView
        // Par exemple, afficher un message
        Intent intent = new Intent(Home.this, About.class);

        // Démarrer l'activité destination
        startActivity(intent);
//        ((TextView) view).setText("Clic détecté !");
    }
}
