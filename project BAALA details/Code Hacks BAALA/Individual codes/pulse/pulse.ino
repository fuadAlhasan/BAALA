//  Variables

int PulseSensorPurplePin = 34;        // Pulse Sensor PURPLE WIRE connected to ANALOG PIN 0

int LED21 = 21;   //  pulse red

int LED19 = 19;   // pulse blue

int Signal;                // holds the incoming raw data. Signal value can range from 0-1024

int Threshold = 2150;            // Determine which Signal to “count as a beat”, and which to ingore.

// The SetUp Function:

void setup() {

pinMode(21,OUTPUT);// pin that will blink to your heartbeat!
pinMode(34,OUTPUT);
pinMode(19, OUTPUT);
Serial.begin(9600);         // Set’s up Serial Communication at certain speed.

}

// The Main Loop Function

void loop() {

Signal = analogRead(PulseSensorPurplePin);  // Read the PulseSensor’s value.

// Assign this value to the “Signal” variable.

Serial.println(Signal);                    // Send the Signal value to Serial Plotter.

if(Signal > Threshold){                          // If the signal is above “550”, then “turn-on” Arduino’s on-Board LED.

digitalWrite(21,HIGH);

delay(200);

} else {

digitalWrite(21,LOW);                //  Else, the sigal must be below “550”, so “turn-off” this LED.

}

delay(500);

}
