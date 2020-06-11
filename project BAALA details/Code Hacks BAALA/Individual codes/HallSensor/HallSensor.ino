
// magnetic field

int val = 0;
void setup()
{   pinMode(26,OUTPUT);
  Serial.begin(115200);
    }

void loop() {
 
  val = hallRead();
  // print the results to the serial monitor:
  
  Serial.println(val);
  delay(1000);

if(val >14)
{
  digitalWrite(26,1);
  delay(100);
  }
  else
  {
    digitalWrite(25,0);
    }

  
  //to graph 
}
