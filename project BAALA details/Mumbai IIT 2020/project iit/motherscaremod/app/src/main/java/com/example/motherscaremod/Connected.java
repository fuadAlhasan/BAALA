package com.example.motherscaremod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

public class Connected extends AppCompatActivity {

    TextView txtArduino, txtString, txtStringLength;
    FloatingActionButton medalarm, diet_chart;
    Handler bluetoothIn;

    final int handlerState = 0;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    PointsGraphSeries<DataPoint> xySeries1, xySeries2;

    GraphView mScatterPlot1, mScatterPlot2;

    private ConnectedThread mConnectedThread;

    private int lastX = 0;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    ArrayList<XYValue> xyValueArray1, xyValueArray2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connected);

        //Link the buttons and textViews to respective views
        txtString = (TextView) findViewById(R.id.txtString);
        txtStringLength = (TextView) findViewById(R.id.testView1);
        medalarm = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        diet_chart = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        mScatterPlot1 = (GraphView) findViewById(R.id.plot1);
        mScatterPlot2 = (GraphView) findViewById(R.id.plot2);

        xyValueArray1 = new ArrayList<>();
        xyValueArray2 = new ArrayList<>();

        medalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Connected.this, MedSchedule.class);
                startActivity(i);
            }
        });

        diet_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Connected.this, Diet.class);
                startActivity(j);
            }
        });


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    Log.d("MY_LOGGER_", "handleMessage: " + recDataString.toString());
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        txtString.setText("Data Received = " + dataInPrint);
                        int dataLength = dataInPrint.length();                          //get length of data received
                        txtStringLength.setText("String Length = " + String.valueOf(dataLength));

                        if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
                        {
                            /*String sensor0 = recDataString.substring(1, 6);             //get sensor value from string between indices 1-5
                            String sensor1 = recDataString.substring(7, 14);            //same again...
                            String sensor2 = recDataString.substring(15, 19);
                            String sensor3 = recDataString.substring(20, 24);*/

                            int beginIndex = 1, endIndex = recDataString.indexOf("+");
                            String sensor0 = recDataString.substring(beginIndex, endIndex);             //get sensor value from string between begin index and end index

                            beginIndex = endIndex + 1;
                            endIndex = recDataString.indexOf("+", beginIndex);
                            String sensor1 = recDataString.substring(beginIndex, endIndex);            //same again...

                            beginIndex = endIndex + 1;
                            endIndex = recDataString.indexOf("+", beginIndex);
                            String sensor2 = recDataString.substring(beginIndex, endIndex);

                            beginIndex = endIndex + 1;
                            endIndex = recDataString.indexOf("+", beginIndex);
                            String sensor3 = recDataString.substring(beginIndex, endIndex);

                            xySeries1 = new PointsGraphSeries<>();
                            xySeries2 = new PointsGraphSeries<>();

                            lastX += 1;
                            double x1 = Double.parseDouble(String.valueOf(lastX));
                            double y1 = Double.parseDouble(sensor0.toString());
                            double x2 = Double.parseDouble(String.valueOf(lastX));
                            double y2 = Double.parseDouble(sensor1.toString());

                            xyValueArray1.add(new XYValue(x1, y1));
                            xyValueArray2.add(new XYValue(x2, y2));

                            if (xyValueArray1.size() != 0) {
                                createScatterPlot1();
                            }
                            if (xyValueArray2.size() != 0) {
                                createScatterPlot2();
                            }

                            String alarmSignalKey = "alarm_signal:";
                            int alarmSignalIndex = recDataString.indexOf(alarmSignalKey);

                            if (alarmSignalIndex != -1) {
                                try {
                                    int alarmSignalEndIndex = recDataString.lastIndexOf("~");
                                    if (alarmSignalEndIndex <= alarmSignalIndex)
                                        alarmSignalEndIndex = recDataString.length();

                                    String s = recDataString.substring(
                                            alarmSignalIndex + alarmSignalKey.length(),
                                            alarmSignalEndIndex
                                    );

                                    Double d = Double.parseDouble(s);

                                    if (d == 11) {
                                        new MyAlarmManager().setAlarm(getApplicationContext(), true);
                                    }

                                } catch (Exception e) {
                                    Log.e("MY_LOGGER", "handleMessage: ", e);
                                }
                            }
                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    private void createScatterPlot1() {

        //sort the array of xy values
        xyValueArray1 = sortArray(xyValueArray1);

        //add the data to the series
        for (int i = 0; i < xyValueArray1.size(); i++) {
            try {
                double x = xyValueArray1.get(i).getX();
                double y = xyValueArray1.get(i).getY();
                xySeries1.appendData(new DataPoint(x, y), true, 1000);
            } catch (IllegalArgumentException e) {

            }

        }
        //set some properties
        xySeries1.setColor(Color.rgb(67, 173, 112));
        xySeries1.setShape(PointsGraphSeries.Shape.RECTANGLE);
        xySeries1.setSize(7);

        mScatterPlot1.setTitle("Body Temperature(Fahrenheit)");
        mScatterPlot1.setTitleColor(Color.WHITE);
        mScatterPlot1.getGridLabelRenderer().setGridColor(Color.GRAY);
        mScatterPlot1.getGridLabelRenderer().setHorizontalLabelsColor(Color.GRAY);
        mScatterPlot1.getGridLabelRenderer().setVerticalLabelsColor(Color.GRAY);

        //set Scrollable and Scaleable
        mScatterPlot1.getViewport().setScalable(true);
        mScatterPlot1.getViewport().setScalableY(true);


        //set manual y bounds
        mScatterPlot1.getViewport().setYAxisBoundsManual(true);
        mScatterPlot1.getViewport().setMaxY(50);
        mScatterPlot1.getViewport().setMinY(0);

        //set manual x bounds
        mScatterPlot1.getViewport().setXAxisBoundsManual(true);
        mScatterPlot1.getViewport().setMinX(0);
        mScatterPlot1.getViewport().scrollToEnd();
        mScatterPlot1.addSeries(xySeries1);


    }

    private void createScatterPlot2() {

        //sort the array of xy values
        xyValueArray2 = sortArray(xyValueArray1);

        //add the data to the series
        for (int i = 0; i < xyValueArray2.size(); i++) {
            try {
                double x = xyValueArray2.get(i).getX();
                double y = xyValueArray2.get(i).getY();
                xySeries2.appendData(new DataPoint(x, y), true, 1000);
            } catch (IllegalArgumentException e) {

            }

        }
        //set some properties
        xySeries2.setColor(Color.rgb(67, 173, 112));
        xySeries2.setShape(PointsGraphSeries.Shape.RECTANGLE);
        xySeries2.setSize(7);

        mScatterPlot2.setTitle("BPM");
        mScatterPlot2.setTitleColor(Color.WHITE);
        mScatterPlot2.getGridLabelRenderer().setGridColor(Color.GRAY);
        mScatterPlot2.getGridLabelRenderer().setHorizontalLabelsColor(Color.GRAY);
        mScatterPlot2.getGridLabelRenderer().setVerticalLabelsColor(Color.GRAY);

        //set Scrollable and Scaleable
        mScatterPlot2.getViewport().setScalable(true);
        mScatterPlot2.getViewport().setScalableY(true);

        //set manual x bounds
        mScatterPlot2.getViewport().setYAxisBoundsManual(true);
        mScatterPlot2.getViewport().setMaxY(2500);
        mScatterPlot2.getViewport().setMinY(0);

        //set manual y bounds
        mScatterPlot2.getViewport().setXAxisBoundsManual(true);
        mScatterPlot2.getViewport().setMinX(0);
        mScatterPlot2.getViewport().scrollToEnd();
        mScatterPlot2.addSeries(xySeries2);

    }

    private ArrayList<XYValue> sortArray(ArrayList<XYValue> array) {
        /*
        //Sorts the xyValues in Ascending order to prepare them for the PointsGraphSeries<DataSet>
        */
        int factor = Integer.parseInt(String.valueOf(Math.round(Math.pow(array.size(), 2))));
        int m = array.size() - 1;
        int count = 0;

        while (true) {
            m--;
            if (m <= 0) {
                m = array.size() - 1;
            }
            try {
                //print out the y entrys so we know what the order looks like
                //Log.d(TAG, "sortArray: Order:");
                //for(int n = 0;n < array.size();n++){
                //Log.d(TAG, "sortArray: " + array.get(n).getY());
                //}
                double tempY = array.get(m - 1).getY();
                double tempX = array.get(m - 1).getX();
                if (tempX > array.get(m).getX()) {
                    array.get(m - 1).setY(array.get(m).getY());
                    array.get(m).setY(tempY);
                    array.get(m - 1).setX(array.get(m).getX());
                    array.get(m).setX(tempX);
                } else if (tempX == array.get(m).getX()) {
                    count++;
                } else if (array.get(m).getX() > array.get(m - 1).getX()) {
                    count++;
                }

                //break when factorial is done
                if (count == factor) {
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
        return array;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(MainActivity.EXTRA_MAC);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        ParcelUuid[] uuids = device.getUuids();
        for (ParcelUuid uuid : uuids) {
            Log.d("MY_LOGGER", "onResume: supported uuid = " + uuid);
        }

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(this, "Socket creation failed", Toast.LENGTH_SHORT).show();
            Log.e("MY_LOGGER", "onResume: ", e);
        }
        Toast.makeText(this, "Connecting...", Toast.LENGTH_LONG).show();
        new SocketConnectionAsyncTask().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }


    private class SocketConnectionAsyncTask extends AsyncTask < Void, Void, Void >
    {
        @Override
        protected Void doInBackground(Void... voids) {
            // Establish the Bluetooth socket connection.
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                    Log.e("MY_LOGGER", "onResume: Socket connect failed because of IOException. Socket closed.", e);
                } catch (IOException e2) {
                    Toast.makeText(Connected.this, "Socket closing failed", Toast.LENGTH_SHORT).show();
                    Log.e("MY_LOGGER", "onResume: ", e);
                    //insert code to deal with this
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();

            //I send a character when resuming.beginning transmission to check device is connected
            //If it is not an exception will be thrown in the write method and finish() will be called
            mConnectedThread.write("x");
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                Log.e("MY_LOGGER", "write: ", e);
            }
        }
    }
}
