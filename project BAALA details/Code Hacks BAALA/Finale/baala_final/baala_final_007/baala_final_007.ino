#include "BluetoothSerial.h"

//verification of bluetooth
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

//Pulse sensor variables
int PulseSensorPurplePin = 34;
int LED19 = 21;
int LED21 = 18;
double Signal;
int Threshold = 2150;

//co sensor variables
int buzzer = 25;
int smokeA0 = 33;
int sensorThreshold = 2700;

//temperature sensor variables
double temp, temp_mod;

//uv ray
int val = 0;
int uvvibrator = 22;

//variable for bluetooth object
BluetoothSerial SerialBT;

//array for storing sensor values
double sensorValue[4] = {0, 0, 0, 0};

//setup
void setup() {
  Serial.begin(115200);


  // CO reading:
  pinMode(33,INPUT);
  pinMode(25,OUTPUT);
  //pulse:
  pinMode(21, OUTPUT);
  pinMode(18, OUTPUT);
  pinMode(34, OUTPUT);
  //Uv ray:
  pinMode(22, OUTPUT);

  SerialBT.begin("ESP32test"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
}

//function for sending sensor values to android over bluetooth
void sendAndroidValues()
{
  //puts # before the values so our app knows what to do with the data
  SerialBT.print('#');
  //for loop cycles through 4 sensors and sends values via serial
  for (int k = 0; k < 4; k++)
  {
    SerialBT.print(sensorValue[k]);
    SerialBT.print('+');
    //technically not needed but I prefer to break up data values
    //so they are easier to see when debugging
  }
  SerialBT.print('~'); //used as an end of transmission character - used in app for string length
  SerialBT.println();
  delay(10);        //added a delay to eliminate missed transmissions
}

//function for storing sensor data
void readSensors(double t, double p)
{
  // read the analog in value to the sensor array
  sensorValue[0] = t;
  sensorValue[1] = p;
  sensorValue[2] = 0;
  sensorValue[3] = 0;
}

//main

void loop() 

{
  Signal = analogRead(PulseSensorPurplePin);
  Serial.print ("Pulse rate : ");
  Serial.println(Signal);
  
  temp_mod = touchRead(T0);
  temp = ((0.555556)*(temp_mod-32));
  Serial.print("Temperature");
  Serial.println(temp);
  
 //uv ray index
  val = hallRead();
  Serial.print("UV index: ");
  Serial.println(val);
  delay(1000);
  
  //co sensor:
int analogSensor = analogRead(smokeA0);

  Serial.print("Pin A0 Smoke Value: ");
  Serial.println(analogSensor);
    // Checks if it has reached the threshold value
  if (analogSensor > sensorThreshold)
  {
    digitalWrite(buzzer,1);
    delay(150);
    //tone(buzzer, 1000, 200);
  }
  else
  {
   
    digitalWrite(buzzer, 0);
    //noTone(buzzer);
  }
  



  
if(val >12)
{
  digitalWrite(uvvibrator,1); // the vibrator will vibrate GPIO pin 22
  delay(100);
  }
  else
  {
    digitalWrite(uvvibrator,0);
    }


  
  if (Signal > Threshold) {                        // If the signal is above “550”, then “turn-on” Arduino’s on-Board LED.
    digitalWrite(LED19, HIGH);
    delay(600);
    digitalWrite(LED19, LOW);
  } else {
    digitalWrite(LED21, HIGH);               //  Else, the sigal must be below “550”, so “turn-off” this LED.
  }

  readSensors(temp, Signal);

  if (SerialBT.available()) {
    sendAndroidValues();
  }


  delay(1000);
}
