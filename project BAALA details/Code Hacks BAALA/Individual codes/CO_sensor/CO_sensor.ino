
int vibrator = 25;
int smokeA0 = 33;
// Your threshold value
int sensorThres = 120;//2200

void setup() {

  pinMode(vibrator, OUTPUT);
  pinMode(smokeA0, INPUT);
  Serial.begin(9600);
}

void loop() {
  int analogSensor = analogRead(smokeA0);

  Serial.print("Pin A0: ");
  delay (250);
  Serial.println(analogSensor);
  // Checks if it has reached the threshold value
  if (analogSensor > sensorThres)
  {
    digitalWrite(vibrator,1);
    delay(150);
    //tone(buzzer, 1000, 200);
  }
  else
  {
   
    digitalWrite(vibrator, 0);
    //noTone(buzzer);
  }
  delay(100);
}
