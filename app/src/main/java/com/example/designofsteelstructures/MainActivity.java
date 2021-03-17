package com.example.designofsteelstructures;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_bolt_single, tv_weld_single, tv_bolt_double, tv_weld_double;

        tv_bolt_single = findViewById(R.id.bolt_single);
        tv_bolt_single.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SingleAngle.class);
            startActivity(intent);
        });

        tv_weld_single = findViewById(R.id.weld_single);
        tv_weld_single.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WeldAngle.class);
            startActivity(intent);
        });

        tv_bolt_double = findViewById(R.id.bolt_double);
        tv_bolt_double.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SingleAngle.class);
            startActivity(intent);
        });

        tv_weld_double = findViewById(R.id.weld_double);
        tv_weld_double.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WeldAngle.class);
            startActivity(intent);
        });
    }
}