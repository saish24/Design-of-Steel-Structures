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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DoubleWeldAngle extends AppCompatActivity {
    private EditText et_Factored_Load_weld, et_length_of_tension_member_weld, et_allowable_slenderness_ratio_weld, et_throat_thickness, et_custom_safety_factor_weld, et_Ultimate_Tensile_Stress_weld, et_Yield_Stress_weld, et_gamma_0_weld, et_gamma_1_weld;
    private CheckBox cb_use_Fe_410_steel_weld, cb_take_partial_safety_factors_wrt_table5_weld, cb_connected_length_larger_weld;
    private RadioButton rb_on_sides_parallel_to_axis, rb_on_three_sides, rb_shop_weld, rb_equal_section_weld;
    private Double factoredLoad, lengthOfTensionMember, allowableSlendernessRatio, throatThickness, customSafetyFactor, ultimateTensileStress, steelYieldStress, gamma_m0, gamma_m1;
    private ArrayList<Design> ISA_Angles = new ArrayList<>();
    public static final String TAG = "DoubleWeld";

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
            readJsonFromFile("equal_angles.json");
        } else {
            readJsonFromFile("unequal_angles.json");
        }
    }
    private void readJsonFromFile(String fileName) {
        try {
            InputStream inputStream = getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i < jsonArray.length(); ++i) {
                Gson gson = new Gson();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Design design = gson.fromJson(jsonObject.toString(), Design.class);
                ISA_Angles.add(design);
            }

        } catch (Exception e) {
            Log.e(TAG, "readJsonFromFile: " + e.getMessage());
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