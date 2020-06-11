// ESP32 Touch Test
// Just test touch pin for the temp.  - Touch0 is T0 which is on GPIO 4.
//temp sensor
void setup()
{
  Serial.begin(115200);
  delay(1000); 
  Serial.println("ESP32 Touch Test");
}

void loop()
{
  Serial.println(touchRead(T0));  // get value using T0
  delay(1000);
}

//you've to put a random value here..
