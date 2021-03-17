package com.example.designofsteelstructures;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DoubleWeldAngle extends AppCompatActivity {
    private EditText et_Factored_Load_weld, et_length_of_tension_member_weld, et_allowable_slenderness_ratio_weld, et_throat_thickness, et_custom_safety_factor_weld, et_Ultimate_Tensile_Stress_weld, et_Yield_Stress_weld, et_gamma_0_weld, et_gamma_1_weld;
    private CheckBox cb_use_Fe_410_steel_weld, cb_take_partial_safety_factors_wrt_table5_weld, cb_connected_length_larger_weld;
    private RadioButton rb_on_sides_parallel_to_axis, rb_on_three_sides, rb_shop_weld, rb_equal_section_weld;
    private Double factoredLoad, lengthOfTensionMember, allowableSlendernessRatio, throatThickness, customSafetyFactor, ultimateTensileStress, steelYieldStress, gamma_m0, gamma_m1;
    private ArrayList<Design> ISA_Angles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_angle_weld);

        et_Factored_Load_weld = findViewById(R.id.et_Factored_Load_weld);
        et_length_of_tension_member_weld = findViewById(R.id.et_len_of_tension_member_weld);
        et_allowable_slenderness_ratio_weld = findViewById(R.id.et_allowable_slenderness_ratio_weld);
        et_Ultimate_Tensile_Stress_weld = findViewById(R.id.et_Ultimate_Tensile_Stress_weld);
        et_Yield_Stress_weld = findViewById(R.id.et_Yield_Stress_weld);
        et_gamma_0_weld = findViewById(R.id.et_gamma_0_weld);
        et_gamma_1_weld = findViewById(R.id.et_gamma_1_weld);
        et_custom_safety_factor_weld = findViewById(R.id.et_custom_safety_factor_weld);
        et_throat_thickness = findViewById(R.id.et_throat_thickness_weld);

        cb_use_Fe_410_steel_weld = findViewById(R.id.checkbox_Use_fe410_weld);
        cb_take_partial_safety_factors_wrt_table5_weld = findViewById(R.id.checkbox_Use_IS800_table5_values_weld);
        cb_connected_length_larger_weld = findViewById(R.id.checkbox_connected_length_larger_weld);

        rb_on_sides_parallel_to_axis = findViewById(R.id.rb_on_sides_parallel_to_axis);
        rb_on_three_sides = findViewById(R.id.rb_on_three_sides);
        rb_shop_weld = findViewById(R.id.rb_shop_weld);
        rb_equal_section_weld = findViewById(R.id.radio_equal_angle_weld);

        cb_use_Fe_410_steel_weld.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_Yield_Stress_weld.setText(R.string.FE410_steel_yield_stress);
                et_Yield_Stress_weld.setTextColor(getResources().getColor((R.color.faded)));

                et_Ultimate_Tensile_Stress_weld.setText(R.string.FE410_ultimate_tensile_stress);
                et_Ultimate_Tensile_Stress_weld.setTextColor(getResources().getColor((R.color.faded)));

//                    Delete lines ahead
                et_Factored_Load_weld.setText("180");
                et_length_of_tension_member_weld.setText("2500");
                et_allowable_slenderness_ratio_weld.setText("350");
                et_throat_thickness.setText("3.5");
                rb_on_sides_parallel_to_axis.performClick();
                cb_take_partial_safety_factors_wrt_table5_weld.performClick();

            } else {
                et_Yield_Stress_weld.setTextColor(Color.BLACK);
                et_Ultimate_Tensile_Stress_weld.setTextColor(Color.BLACK);
            }
        });
        cb_take_partial_safety_factors_wrt_table5_weld.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_gamma_1_weld.setText(R.string.gamma_m1);
                et_gamma_0_weld.setText(R.string.gamma_m0);

                et_gamma_1_weld.setTextColor(getResources().getColor((R.color.faded)));
                et_gamma_0_weld.setTextColor(getResources().getColor((R.color.faded)));
            } else {
                et_gamma_1_weld.setTextColor(Color.BLACK);
                et_gamma_0_weld.setTextColor(Color.BLACK);
            }
        });
        rb_shop_weld.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                et_custom_safety_factor_weld.setText(R.string.shop_weld);
            } else {
                et_custom_safety_factor_weld.setText(R.string.site_weld);
            }
            et_custom_safety_factor_weld.setTextColor(getResources().getColor((R.color.faded)));
        });

