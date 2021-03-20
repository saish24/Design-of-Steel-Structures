package com.example.designofsteelstructures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DoubleAngle extends AppCompatActivity {
    private EditText et_Factored_Load, et_length_of_tension_member, et_allowable_slenderness_ratio, et_ultimate_tensile_stress, et_steel_yield_stress, et_gamma_m0, et_gamma_m1, et_bolt_ultimate_tensile_stress, et_bolt_diameter, et_pitch, et_end_distance, et_gamma_mb;
    private CheckBox cb_use_Fe_410_steel, cb_take_partial_safety_factors_wrt_table5, cb_use_min_value_acc_to_IS800, cb_take_gamma_mb_from_IS800, cb_connected_length_larger;
    private RadioButton rb_bolt_grade_4x6, rb_bolt_grade_8x8, rb_equal_section, rb_unequal_section;
    private Double factoredLoad, lengthOfTensionMember, allowableSlendernessRatio, ultimateTensileStress, steelYieldStress, gamma_m0, gamma_m1, boltUltimateTensileStress, boltDiameter, holeDiameter, Pitch, endDistance, gamma_mb;
    private ArrayList<Design> ISA_Angles = new ArrayList<>();
    public static final String TAG = "DoubleAngle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_angle_bolt);

        et_Factored_Load = findViewById(R.id.et_Factored_Load);
        et_length_of_tension_member = findViewById(R.id.et_len_of_tension_member);
        et_allowable_slenderness_ratio = findViewById(R.id.et_allowable_slenderness_ratio);
        et_ultimate_tensile_stress = findViewById(R.id.et_Ultimate_Tensile_Stress);
        et_steel_yield_stress = findViewById(R.id.et_Yield_Stress);
        et_gamma_m0 = findViewById(R.id.et_gamma_0);
        et_gamma_m1 = findViewById(R.id.et_gamma_1);
        et_bolt_ultimate_tensile_stress = findViewById(R.id.et_Ultimate_Tensile_Stress_bolt);
        et_bolt_diameter = findViewById(R.id.et_diameter_bolt);
        et_pitch = findViewById(R.id.et_pitch);
        et_end_distance = findViewById(R.id.et_end_dist);
        et_gamma_mb = findViewById(R.id.et_custom_safety_factor);

        cb_use_Fe_410_steel = findViewById(R.id.checkbox_Use_fe410);
        cb_take_partial_safety_factors_wrt_table5 = findViewById(R.id.checkbox_Use_IS800_table5_values);
        cb_use_min_value_acc_to_IS800 = findViewById(R.id.take_min_val_from_IS800);
        cb_take_gamma_mb_from_IS800 = findViewById(R.id.checkbox_Use_IS800_table5_values2);
        cb_connected_length_larger = findViewById(R.id.checkbox_connected_length_larger);

        rb_bolt_grade_4x6 = findViewById(R.id.radio_grade4_6);
        rb_bolt_grade_8x8 = findViewById(R.id.radio_grade8_8);
        rb_equal_section = findViewById(R.id.radio_equal_angle);
        rb_unequal_section = findViewById(R.id.radio_unequal_angle);

        cb_use_Fe_410_steel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_steel_yield_stress.setText(R.string.FE410_steel_yield_stress);
                et_steel_yield_stress.setTextColor(getResources().getColor((R.color.faded)));

                et_ultimate_tensile_stress.setText(R.string.FE410_ultimate_tensile_stress);
                et_ultimate_tensile_stress.setTextColor(getResources().getColor((R.color.faded)));
            } else {
                et_steel_yield_stress.setTextColor(Color.BLACK);
                et_ultimate_tensile_stress.setTextColor(Color.BLACK);
            }
        });

        cb_take_partial_safety_factors_wrt_table5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_gamma_m1.setText(R.string.gamma_m1);
                et_gamma_m0.setText(R.string.gamma_m0);

                et_gamma_m1.setTextColor(getResources().getColor((R.color.faded)));
                et_gamma_m0.setTextColor(getResources().getColor((R.color.faded)));
            } else {
                et_gamma_m1.setTextColor(Color.BLACK);
                et_gamma_m0.setTextColor(Color.BLACK);
            }
        });

        rb_bolt_grade_4x6.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_bolt_ultimate_tensile_stress.setText(R.string.bolt_46);
                et_bolt_ultimate_tensile_stress.setTextColor(getResources().getColor((R.color.faded)));
            } else if (rb_bolt_grade_8x8.isChecked()) {
                et_bolt_ultimate_tensile_stress.setText(R.string.bolt_88);
                et_bolt_ultimate_tensile_stress.setTextColor(getResources().getColor((R.color.faded)));
            } else {
                et_bolt_ultimate_tensile_stress.setTextColor(Color.BLACK);
            }
        });

        rb_bolt_grade_8x8.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_bolt_ultimate_tensile_stress.setText(R.string.bolt_88);
                et_bolt_ultimate_tensile_stress.setTextColor(getResources().getColor((R.color.faded)));
            } else if (rb_bolt_grade_4x6.isChecked()) {
                et_bolt_ultimate_tensile_stress.setText(R.string.bolt_46);
                et_bolt_ultimate_tensile_stress.setTextColor(getResources().getColor((R.color.faded)));
            } else {
                et_bolt_ultimate_tensile_stress.setTextColor(Color.BLACK);
            }
        });

        cb_use_min_value_acc_to_IS800.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String value = et_bolt_diameter.getText().toString();
                if (value.equals("")) return;
                double ans = 2.5 * (Double.parseDouble(value) + 2);
                value = ans + "";
                et_pitch.setText(value);
                et_pitch.setTextColor(getResources().getColor((R.color.faded)));

                double holeDia = 1.5 * (Double.parseDouble(et_bolt_diameter.getText().toString()) + 2);
                value = holeDia + "";
                et_end_distance.setText(value);
                et_end_distance.setTextColor(getResources().getColor((R.color.faded)));
            } else {
                et_pitch.setTextColor(Color.BLACK);
                et_end_distance.setTextColor(Color.BLACK);
            }
        });

        cb_take_gamma_mb_from_IS800.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                et_gamma_mb.setText(R.string.gamma_mb);
                et_gamma_mb.setTextColor(getResources().getColor((R.color.faded)));
            } else {
                et_gamma_mb.setTextColor(Color.BLACK);
            }
        });

