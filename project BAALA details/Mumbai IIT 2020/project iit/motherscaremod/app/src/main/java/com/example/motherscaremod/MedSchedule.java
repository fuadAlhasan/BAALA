package com.example.motherscaremod;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MedSchedule extends AppCompatActivity {

    private static final String FILE_NAME = "example.txt";

    String clr = " ";

    EditText medname, hour, minute, format;
    Button adder, clear;
    ListView medlist;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_schedule);

        adder = (Button) findViewById(R.id.button);
        clear = (Button) findViewById(R.id.button2);
        medlist = (ListView) findViewById(R.id.medview);
        medname = (EditText) findViewById(R.id.medname);
        hour = (EditText) findViewById(R.id.hour);
        minute = (EditText) findViewById(R.id.minute);
        format = (EditText) findViewById(R.id.format);

        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_dark, R.id.content_list, listItems);
        medlist.setAdapter(adapter);

        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
                listItems.add(text.toString());
            }


            adapter.notifyDataSetChanged();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = medname.getText().toString() + "  Time: " + hour.getText().toString() + ":" + minute.getText().toString() + " " + format.getText().toString();
                String rec_data = data + "\n";
                listItems.add(data);
                adapter.notifyDataSetChanged();
                Writer(rec_data, FILE_NAME);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.clear();
                adapter.notifyDataSetChanged();
                clear(clr, FILE_NAME);

            }
        });
    }

    public void Writer(String s, String f) {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(f, MODE_APPEND);
            fos.write(s.getBytes());
            Toast.makeText(MedSchedule.this, "Saved to " + getExternalFilesDir(null) + "/" + FILE_NAME,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clear(String s, String f) {
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(f, MODE_PRIVATE);
            fos.write(s.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