//        rb_equal_section.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                } else {
//
//                }
//            }
//        });

        TextView next = findViewById(R.id.btn_submit_weld);
        next.setOnClickListener(v -> {
            readISAValues();
            make_design();
        });
    }

    private boolean updateValues() {
        if (cb_use_Fe_410_steel_weld.isChecked()) {
            et_Ultimate_Tensile_Stress_weld.setText(R.string.FE410_ultimate_tensile_stress);
            et_Yield_Stress_weld.setText(R.string.FE410_steel_yield_stress);
        }

        if (cb_take_partial_safety_factors_wrt_table5_weld.isChecked()) {
            et_gamma_1_weld.setText(R.string.gamma_m1);
            et_gamma_0_weld.setText(R.string.gamma_m0);
        }

        if (cb_take_partial_safety_factors_wrt_table5_weld.isChecked()) {
            et_gamma_0_weld.setText(R.string.gamma_m0);
            et_gamma_1_weld.setText(R.string.gamma_m1);
        }

        String value = et_Factored_Load_weld.getText().toString();
        if (value.equals("")) return false;
        factoredLoad = 1000 * Double.parseDouble(value);

        value = et_length_of_tension_member_weld.getText().toString();
        if (value.equals("")) return false;
        lengthOfTensionMember = Double.parseDouble(value);

        value = et_allowable_slenderness_ratio_weld.getText().toString();
        if (value.equals("")) return false;
        allowableSlendernessRatio = Double.parseDouble(value);

        value = et_Ultimate_Tensile_Stress_weld.getText().toString();
        if (value.equals("")) return false;
        ultimateTensileStress = Double.parseDouble(value);

        value = et_Yield_Stress_weld.getText().toString();
        if (value.equals("")) return false;
        steelYieldStress = Double.parseDouble(value);

        value = et_throat_thickness.getText().toString();
        if (value.equals("")) return false;
        throatThickness = Double.parseDouble(value);

        value = et_gamma_0_weld.getText().toString();
        if (value.equals("")) return false;
        gamma_m0 = Double.parseDouble(value);

        value = et_gamma_1_weld.getText().toString();
        if (value.equals("")) return false;
        gamma_m1 = Double.parseDouble(value);

        value = et_custom_safety_factor_weld.getText().toString();
        if (value.equals("")) return false;
        customSafetyFactor = Double.parseDouble(value);

        return true;
    }

    void readISAValues() {
        ISA_Angles.clear();

        if (rb_equal_section_weld.isChecked()) {
            ISA_Angles.add(new Design(20, 20, 3, 1.12, 0.9, 0.59, 1.41, 0.4, 0.4, 0.6, 0.6, 0.2, 0.2, 0.58, 0.73, 0.37, 0.3, 0.2, 0.37));
            ISA_Angles.add(new Design(25, 25, 3, 1.41, 1.1, 0.71, 1.79, 0.8, 0.8, 1.2, 1.2, 0.3, 0.3, 0.73, 0.93, 0.47, 0.4, 0.4, 0.47));
            ISA_Angles.add(new Design(20, 20, 4, 1.45, 1.1, 0.63, 1.37, 0.5, 0.5, 0.8, 0.8, 0.2, 0.2, 0.58, 0.72, 0.37, 0.4, 0.3, 0.37));
            ISA_Angles.add(new Design(30, 30, 3, 1.73, 1.4, 0.83, 2.17, 1.4, 1.4, 2.2, 2.2, 0.6, 0.6, 0.89, 1.13, 0.57, 0.6, 0.8, 0.57));
            ISA_Angles.add(new Design(25, 25, 4, 1.84, 1.4, 0.75, 1.75, 1.0, 1.0, 1.6, 1.6, 0.4, 0.4, 0.73, 0.91, 0.47, 0.6, 0.6, 0.47));
            ISA_Angles.add(new Design(35, 35, 3, 2.03, 1.6, 0.95, 2.55, 2.3, 2.3, 3.6, 3.6, 0.9, 0.9, 1.05, 1.33, 0.67, 0.9, 1.3, 0.37));
            ISA_Angles.add(new Design(25, 25, 5, 2.25, 1.8, 0.79, 1.71, 1.2, 1.2, 1.8, 1.8, 0.5, 0.5, 0.72, 0.91, 0.47, 0.7, 0.7, 0.47));
            ISA_Angles.add(new Design(30, 30, 4, 2.26, 1.8, 0.87, 2.13, 1.8, 1.8, 2.8, 2.8, 0.7, 0.7, 0.89, 1.12, 0.57, 0.8, 1.0, 0.57));
            ISA_Angles.add(new Design(40, 40, 3, 2.34, 1.8, 1.08, 2.92, 3.4, 3.4, 5.5, 5.5, 1.4, 1.4, 1.21, 1.54, 0.77, 1.2, 2.0, 0.77));
            ISA_Angles.add(new Design(45, 45, 3, 2.64, 2.1, 1.2, 3.3, 5.0, 5.0, 8.0, 8.0, 2.0, 2.0, 1.38, 1.74, 0.87, 1.5, 2.9, 0.87));
            ISA_Angles.add(new Design(35, 35, 4, 2.66, 2.1, 1.0, 2.5, 2.9, 2.9, 4.7, 4.7, 1.2, 1.2, 1.05, 1.32, 0.67, 1.2, 1.7, 0.67));
            ISA_Angles.add(new Design(30, 30, 5, 2.77, 2.2, 0.92, 2.08, 2.1, 2.1, 3.4, 3.4, 0.9, 0.9, 0.88, 1.11, 0.57, 1.0, 1.2, 0.57));
            ISA_Angles.add(new Design(50, 50, 3, 2.95, 2.3, 1.32, 3.68, 6.9, 6.9, 11.1, 11.1, 2.8, 2.8, 1.53, 1.94, 0.97, 1.9, 4.1, 0.97));
            ISA_Angles.add(new Design(40, 40, 4, 3.07, 2.4, 1.12, 2.88, 4.5, 4.5, 7.1, 7.1, 1.8, 1.8, 1.21, 1.53, 0.77, 1.6, 2.6, 0.77));
            ISA_Angles.add(new Design(35, 35, 5, 3.27, 2.6, 1.04, 2.46, 3.5, 3.5, 5.6, 5.6, 1.5, 1.5, 1.04, 1.31, 0.67, 1.4, 2.1, 0.67));
            ISA_Angles.add(new Design(45, 45, 4, 3.47, 2.7, 1.25, 3.25, 6.5, 6.5, 10.4, 10.4, 2.6, 2.6, 1.37, 1.73, 0.87, 2.0, 3.8, 0.87));
            ISA_Angles.add(new Design(40, 40, 5, 3.78, 3.0, 1.16, 2.84, 5.4, 5.4, 8.6, 8.6, 2.2, 2.2, 1.2, 1.51, 0.77, 1.9, 3.2, 0.77));
            ISA_Angles.add(new Design(35, 35, 6, 3.86, 3.0, 1.08, 2.42, 4.1, 4.1, 6.5, 6.5, 1.7, 1.7, 1.03, 1.29, 0.67, 1.7, 2.4, 0.67));
            ISA_Angles.add(new Design(50, 50, 4, 3.88, 3.0, 1.37, 3.63, 9.1, 9.1, 14.5, 14.5, 3.6, 3.6, 1.53, 1.93, 0.97, 2.5, 5.3, 0.97));
            ISA_Angles.add(new Design(45, 45, 5, 4.28, 3.4, 1.29, 3.21, 7.9, 7.9, 12.6, 12.6, 3.2, 3.2, 1.36, 1.72, 0.87, 2.5, 4.6, 0.87));
            ISA_Angles.add(new Design(40, 40, 6, 4.47, 3.5, 1.2, 2.8, 6.3, 6.3, 10.0, 10.0, 2.6, 2.6, 1.19, 1.5, 0.77, 2.3, 3.7, 0.77));
            ISA_Angles.add(new Design(50, 50, 5, 4.79, 3.8, 1.41, 3.59, 11.0, 11.0, 17.6, 17.6, 4.5, 4.5, 1.52, 1.92, 0.97, 3.1, 6.5, 0.97));
            ISA_Angles.add(new Design(45, 45, 6, 5.07, 4.0, 1.33, 3.17, 9.2, 9.2, 14.6, 14.6, 3.8, 3.8, 1.35, 1.7, 0.87, 2.9, 5.4, 0.87));
            ISA_Angles.add(new Design(55, 55, 5, 5.27, 4.1, 1.53, 3.97, 14.7, 14.7, 23.5, 23.5, 5.9, 5.9, 1.67, 2.11, 1.06, 3.7, 8.6, 1.06));
            ISA_Angles.add(new Design(50, 50, 6, 5.68, 4.5, 1.45, 3.55, 12.9, 12.9, 20.6, 20.6, 5.3, 5.3, 1.51, 1.9, 0.96, 3.6, 7.6, 0.96));
            ISA_Angles.add(new Design(60, 60, 5, 5.75, 4.5, 1.65, 4.35, 19.2, 19.2, 30.6, 30.6, 7.7, 7.7, 1.82, 2.31, 1.16, 4.4, 11.3, 1.16));
            ISA_Angles.add(new Design(65, 65, 5, 6.25, 4.9, 1.77, 4.73, 24.7, 24.7, 39.4, 39.4, 9.9, 9.9, 1.99, 2.51, 1.26, 5.2, 14.5, 0.96));
            ISA_Angles.add(new Design(55, 55, 6, 6.26, 4.9, 1.57, 3.93, 17.3, 17.3, 27.5, 27.5, 7.0, 7d, 1.66, 2.1, 1.06, 4.4, 10.1, 1.16));
            ISA_Angles.add(new Design(70, 70, 5, 6.77, 5.3, 1.89, 5.11, 31.1, 31.1, 49.8, 49.8, 12.5, 12.5, 2.15, 2.71, 1.36, 6.1, 18.4, 1.26));
            ISA_Angles.add(new Design(60, 60, 6, 6.84, 5.4, 1.69, 4.31, 22.6, 22.6, 36d, 36d, 9.1, 9.1, 1.82, 2.29, 1.15, 5.2, 13.3, 1.06));
            ISA_Angles.add(new Design(75, 75, 5, 7.27, 5.7, 2.02, 5.48, 38.7, 38.7, 61.9, 61.9, 15.5, 15.5, 2.31, 2.92, 1.46, 7.1, 22.8, 1.36));
            ISA_Angles.add(new Design(65, 65, 6, 7.44, 5.8, 1.81, 4.69, 29.1, 29.1, 46.5, 46.5, 11.7, 11.7, 1.98, 2.5, 1.26, 6.2, 17.2, 1.15));
            ISA_Angles.add(new Design(70, 70, 6, 8.06, 6.3, 1.94, 5.06, 36.8, 36.8, 58.8, 58.8, 14.8, 14.8, 2.14, 2.7, 1.36, 7.3, 21.7,1.36));
            ISA_Angles.add(new Design(55, 55, 8, 8.18, 6.4, 1.65, 3.85, 22d, 22d, 34.9, 34.9, 9.1, 9.1, 1.64, 2.07, 1.06, 5.7, 12.8,1.06));
            ISA_Angles.add(new Design(75, 75, 6, 8.66, 6.8, 2.06, 5.44, 45.7, 45.7, 73.1, 73.1, 18.4, 18.4, 2.3, 2.91, 1.46, 8.4, 27d, 1.46));
            ISA_Angles.add(new Design(60, 60, 8, 8.96, 7d, 1.77, 4.23, 29d, 29d, 46d, 46d, 11.9, 11.9, 1.8, 2.27, 1.15, 6.8, 16.9, 1.15));
            ISA_Angles.add(new Design(80, 80, 6, 9.29, 7.3, 2.18, 5.28, 56d, 56d, 89.6, 89.6, 22.5, 22.5, 2.46, 3.11, 1.56, 9.6, 33d, 1.56));
            ISA_Angles.add(new Design(65, 65, 8, 9.76, 7.7, 1.89, 4.61, 37.4, 37.4, 59.5, 59.5, 15.3, 15.3, 1.96, 2.47, 1.25, 8.1, 22d, 1.25));
            ISA_Angles.add(new Design(55, 55, 10, 10.02, 7.9, 1.72, 3.78, 26.3, 26.3, 41.5, 41.5, 11.2, 11.2, 1.62, 2.03, 1.06, 7d, 15.1, 1.06));
            ISA_Angles.add(new Design(90, 90, 6, 10.47, 8.2, 2.42, 6.58, 80.1, 80.1, 128.1, 128.1, 32d, 32d, 2.77, 3.5, 1.75, 12.2, 47.2, 1.75));
            ISA_Angles.add(new Design(70, 70, 8, 10.58, 8.3, 2.02, 4.98, 47.4, 47.4, 75.5, 75.5, 19.3, 19.3, 2.12, 2.67, 1.35, 9.5, 27.9, 1.35));
            ISA_Angles.add(new Design(60, 60, 10, 11d, 8.6, 1.85, 4.15, 34.8, 34.8, 54.9, 54.9, 14.6, 14.6, 1.78, 2.23, 1.15, 8.4, 20.1, 1.15));
            ISA_Angles.add(new Design(75, 75, 8, 11.38, 8.9, 2.14, 5.36, 59d, 59d, 94.1, 94.1, 24d, 24d, 2.28, 2.88, 1.45, 11d, 34.8, 1.45));
            ISA_Angles.add(new Design(100, 100, 6, 11.67, 9.2, 2.67, 7.33, 111.3, 111.3, 178.1, 178.1, 44.5, 44.5, 3.09, 3.91, 1.95, 15.2, 65.7, 1.95));
            ISA_Angles.add(new Design(65, 65, 10, 12d, 9.4, 1.97, 4.53, 45d, 45d, 71.3, 71.3, 18.8, 18.8, 1.94, 2.44, 1.25, 9.9, 26.2, 1.25));
            ISA_Angles.add(new Design(80, 80, 8, 12.21, 9.6, 2.27, 5.73, 72.5, 72.5, 115.6, 115.6, 29.4, 29.4, 2.44, 3.08, 1.55, 12.6, 42.7, 1.55));
            ISA_Angles.add(new Design(70, 70, 10, 13.02, 10.2, 2.1, 4.9, 57.2, 57.2, 90.7, 90.7, 23.7, 23.7, 2.1, 2.64, 1.35, 11.7, 33.3, 1.35));
            ISA_Angles.add(new Design(90, 90, 8, 13.79, 10.8, 2.51, 6.49, 104.2, 104.2, 166.4, 166.4, 42d, 42d, 2.75, 3.47, 1.75, 16d, 61.5, 1.75));
            ISA_Angles.add(new Design(75, 75, 10, 14.02, 11d, 2.22, 5.28, 71.4, 71.4, 113.3, 113.3, 29.4, 29.4, 2.26, 2.84, 1.45, 13.5, 41.7, 1.45));
            ISA_Angles.add(new Design(80, 80, 10, 15.05, 11.8, 2.34, 5.66, 87.7, 87.7, 139.5, 139.5, 36d, 36d, 2.41, 3.04, 1.55, 15.5, 51.4, 1.55));
            ISA_Angles.add(new Design(100, 100, 8, 15.39, 12.1, 2.76, 7.24, 145.1, 145.1, 231.8, 231.8, 58.4, 58.4, 3.07, 3.88, 1.95, (double) 20, 85.8, 1.95));
            ISA_Angles.add(new Design(110, 110, 8, 17.02, 13.4, 3d, 8d, 195d, 195d, 311.7, 311.7, 78.2, 78.2, 3.38, 4.28, 2.14, 24.4, 115.1, 2.14));
            ISA_Angles.add(new Design(90, 90, 10, 17.03, 13.4, 2.59, 6.41, 126.7, 126.7, 201.9, 201.9, 51.6, 51.6, 2.73, 3.44, 1.74, 19.8, 74.5, 1.74));
            ISA_Angles.add(new Design(80, 80, 12, 17.81, 14d, 2.42, 5.58, 101.9, 101.9, 161.4, 161.4, 42.4, 42.4, 2.39, 3.01, 1.54, 18.3, 59.2, 1.54));
            ISA_Angles.add(new Design(100, 100, 10, 19.03, 14.9, 2.84, 7.16, 177d, 177d, 282.2, 282.2, 71.8, 71.8, 3.05, 3.85, 1.94, 24.7, 104.4, 1.94));
            ISA_Angles.add(new Design(90, 90, 12, 20.19, 15.8, 2.66, 6.34, 147.9, 147.9, 234.9, 234.9, 60.9, 60.9, 2.71, 3.41, 1.74, 23.3, 86.5, 1.74));
            ISA_Angles.add(new Design(130, 130, 8, 20.22, 15.9, 3.5, 9.5, 328.3, 328.3, 525.1, 525.1, 131.4, 131.4, 4.03, 5.1, 2.55, 34.5, 194.2, 2.55));
            ISA_Angles.add(new Design(110, 110, 10, 21.06, 16.5, 3.08, 7.92, 238.4, 238.4, 380.5, 380.5, 96.3, 96.3, 3.36, 4.25, 2.14, 30.1, 140.6, 2.14));
            ISA_Angles.add(new Design(100, 100, 12, 22.59, 17.7, 2.92, 7.08, 207d, 207d, 329.3, 329.3, 84.7, 84.7, 3.03, 3.82, 1.94, 29.2, 121.6,1.94));
            ISA_Angles.add(new Design(110, 110, 12, 25.02, 19.6, 3.16, 7.84, 279.6, 279.6, 445.3, 445.3, 113.8, 113.8, 3.34, 4.22, 2.13, 35.7, 164.5, 2.13));
            ISA_Angles.add(new Design(130, 130, 10, 25.06, 19.7, 3.58, 9.42, 402.7, 402.7, 643.4, 643.4, 162.1, 162.1, 4.01, 5.07, 2.54, 42.7, 238.3, 2.54));
            ISA_Angles.add(new Design(150, 150, 10, 29.03, 22.8, 4.06, 10.94, 622.4, 622.4, 995.4, 995.4, 249.4, 249.4, 4.63, 5.86, 2.93, 56.9, 368.2, 2.93));
            ISA_Angles.add(new Design(130, 130, 12, 29.82, 23.4, 3.66, 9.34, 473.8, 473.8, 755.9, 755.9, 191.8, 191.8, 3.99, 5.03, 2.54, 50.7, 279.9, 2.54));
            ISA_Angles.add(new Design(110, 110, 15, 30.81, 24.2, 3.27, 7.73, 337.4, 337.4, 535.4, 535.4, 139.3, 139.3, 3.31, 4.17, 2.13, 43.7, 197d, 2.13));
            ISA_Angles.add(new Design(150, 150, 12, 34.59, 27.2, 4.14, 10.86, 735.4, 735.4, 1174.8, 1174.8, 296d, 296d, 4.61, 5.83, 2.93, 67.7, 435d, 2.93));
            ISA_Angles.add(new Design(130, 130, 15, 36.81, 28.9, 3.78, 9.22, 574.6, 574.6, 914.2, 914.2, 235d, 235d, 3.95, 4.98, 2.53, 62.3, 337.8, 2.53));
            ISA_Angles.add(new Design(150, 150, 15, 42.78, 33.6, 4.26, 10.74, 896.8, 896.8, 1429.7, 1429.7, 363.8, 363.8, 4.58, 5.78, 2.92, 83.5, 529.1, 2.92));
            ISA_Angles.add(new Design(200, 200, 12, 46.61, 36.6, 5.36, 14.64, 1788.9, 1788.9, 2862d, 2862d, 715.9, 715.9, 6.2, 7.84, 3.92, 122.2, 1058.9, 3.92));
            ISA_Angles.add(new Design(150, 150, 18, 50.79, 39.9, 4.38, 10.62, 1048d, 1048d, 1668.2, 1668.2, 429.5, 429.5, 4.54, 5.73, 2.91, 98.7, 616d, 2.91));
            ISA_Angles.add(new Design(200, 200, 15, 57.8, 45.4, 5.49, 14.51, 2197.7, 2197.7, 3511.8, 3511.8, 883.7, 883.7, 6.17, 7.79, 3.91, 151.4, 1301.2, 3.91));
            ISA_Angles.add(new Design(200, 200, 18, 68.81, 54d, 5.61, 14.39, 2588.7, 2588.7, 4130.8, 4130.8, 1046.5, 1046.5, 6.13, 7.75, 3.9, 179.9, 1530.5, 3.9));
            ISA_Angles.add(new Design(200, 200, 25, 93.8, 73.6, 5.88, 14.12, 3436d, 3436d, 5460.9, 5460.9, 1411.6, 1411.6, 6.05, 7.63, 3.88, 243.3, 2015.7, 3.88));

        } else {
            ISA_Angles.add(new Design(30, 20, 3, 1.41, 1.1, 0.98, 0.49, 2.02, 1.51, 1.2, 0.4, 1.4, 0.2, 0.92, 0.54, 0.99, 0.41, 0.6, 0.3, 0.4, 0.41));
            ISA_Angles.add(new Design(30, 20, 4, 1.84, 1.4, 1.02, 0.53, 1.98, 1.47, 1.5, 0.5, 1.8, 0.3, 0.92, 0.54, 0.98, 0.41, 0.8, 0.4, 0.5, 0.41));
            ISA_Angles.add(new Design(40, 25, 3, 1.88, 1.5, 1.3, 0.57, 2.7, 1.93, 3d, 0.9, 3.3, 0.5, 1.25, 0.68, 1.33, 0.52, 1.1, 0.5, 0.9, 0.52));
            ISA_Angles.add(new Design(45, 30, 3, 2.18, 1.7, 1.42, 0.69, 3.08, 2.31, 4.4, 1.5, (double) 5, 0.9, 1.42, 0.84, 1.52, 0.63, 1.4, 0.7, 1.5, 0.63));
            ISA_Angles.add(new Design(30, 20, 5, 2.25, 1.8, 1.06, 0.57, 1.94, 1.43, 1.9, 0.6, 2.1, 0.4, 0.91, 0.53, 0.97, 0.41, 1d, 0.4, 0.6, 0.41));
            ISA_Angles.add(new Design(50, 30, 3, 2.34, 1.8, 1.63, 0.65, 3.37, 2.35, 5.9, 1.6, 6.5, 1d, 1.59, 0.82, 1.67, 0.65, 1.7, 0.7, 1.7, 0.65));
            ISA_Angles.add(new Design(40, 25, 4, 2.46, 1.9, 1.35, 0.62, 2.65, 1.88, 3.8, 1.1, 4.3, 0.7, 1.25, 0.68, 1.32, 0.52, 1.4, 0.6, 1.2, 0.52));
            ISA_Angles.add(new Design(45, 30, 4, 2.86, 2.2, 1.47, 0.73, 3.03, 2.27, 5.7, 2d, 6.5, 1.1, 1.41, 0.84, 1.51, 0.63, 1.9, 0.9, 1.9, 0.63));
            ISA_Angles.add(new Design(40, 25, 5, 3.02, 2.4, 1.39, 0.66, 2.61, 1.84, 4.6, 1.4, 5.1, 0.8, 1.24, 0.67, 1.31, 0.52, 1.8, 0.7, 1.4, 0.52));
            ISA_Angles.add(new Design(50, 30, 4, 3.07, 2.4, 1.68, 0.7, 3.33, 2.3, 7.7, 2.1, 8.5, 1.2, 1.58, 0.82, 1.66, 0.63, 2.3, 0.9, 2.3, 0.63));
            ISA_Angles.add(new Design(45, 30, 5, 3.52, 2.8, 1.51, 0.77, 2.99, 2.23, 6.9, 2.4, 7.9, 1.4, 1.4, 0.83, 1.5, 0.63, 2.3, 1.1, 2.3, 0.63));
            ISA_Angles.add(new Design(40, 25, 6, 3.56, 2.8, 1.43, 0.69, 2.57, 1.81, 5.4, 1.6, 5.9, 1d, 1.23, 0.66, 1.29, 0.52, 2.1, 0.9, 1.6, 0.52));
            ISA_Angles.add(new Design(50, 30, 5, 3.78, 3d, 1.72, 0.74, 3.28, 2.26, 9.3, 2.5, 10.3, 1.5, 1.57, 0.81, 1.65, 0.63, 2.8, 1.1, 2.7, 0.63));
            ISA_Angles.add(new Design(45, 30, 6, 4.16, 3.3, 1.55, 0.81, 2.95, 2.19, 8d, 2.8, 9.2, 1.7, 1.39, 0.82, 1.49, 0.63, 2.7, 1.3, 2.7, 0.63));
            ISA_Angles.add(new Design(50, 30, 6, 4.47, 3.5, 1.76, 0.78, 3.24, 2.22, 10.9, 2.9, 11.9, 1.8, 1.56, 0.8, 1.64, 0.63, 3.4, 1.3, 3.1, 0.63));
            ISA_Angles.add(new Design(60, 40, 5, 4.76, 3.7, 1.95, 0.96, 4.05, 3.04, 16.9, 6d, 19.5, 3.4, 1.89, 1.12, 2.02, 0.85, 4.2, 2d, 5.8, 0.85));
            ISA_Angles.add(new Design(65, 45, 5, 5.26, 4.1, 2.07, 1.08, 4.43, 3.42, 22.1, 8.6, 25.9, 4.8, 2.05, 1.28, 2.22, 0.96, 5d, 2.5, 8d, 0.96));
            ISA_Angles.add(new Design(70, 45, 5, 5.52, 4.3, 2.27, 1.04, 4.73, 3.46, 27.2, 8.8, 30.9, 5.1, 2.22, 1.26, 2.36, 0.96, 5.7, 2.5, 8.9, 0.96));
            ISA_Angles.add(new Design(60, 40, 6, 5.65, 4.4, 1.99, 1d, 4.01, 3d, 19.9, 7d, 22.8, 4d, 1.88, 1.11, 2.01, 0.85, 5d, 2.3, 6.8, 0.85));
            ISA_Angles.add(new Design(75, 50, 5, 6.02, 4.7, 2.39, 1.16, 5.11, 3.84, 34.1, 12.2, 39.4, 6.9, 2.38, 1.42, 2.56, 1.07, 6.7, 3.2, 11.8, 1.07));
            ISA_Angles.add(new Design(65, 45, 6, 6.25, 4.9, 2.11, 1.12, 4.39, 3.38, 26d, 10.1, 30.4, 5.7, 2.04, 1.27, 2.21, 0.95, 5.9, 3d, 9.4, 0.95));
            ISA_Angles.add(new Design(80, 50, 5, 6.27, 4.9, 2.6, 1.12, 5.4, 3.88, 40.6, 12.3, 45.7, 7.2, 2.55, 1.4, 2.7, 1.07, 7.5, 3.2, 12.9, 1.07));
            ISA_Angles.add(new Design(70, 45, 6, 6.56, 5.2, 2.32, 1.09, 4.68, 3.41, 32d, 10.3, 36.3, 6d, 2.21, 1.25, 2.35, 0.96, 6.8, 3d, 10.5, 0.96));
            ISA_Angles.add(new Design(75, 50, 6, 7.16, 5.6, 2.44, 1.2, 5.06, 3.8, 40.3, 14.3, 46.4, 8.2, 2.37, 1.41, 2.55, 1.07, 8d, 3.8, 13.9, 1.07));
            ISA_Angles.add(new Design(60, 40, 8, 7.37, 5.8, 2.07, 1.08, 3.93, 2.92, 25.4, 8d, 29d, 5.2, 1.86, 1.1, 1.98, 0.84, 6.5, 3d, 8.5, 0.84));
            ISA_Angles.add(new Design(80, 50, 6, 7.46, 5.9, 2.64, 1.16, 5.36, 3.84, 48d, 14.4, 53.9, 8.5, 2.54, 1.39, 2.69, 1.07, 9d, 3.8, 15.2, 1.07));
            ISA_Angles.add(new Design(65, 45, 8, 8.17, 6.4, 2.19, 1.2, 4.31, 3.3, 33.2, 12.8, 38.7, 7.4, 2.02, 1.25, 2.18, 0.95, 7.7, 3.9, 11.8, 0.95));
            ISA_Angles.add(new Design(70, 45, 8, 8.58, 6.7, 2.4, 1.16, 4.6, 3.34, 41d, 13.1, 46.3, 7.8, 2.19, 1.24, 2.32, 0.95, 8.9, 3.9, 13.2, 0.95));
            ISA_Angles.add(new Design(90, 60, 6, 8.65, 6.8, 2.87, 1.39, 6.13, 4.61, 70.6, 25.2, 81.5, 14.3, 2.86, 1.71, 3.07, 1.28, 11.5, 5.5, 24.5, 1.28));
            ISA_Angles.add(new Design(75, 50, 8, 9.38, 7.4, 2.52, 1.28, 4.98, 3.72, 51.8, 18.3, 59.4, 10.6, 2.35, 1.4, 2.52, 1.06, 10.4, 4.9, 17.7, 1.06));
            ISA_Angles.add(new Design(100, 65, 6, 9.55, 7.5, 3.19, 1.47, 6.81, 5.03, 96.7, 32.4, 110.6, 18.6, 3.18, 1.84, 3.4, 1.39, 14.2, 6.4, 32.5, 1.39));
            ISA_Angles.add(new Design(80, 50, 8, 9.78, 7.7, 2.72, 1.24, 5.27, 3.76, 61.9, 18.5, 69.3, 11d, 2.52, 1.37, 2.66, 1.06, 11.7, 4.9, 19.3, 1.06));
            ISA_Angles.add(new Design(100, 75, 6, 10.14, 8d, 3.01, 1.78, 6.99, 5.72, 100.9, 48.7, 124d, 25.6, 3.15, 2.19, 3.5, 1.59, 14.4, 8.5, 41d, 1.59));
            ISA_Angles.add(new Design(70, 45, 10, 10.52, 8.3, 2.48, 1.24, 4.52, 3.26, 49.3, 15.6, 55.4, 9.5, 2.16, 1.22, 2.29, 0.95, 10.9, 4.8, 15.5, 0.95));
            ISA_Angles.add(new Design(90, 60, 8, 11.37, 8.9, 2.96, 1.48, 6.04, 4.52, 91.5, 32.4, 105.3, 18.6, 2.84, 1.69, 3.04, 1.28, 15.1, 7.2, 31.5, 1.28));
            ISA_Angles.add(new Design(75, 50, 10, 11.52, 9d, 2.6, 1.36, 4.9, 3.64, 62.3, 21.8, 71.2, 12.9, 2.33, 1.38, 2.49, 1.06, 12.7, 6d, 20.9, 1.06));
            ISA_Angles.add(new Design(125, 75, 6, 11.66, 9.2, 4.05, 1.59, 8.45, 5.91, 187.8, 51.6, 208.9, 30.5, 4.01, 2.1, 4.23, 1.62, 22.2, 8.7, 56.7, 1.62));
            ISA_Angles.add(new Design(80, 50, 10, 12.02, 9.4, 2.81, 1.32, 5.19, 3.68, 74.7, 22.1, 83.3, 13.5, 2.49, 1.36, 2.63, 1.06, 14.4, 6d, 22.9, 1.06));
            ISA_Angles.add(new Design(100, 65, 8, 12.57, 9.9, 3.28, 1.55, 6.72, 4.93, 125.9, 41.9, 143.6, 24.2, 3.16, 1.83, 3.38, 1.39, 18.7, 8.5, 42d, 1.39));
            ISA_Angles.add(new Design(125, 95, 6, 12.86, 10.1, 3.7, 2.22, 8.8, 7.28, 203.2, 102.1, 252.3, 52.9, 3.97, 2.82, 4.43, 2.03, 23.1, 14d, 84.5, 2.03));
            ISA_Angles.add(new Design(100, 75, 8, 13.36, 10.5, 3.1, 1.87, 6.9, 5.63, 131.6, 63.3, 161.3, 33.6, 3.14, 2.18, 3.48, 1.59, 19.1, 11.2, 53.4, 1.59));
            ISA_Angles.add(new Design(90, 60, 10, 14.01, 11d, 3.04, 1.55, 5.96, 4.45, 110.9, 39.1, 127.3, 22.8, 2.81, 1.67, 3.01, 1.27, 18.6, 8.8, 37.8, 1.27));
            ISA_Angles.add(new Design(125, 75, 8, 15.38, 12.1, 4.15, 1.68, 8.35, 5.82, 245.5, 67.2, 272.8, 40d, 4d, 2.09, 4.21, 1.61, 29.4, 11.5, 74d, 1.61));
            ISA_Angles.add(new Design(100, 65, 10, 15.51, 12.2, 3.37, 1.63, 6.63, 4.87, 153.2, 50.7, 174.2, 29.7, 3.14, 1.81, 3.35, 1.38, 23.1, 10.4, 50.7, 1.38));
            ISA_Angles.add(new Design(100, 75, 10, 16.5d, 13d, 3.19, 1.95, 6.81, 5.55, 160.4, 76.9, 196.1, 41.2, 3.12, 2.16, 3.45, 1.58, 23.6, 13.8, 64.7, 1.58));
            ISA_Angles.add(new Design(90, 60, 12, 16.57, 13d, 3.12, 1.63, 5.88, 4.37, 129.1, 45.2, 147.5, 26.8, 2.79, 1.65, 2.98, 1.27, 22d, 10.3, 43.3, 1.27));
            ISA_Angles.add(new Design(125, 95, 8, 16.98, 13.3, 3.8, 2.31, 8.7, 7.19, 266d, 133.3, 329.7, 69.6, 3.96, 2.8, 4.41, 2.02, 30.6, 18.5, 110.6, 2.02));
            ISA_Angles.add(new Design(150, 75, 8, 17.42, 13.7, 5.23, 1.53, 9.77, 5.97, 407.2, 70.2, 432.8, 44.5, 4.83, 2.01, 4.98, 1.6, 41.7, 11.8, 95.5, 1.6));
            ISA_Angles.add(new Design(125, 75, 10, 19.02, 14.9, 4.24, 1.76, 8.26, 5.74, 300.3, 81.6, 332.9, 49.1, 3.97, 2.07, 4.18, 1.61, 36.3, 14.2, 89.9, 1.61));
            ISA_Angles.add(new Design(100, 75, 12, 19.56, 15.4, 3.27, 2.03, 6.73, 5.47, 187.5, 89.5, 228.4, 48.6, 3.1, 2.14, 3.42, 1.58, 27.9, 16.3, 74.9, 1.58));
            ISA_Angles.add(new Design(150, 115, 8, 20.58, 16.2, 4.46, 2.73, 10.54, 8.77, 465.7, 238.9, 581.2, 123.3, 4.76, 3.41, 5.31, 2.45, 44.2, 27.2, 195.9, 2.45));
            ISA_Angles.add(new Design(125, 95, 10, 21.02, 16.5, 3.88, 2.39, 8.62, 7.11, 325.8, 162.7, 402.9, 85.6, 3.94, 2.78, 4.38, 2.02, 37.8, 22.9, 135d, 2.02));
            ISA_Angles.add(new Design(150, 75, 10, 21.56, 16.9, 5.23, 1.61, 9.68, 5.89, 499.1, 85.3, 529.8, 54.6, 4.81, 1.99, 4.96, 1.59, 51.6, 14.5, 116.2, 1.59));
            ISA_Angles.add(new Design(125, 95, 12, 24.98, 19.6, 3.96, 2.47, 8.54, 7.03, 382.6, 190.4, 472d, 101d, 3.91, 2.76, 4.35, 2.01, 44.8, 27.1, 157.7, 2.01));
            ISA_Angles.add(new Design(150, 115, 10, 25.52, 20d, 4.55, 2.82, 10.45, 8.68, 573.3, 293.4, 714.3, 152.4, 4.74, 3.39, 5.29, 2.44, 54.9, 33.8, 241d, 2.44));
            ISA_Angles.add(new Design(150, 75, 12, 25.62, 20.1, 5.41, 1.69, 9.59, 5.81, 587d, 99.5, 622.2, 64.3, 4.79, 1.97, 4.93, 1.58, 61.2, 17.1, 135.2, 1.58));
            ISA_Angles.add(new Design(200, 100, 10, 29.03, 22.8, 6.96, 2.01, 13.04, 7.99, 1210d, 209.2, 1286.7, 132.5, 6.46, 2.68, 6.66, 2.14, 92.8, 26.2, 284.8, 2.14));
            ISA_Angles.add(new Design(150, 115, 12, 30.38, 23.8, 4.64, 2.9, 10.36, 8.6, 676.5, 345.3, 841.4, 180.4, 4.72, 3.37, 5.26, 2.44, 65.3, 40.2, 283.6, 2.44));
            ISA_Angles.add(new Design(200, 150, 10, 34d, 26.7, 5.99, 3.51, 14.01, 11.49, 1377.9, 669.6, 1696.6, 350.8, 6.37, 4.44, 7.06, 3.21, 98.3, 58.3, 564.1, 3.21));
            ISA_Angles.add(new Design(200, 100, 12, 34.59, 27.2, 7.05, 2.1, 12.95, 7.9, 1431.7, 246.2, 1521d, 156.8, 6.43, 2.67, 6.63, 2.13, 110.6, 31.1, 335.3, 2.13));
            ISA_Angles.add(new Design(150, 115, 15, 37.52, 29.5, 4.76, 3.02, 10.24, 8.48, 823.5, 418.6, 1020.9, 221.2, 4.69, 3.34, 5.22, 2.43, 80.4, 49.4, 342.8, 2.43));
            ISA_Angles.add(new Design(200, 150, 12, 40.56, 31.8, 6.08, 3.6, 13.92, 11.4, 1634.9, 793.2, 2010.8, 417.2, 6.35, 4.42, 7.04, 3.21, 117.4, 69.6, 669.1, 3.21));
            ISA_Angles.add(new Design(200, 100, 15, 42.78, 33.6, 7.18, 2.22, 12.82, 7.78, 1750.5, 298.1, 1856.7, 191.9, 6.4, 2.64, 6.59, 2.12, 136.5, 38.3, 405.4, 2.12));
            ISA_Angles.add(new Design(200, 150, 15, 50.25, 39.4, 6.2, 3.72, 13.8, 11.28, 2005.6, 969.9, 2461.9, 513.6, 6.32, 4.39, 7d, 3.2, 145.4, 86d, 818.5, 3.2));
            ISA_Angles.add(new Design(200, 150, 18, 59.76, 46.9, 6.33, 3.84, 13.67, 11.16, 2359.4, 1136.9, 2889.5, 606.9, 6.28, 4.36, 6.95, 3.19, 172.5, 101.9, 958.1, 3.19));
        }
    }

    void make_design() {
//        Prompt to fill all values before continuing operation
        if(!updateValues()) {
            Toast.makeText(getApplicationContext(),  "Insert All Values.", Toast.LENGTH_SHORT).show();
            return;
        }

        factoredLoad = factoredLoad/2;

        double minAreaRequired = (factoredLoad * gamma_m0) / steelYieldStress;
        double strengthOfWeldPerUnitLength = (throatThickness * ultimateTensileStress) / (1.732d * customSafetyFactor);
        double lengthOfWeld = (factoredLoad)/(strengthOfWeldPerUnitLength);

//        We put a counter variable to traverse all the json objects belonging to the equal / unequal section
        for (int counter = 0; counter < ISA_Angles.size(); ++counter) {

            double areaOfAngle = ISA_Angles.get(counter).getSectionalArea() * 100;
            double angleLength = ISA_Angles.get(counter).getLength();
            double angleWidth = ISA_Angles.get(counter).getWidth();
            double angleThickness = ISA_Angles.get(counter).getThickness();
            double Cxx = ISA_Angles.get(counter).getCxx() * 10;
            double angleOfGyration = ISA_Angles.get(counter).getRzz() * 10;


            if (areaOfAngle >= minAreaRequired) {

//                Calculating the length of welds
                double lengthOfWeldUpper = Math.ceil(factoredLoad * Cxx / (strengthOfWeldPerUnitLength * angleLength));
                double lengthOfWeldLower = lengthOfWeld - lengthOfWeldUpper;

//                Yielding Strength
                double Tdg = areaOfAngle * steelYieldStress / gamma_m0;

//                Rupture Strength
                double alpha = 0.8d;
                double Anc = (angleLength - angleThickness/2) * angleThickness;
                double Ago = (angleWidth - angleThickness/2) * angleThickness;
                double An = Anc + Ago;
                double Tdn = alpha * An * ultimateTensileStress / gamma_m1;

//                Block Strength
                double Avg = Math.max(lengthOfWeldUpper, lengthOfWeldLower) * angleThickness * 2;
                double Avn = Math.max(lengthOfWeldUpper, lengthOfWeldLower) * angleThickness * 2;

                double Atn = angleLength * angleThickness;
                double Atg = angleLength * angleThickness;

                double Tdb1 = 0.9d * Avn * ultimateTensileStress / (1.732d * gamma_m1) + Atg * steelYieldStress / gamma_m0;
                double Tdb2 = Avg * steelYieldStress / (1.732d * gamma_m0) + (0.9d * Atn * ultimateTensileStress / gamma_m1);
                double Tdb = Math.min(Tdb1, Tdb2);


//                Taking Minimum Magnitude of possible failure scenarios
                double designStrengthOfAngle = Math.min(Math.min(Tdg, Tdb), Tdn);

                double lamda = lengthOfTensionMember / angleOfGyration;


                if (designStrengthOfAngle >= factoredLoad && lamda < allowableSlendernessRatio) {
                    String ansStatement = "ISA section " + ISA_Angles.get(counter).getLength() + " x " + ISA_Angles.get(counter).getWidth() + " x " + ISA_Angles.get(counter).getThickness() + " is termed suitable for the given load conditions.";

                    Intent intent = new Intent(getApplicationContext(), ResultShowcase.class);
                    intent.putExtra("results", ansStatement);
                    intent.putExtra("isBolt", false);
                    intent.putExtra("factored_load", "" + factoredLoad);
                    intent.putExtra("len_of_ten_member", "" + lengthOfTensionMember);
                    intent.putExtra("slenderness_ratio", "" + allowableSlendernessRatio);
                    intent.putExtra("gamma_m1", gamma_m1);
                    intent.putExtra("gamma_m0", gamma_m0);
                    intent.putExtra("gamma_mb", customSafetyFactor);
//                    intent.putExtra("diameter_of_bolt", boltDiameter);
//                    intent.putExtra("num_of_bolts", numberOfBolts);
                    intent.putExtra("throat_thickness", throatThickness);


                    startActivity(intent);
                    return;
                }
            }
        }

        String ansStatement = "No available ISA section can be termed suitable for the given load conditions.";
        Toast.makeText(this, ansStatement, Toast.LENGTH_SHORT).show();
    }
}