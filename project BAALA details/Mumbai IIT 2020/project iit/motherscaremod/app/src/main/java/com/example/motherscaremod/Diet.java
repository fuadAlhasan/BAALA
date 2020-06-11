package com.example.motherscaremod;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Diet extends AppCompatActivity {

    TextView calculated, calculated_tot;
    EditText weight, height, age, ricee, fishe, beefe, chickene, vegetablee, egge;
    RadioButton activity;
    Button calculate, calculate1;
    RadioGroup grp;

    double w, h, a, r, f, b, c, v, e, activity_factor, cal1, cal2, cal3, tot_cal;
    String we, he, ag, ri, fi, be, ch, ve, eg, radio, mod_str1, mod_str2, mod_str3, str_cal1, str_cal2, str_cal3, tot;

    String str1 = "In case of first trimester(week 1 to 12), calories ";
    String str2 = "In case of second trimester(week 12 to 2), calories ";
    String str3 = "In case of third trimester(week 27 to the end of pregnancy), calories ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet);

        weight = (EditText) findViewById(R.id.weight);
        height = (EditText) findViewById(R.id.height);
        age = (EditText) findViewById(R.id.age);
        grp = (RadioGroup) findViewById(R.id.grp);
        calculate = (Button) findViewById(R.id.button3);
        calculated = (TextView) findViewById(R.id.calculated);
        calculated_tot = (TextView) findViewById(R.id.calculated_tot);
        calculate1 = (Button) findViewById(R.id.button4);
        ricee = (EditText) findViewById(R.id.ricee);
        fishe = (EditText) findViewById(R.id.fishe);
        beefe = (EditText) findViewById(R.id.beefe);
        chickene = (EditText) findViewById(R.id.chickene);
        vegetablee = (EditText) findViewById(R.id.vegetablee);
        egge = (EditText) findViewById(R.id.egge);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                we = weight.getText().toString();
                he = height.getText().toString();
                ag = age.getText().toString();
                w = Double.parseDouble(we);
                h = Double.parseDouble(he);
                a = Double.parseDouble(ag);
                int selectedId = grp.getCheckedRadioButtonId();
                activity = (RadioButton) findViewById(selectedId);
                radio = activity.getText().toString();
                switch (radio) {
                    case "No activity":
                        activity_factor = 1;
                        break;
                    case "Moderate activity":
                        activity_factor = 1.5;
                        break;
                    case "High activity":
                        activity_factor = 2;
                        break;
                }
                cal1 = (1800 - (((10 * w) + (6.25 * h) + a - 161) * activity_factor));
                cal2 = (2200 - (((10 * w) + (6.25 * h) + a - 161) * activity_factor));
                cal3 = (2400 - (((10 * w) + (6.25 * h) + a - 161) * activity_factor));
                if (cal1 < 0 && cal2 < 0 && cal3 < 0) {
                    cal1 = (cal1 * (-1));
                    cal2 = (cal2 * (-1));
                    cal3 = (cal3 * (-1));
                    mod_str1 = "to lose: ";
                    mod_str2 = "to lose: ";
                    mod_str3 = "to lose: ";
                } else if (cal1 < 0 && cal2 < 0) {
                    cal1 = (cal1 * (-1));
                    cal2 = (cal2 * (-1));
                    cal3 = (cal3 * (1));
                    mod_str1 = "to lose: ";
                    mod_str2 = "to lose: ";
                    mod_str3 = "needed: ";
                } else if (cal2 < 0 && cal3 < 0) {
                    cal1 = (cal1 * (1));
                    cal2 = (cal2 * (-1));
                    cal3 = (cal3 * (1));
                    mod_str1 = "needed: ";
                    mod_str2 = "to lose: ";
                    mod_str3 = "to lose: ";
                } else if (cal1 < 0 && cal3 < 0) {
                    cal1 = (cal1 * (-1));
                    cal2 = (cal2 * (1));
                    cal3 = (cal3 * (-1));
                    mod_str1 = "to lose: ";
                    mod_str2 = "needed: ";
                    mod_str3 = "to lose: ";
                } else if (cal1 < 0) {
                    cal1 = (cal1 * (-1));
                    cal2 = (cal2 * (1));
                    cal3 = (cal3 * (1));
                    mod_str1 = "to lose: ";
                    mod_str2 = "needed: ";
                    mod_str3 = "needed: ";
                } else if (cal2 < 0) {
                    cal1 = (cal1 * (1));
                    cal2 = (cal2 * (-1));
                    cal3 = (cal3 * (1));
                    mod_str1 = "needed: ";
                    mod_str2 = "to lose: ";
                    mod_str3 = "needed: ";
                } else if (cal3 < 0) {
                    cal1 = (cal1 * (1));
                    cal2 = (cal2 * (1));
                    cal3 = (cal3 * (-1));
                    mod_str1 = "needed: ";
                    mod_str2 = "needed: ";
                    mod_str3 = "to lose: ";
                } else {
                    cal1 = (cal1 * (1));
                    cal2 = (cal2 * (1));
                    cal3 = (cal3 * (1));
                    mod_str1 = "needed: ";
                    mod_str2 = "needed: ";
                    mod_str3 = "needed: ";
                }
                str_cal1 = String.valueOf(cal1);
                str_cal2 = String.valueOf(cal2);
                str_cal3 = String.valueOf(cal3);
                calculated.setText(str1 + mod_str1 + str_cal1 + "\n \n" + str2 + mod_str2 + str_cal2 + "\n \n" + str3 + mod_str3 + str_cal3);

            }
        });

        calculate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ri = ricee.getText().toString();
                fi = fishe.getText().toString();
                be = beefe.getText().toString();
                ch = chickene.getText().toString();
                ve = vegetablee.getText().toString();
                eg = egge.getText().toString();

                r = Double.parseDouble(ri);
                f = Double.parseDouble(fi);
                b = Double.parseDouble(be);
                c = Double.parseDouble(ch);
                v = Double.parseDouble(ve);
                e = Double.parseDouble(eg);

                tot_cal = (r * (3.2)) + (f * (1.1)) + (b * (2.8)) + (c * (2.25)) + (v * (1.3)) + (e * (75));
                tot = String.valueOf(tot_cal);

                calculated_tot.setText("Gained total calories: " + tot);
            }
        });
    }

}