//        rb_equal_section.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                } else if(rb_bolt_grade_8x8.isChecked()) {
//
//                } else {
//
//                }
//            }
//        });

//        rb_bolt_grade_8x8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                } else if(rb_bolt_grade_4x6.isChecked()) {
//
//                } else {
//
//                }
//            }
//        });

        TextView next = findViewById(R.id.btn_submit);
        next.setOnClickListener(v -> {
            readISAValues();
            make_design();
        });
    }

    private boolean updateValues() {
        if (cb_use_Fe_410_steel.isChecked()) {
            et_ultimate_tensile_stress.setText(R.string.FE410_ultimate_tensile_stress);
            et_steel_yield_stress.setText(R.string.FE410_steel_yield_stress);

            et_ultimate_tensile_stress.setTextColor(getResources().getColor((R.color.faded)));
            et_steel_yield_stress.setTextColor(getResources().getColor((R.color.faded)));
        }

        if (cb_take_partial_safety_factors_wrt_table5.isChecked()) {
            et_gamma_m1.setText(R.string.gamma_m1);
            et_gamma_m0.setText(R.string.gamma_m0);

            et_gamma_m1.setTextColor(getResources().getColor((R.color.faded)));
            et_gamma_m0.setTextColor(getResources().getColor((R.color.faded)));

        }

        if (rb_bolt_grade_4x6.isChecked()) {
            et_bolt_ultimate_tensile_stress.setText(R.string.bolt_46);
            et_bolt_ultimate_tensile_stress.setTextColor(getResources().getColor((R.color.faded)));
        } else if (rb_bolt_grade_8x8.isChecked()) {
            et_bolt_ultimate_tensile_stress.setText(R.string.bolt_88);
            et_bolt_ultimate_tensile_stress.setTextColor(getResources().getColor((R.color.faded)));
        }

        if (cb_use_min_value_acc_to_IS800.isChecked()) {
            cb_use_min_value_acc_to_IS800.performClick();
            cb_use_min_value_acc_to_IS800.performClick();

            et_pitch.setTextColor(getResources().getColor((R.color.faded)));
            et_end_distance.setTextColor(getResources().getColor((R.color.faded)));
        }

        if (cb_take_gamma_mb_from_IS800.isChecked()) {
            et_gamma_mb.setText(R.string.gamma_mb);
            et_gamma_mb.setTextColor(getResources().getColor((R.color.faded)));
        }

        String value = et_Factored_Load.getText().toString();
        if (value.equals("")) return false;
        factoredLoad = 1000 * Double.parseDouble(value);

        value = et_length_of_tension_member.getText().toString();
        if (value.equals("")) return false;
        lengthOfTensionMember = Double.parseDouble(value);


        value = et_allowable_slenderness_ratio.getText().toString();
        if (value.equals("")) return false;
        allowableSlendernessRatio = Double.parseDouble(value);

        value = et_ultimate_tensile_stress.getText().toString();
        if (value.equals("")) return false;
        ultimateTensileStress = Double.parseDouble(value);

        value = et_steel_yield_stress.getText().toString();
        if (value.equals("")) return false;
        steelYieldStress = Double.parseDouble(value);

        value = et_gamma_m0.getText().toString();
        if (value.equals("")) return false;
        gamma_m0 = Double.parseDouble(value);

        value = et_gamma_m1.getText().toString();
        if (value.equals("")) return false;
        gamma_m1 = Double.parseDouble(value);

        value = et_bolt_ultimate_tensile_stress.getText().toString();
        if (value.equals("")) return false;
        boltUltimateTensileStress = Double.parseDouble(value);

        value = et_bolt_diameter.getText().toString();
        if (value.equals("")) return false;
        boltDiameter = Double.parseDouble(value);
        holeDiameter = boltDiameter + 2;

        value = et_pitch.getText().toString();
        if (value.equals("")) return false;
        Pitch = Double.parseDouble(value);

        value = et_end_distance.getText().toString();
        if (value.equals("")) return false;
        endDistance = Double.parseDouble(value);

        value = et_gamma_mb.getText().toString();
        if (value.equals("")) return false;
        gamma_mb = Double.parseDouble(value);
        return true;
    }

    void readISAValues() {
        ISA_Angles.clear();

        if (rb_equal_section.isChecked()) {
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
        if (!updateValues()) {
            Toast.makeText(this, "Insert All Values.", Toast.LENGTH_SHORT).show();
            return;
        }

        double minAreaRequired = (factoredLoad * gamma_m0) / steelYieldStress;

//        We put a counter variable to traverse all the json objects belonging to the equal / unequal section
        for (int counter = 0; counter < ISA_Angles.size(); ++counter) {

            double areaOfAngle = ISA_Angles.get(counter).getSectionalArea() * 100d;
            int angleLength = ISA_Angles.get(counter).getLength();
            int angleWidth = ISA_Angles.get(counter).getWidth();
            int angleThickness = ISA_Angles.get(counter).getThickness();
            double Cxx = ISA_Angles.get(counter).getCxx() * 10;
            double angleOfGyration = ISA_Angles.get(counter).getRzz() * 10;
            String angleName = "ISA Section " + angleLength + " x " + angleWidth + " x " + angleThickness;

            if (2 * areaOfAngle >= minAreaRequired) {
//                Checking for Shear Strength
                double crossSectionalArea = 0.78 * 0.25 * 3.14 * (boltDiameter * boltDiameter);
                double boltShearStrength = boltUltimateTensileStress * crossSectionalArea / (1.73 * gamma_mb);

//                Bearing Strength
                double Kb = Math.min(Math.min(endDistance / (3 * holeDiameter), (Pitch / (3 * holeDiameter)) - 0.25), Math.min(boltUltimateTensileStress / ultimateTensileStress, 1d));
                double bearingStrengthOfBolt = (2.5d * Kb * boltDiameter * boltUltimateTensileStress * ISA_Angles.get(counter).getThickness()) / gamma_mb;

                int numberOfBolts = (int) Math.ceil(factoredLoad / Math.min(boltShearStrength, bearingStrengthOfBolt));
                int numberOfBoltsPerSection = (numberOfBolts%2 == 0) ? numberOfBolts/2 : (numberOfBolts/2 + 1);
                double alphaPerSection = (numberOfBoltsPerSection <= 2) ? 0.6 : (numberOfBoltsPerSection == 3) ? 0.7 : 0.8;

//                Yielding Strength
                double Tdg = 2 * steelYieldStress * areaOfAngle * 100 / gamma_m0;

//                Rupture Strength
                double Anc = (angleLength - angleThickness/2 - holeDiameter) * angleThickness;
                double Ago = (angleWidth - angleThickness/2) * angleThickness;
                double An = Anc + Ago;

                double Tdn = ultimateTensileStress * An * alphaPerSection / gamma_m1;

//                Block Shear
                double g = Math.ceil((angleLength - angleThickness)  / 2);
                double p = angleLength - g;
                double Avg = (((numberOfBoltsPerSection - 1) * Pitch) + endDistance) * angleThickness;
                double Avn = (((numberOfBoltsPerSection - 1) * Pitch) + endDistance - (numberOfBoltsPerSection - 0.5d) * holeDiameter) * angleThickness;

                double Atg = p * angleThickness;
                double Atn = (p - (0.5 * holeDiameter)) * angleThickness;

                double Tdb1 = 2 * ((0.9d * Avn * ultimateTensileStress) / (1.73d * gamma_m1)) + (Atg * steelYieldStress / gamma_m0);
                double Tdb2 = 2 * ((Avg * steelYieldStress) / (1.73d * gamma_m0)) + ((0.9d * Atn * ultimateTensileStress) / gamma_m1);
                double Tdb = Math.min(Tdb1, Tdb2);

//                Taking Minimum Magnitude of possible failure scenarios
                double designStrengthOfAngle = Math.min(Math.min(Tdg, Tdb), Tdn);

                double lambda = lengthOfTensionMember / angleOfGyration;

                if (designStrengthOfAngle >= factoredLoad && lambda < allowableSlendernessRatio) {
                    String ansStatement = "ISA section " + angleLength + " x " + ISA_Angles.get(counter).getWidth() + " x " + angleThickness + " is termed suitable for the given load conditions.";

                    Intent intent = new Intent(this, ResultShowcase.class);

                    intent.putExtra("results", ansStatement);
                    intent.putExtra("isBolt", true);
                    intent.putExtra("factored_load", "" + factoredLoad/1000);
                    intent.putExtra("len_of_ten_member", "" + lengthOfTensionMember);
                    intent.putExtra("slenderness_ratio", "" + allowableSlendernessRatio);
                    intent.putExtra("gamma_m1", "" + gamma_m1);
                    intent.putExtra("gamma_m0", "" + gamma_m0);
                    intent.putExtra("gamma_mb", "" + gamma_mb);
                    intent.putExtra("diameter_of_bolt", "" + boltDiameter);
                    intent.putExtra("num_of_bolts", "" + numberOfBolts);
                    intent.putExtra("", "" + (2 * numberOfBoltsPerSection));

                    this.startActivity(intent);
                    return;
                }
            }
        }

        String ansStatement = "No available ISA section can be termed suitable for the given load conditions.";
        Toast.makeText(this, ansStatement, Toast.LENGTH_SHORT).show();
    }
}