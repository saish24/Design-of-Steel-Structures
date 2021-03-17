package com.example.designofsteelstructures;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultShowcase extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);


        Intent intent = getIntent();

        boolean isBoltConnection = intent.getBooleanExtra("isBolt", true);

        String result = intent.getStringExtra("results");
        TextView textView = findViewById(R.id.results);
        textView.setText(result);

        result = intent.getStringExtra("factored_load");
        textView = findViewById(R.id.tv_factored_load);
        textView.setText(result);

        result = intent.getStringExtra("len_of_ten_member");
        textView = findViewById(R.id.tv_len_of_tension_member);
        textView.setText(result);

        result = intent.getStringExtra("slenderness_ratio");
        textView = findViewById(R.id.tv_slenderness_ratio);
        textView.setText(result);

        result = intent.getStringExtra("gamma_m1");
        textView = findViewById(R.id.tv_gamma_m1);
        textView.setText(result);

        result = intent.getStringExtra("gamma_m0");
        textView = findViewById(R.id.tv_gamma_m0);
        textView.setText(result);

        result = intent.getStringExtra("gamma_mb");
        textView = findViewById(R.id.tv_gamma_mb);
        textView.setText(result);


        if(isBoltConnection){
            result = intent.getStringExtra("diameter_of_bolt");
            textView = findViewById(R.id.tv_dia_of_bolt);
            textView.setText(result);

            result = intent.getStringExtra("num_of_bolts");
            textView = findViewById(R.id.tv_num_of_bolts);
            textView.setText(result);

            LinearLayout linearLayout = findViewById(R.id.bolted_only);
            linearLayout.setVisibility(View.VISIBLE);

            linearLayout = findViewById(R.id.bolted_only_3);
            linearLayout.setVisibility(View.VISIBLE);

            linearLayout = findViewById(R.id.welded_only);
            linearLayout.setVisibility(View.GONE);

            textView = findViewById(R.id.tv_gamma_mb_PSF);
            textView.setText("PSF for Bolt");
        } else {
            result = intent.getStringExtra("throat_thickness");
            textView = findViewById(R.id.tv_throat_thickness_weld);
            textView.setText(result);

            LinearLayout linearLayout = findViewById(R.id.bolted_only);
            linearLayout.setVisibility(View.GONE);

            linearLayout = findViewById(R.id.bolted_only_3);
            linearLayout.setVisibility(View.GONE);

            linearLayout = findViewById(R.id.welded_only);
            linearLayout.setVisibility(View.VISIBLE);

            textView = findViewById(R.id.tv_gamma_mb_PSF);
            textView.setText("PSF for Weld");
        }

    }
}
