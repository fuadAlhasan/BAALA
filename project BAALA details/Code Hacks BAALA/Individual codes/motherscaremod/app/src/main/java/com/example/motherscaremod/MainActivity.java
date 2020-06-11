package com.example.motherscaremod;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private Button pairedButton;
    private Switch btstate;
    private ProgressDialog progress;
    ListView listView;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    private ArrayList<String> mDeviceList = new ArrayList<String>();

    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    public static String EXTRA_MAC = "device_address";

//BLUETOOTH VERBINDUNG


    private static final int REQUEST_ENABLED = 0;
    private static final int REQUEST_DISCOVERABLE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pairedButton = (Button) findViewById(R.id.button1);
        btstate = (Switch) findViewById(R.id.btstate);
        listView = (ListView) findViewById(R.id.listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                String MAC = itemValue.substring(itemValue.length() - 17);
                Intent cin = new Intent(MainActivity.this, Connected.class);
                cin.putExtra(EXTRA_MAC, MAC);
                startActivity(cin);
            }
        });

        //Pairing Button

        pairedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

                ArrayList<String> devices = new ArrayList<String>();

                for (BluetoothDevice bt : pairedDevices) {
                    devices.add(bt.getName() + "\n" + bt.getAddress());
                    //devices.add(bt.getAddress());

                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.list_dark, R.id.content_list, devices);
                listView.setAdapter(arrayAdapter);
            }
        });


        btstate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Intent bton = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(bton, REQUEST_ENABLED);
                }else{
                    btAdapter.disable();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLED) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "Bluetooth turned on.", Toast.LENGTH_SHORT).show();
                    pairedButton.performClick();
                    break;

                case RESULT_CANCELED:
                    Toast.makeText(this, "Couldn't turn on the bluetooth.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
