#include <SoftwareSerial.h>

SoftwareSerial mySerial(10, 11); // TX of hc-05, RX of hc-05
 
int led = 13;

// Pins used for inputs and outputs
const int analogInPin0 = A0;// Analog input pins
const int analogInPin1;
const int analogInPin2;
const int analogInPin3;
 
//Arrays for the 4 inputs
float sensorValue[4] = {0,0,0,0};
float voltageValue[4] = {0,0,0,0};
 
//Char used for reading in Serial characters
char inbyte = 0;
 
void setup() {
  // initialise serial communications at 9600 bps:
  Serial.begin(9600);
  mySerial.begin(9600);
  pinMode(led, OUTPUT);
  digitalWrite(led, HIGH);
}
 
void loop() {
  readSensors();
  getVoltageValue();
  sendAndroidValues();
  //when serial values have been received this will be true
  if (Serial.available() > 0)
  {
    inbyte = Serial.read();
    if (inbyte == '0')
    {
      //LED off
      digitalWrite(led, LOW);
    }
    if (inbyte == '1')
    {
      //LED on
      digitalWrite(led, HIGH);
    }
  }
  //delay by 2s. Meaning we will be sent values every 2s approx
  //also means that it can take up to 2 seconds to change LED state
  delay(1000);
}
 
void readSensors()
{
  // read the analog in value to the sensor array
  
  sensorValue[0] = analogRead(analogInPin0);
  sensorValue[1] = (sensorValue[0] * 2);
  sensorValue[2] = (sensorValue[0] * 3);
  sensorValue[3] = (sensorValue[0] * 4);
  Serial.println(sensorValue[0]);
}
//sends the values from the sensor over serial to BT module
void sendAndroidValues()
 {
  //puts # before the values so our app knows what to do with the data
  mySerial.print('#');
  //for loop cycles through 4 sensors and sends values via serial
  for(int k=0; k<4; k++)
  {
    mySerial.print(voltageValue[k]);
    mySerial.print('+');
    //technically not needed but I prefer to break up data values
    //so they are easier to see when debugging
  }
 mySerial.print('~'); //used as an end of transmission character - used in app for string length
 mySerial.println();
 delay(10);        //added a delay to eliminate missed transmissions
}
 
void getVoltageValue()
{
  for (int x = 0; x < 4; x++)
  {
    voltageValue[x] = ((sensorValue[x]/1023)*5);
  }
}
