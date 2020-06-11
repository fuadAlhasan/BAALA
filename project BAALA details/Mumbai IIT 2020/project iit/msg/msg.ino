#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <UniversalTelegramBot.h>
// Initialize Wifi connection to the router
char ssid[] = "****"; // your network SSID (name)
char password[] = "****"; // your network key
// Initialize Telegram BOT
#define BOTtoken "*********************************" // your Bot Token (Get from Botfather)
WiFiClientSecure client;
UniversalTelegramBot bot(BOTtoken, client);
int inPut1 = 14; // motion sensor
int inputState = 0; // motion sensor
int ledPin1 = 5; //output for lights or gate  /on /off
int Bot_mtbs = 1000; //mean time between scan messages
long Bot_lasttime; //last time messages' scan has been done
bool inputFlag = HIGH; // flag for input loop to keep it from sending a bunch of text
void setup()
{
  Serial.begin(115200);
  pinMode (ledPin1 , OUTPUT); //control something
  pinMode (inPut1 , INPUT);  //send a message
  // Set WiFi to station mode and disconnect from an AP if it was Previously
  // connected
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  // Attempt to connect to Wifi network:
  Serial.print("Connecting Wifi: ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}

void loop()
{
  inputState = digitalRead (inPut1);

  if (inputState == HIGH && inputFlag == HIGH) { //if motion detected and loop has not run
    for (int x = 0; x < 1; x++) {
      //Serial.println ("send text");
      bot.sendMessage("841056557", "input1 high", ""); //chat id and message to be sent
      inputFlag = LOW; //keep loop from running and sending multi text
    }
  }
  if (inputState == LOW && inputFlag == LOW) { //input returns low and loop has run once
    inputFlag = HIGH; //reset flag after input stops and loop ran once
    // if (inputState == HIGH && timming event ){ //use to start checking for message after motion detected reduce the traffic on wifi
    if (millis() > Bot_lasttime + Bot_mtbs)

    {
      int numNewMessages = bot.getUpdates(bot.last_message_received + 1);
      while (numNewMessages) {
        Serial.println("got response");
        for (int i = 0; i < numNewMessages; i++) {
          Serial.print("chat id ");
          Serial.println(bot.messages[i].chat_id);
          if (bot.messages[i].text == "/on") { //CHECK INCOMMING MESSAGE then do
            // Serial.println("turnn led on");
            digitalWrite (ledPin1 , HIGH);
            bot.sendMessage("841056557", "LED on", "");
          }
          if (bot.messages[i].text == "/off") { //CHECK INCOMMING MESSAGE then do
            //Serial.println("turnn led off");
            digitalWrite (ledPin1 , LOW);
            bot.sendMessage("841056557", "LED off", "");
          }
          if (bot.messages[i].text == "/hello") { //CHECK INCOMMING MESSAGE then do
            //Serial.println("turnn led off");
            //digitalWrite (ledPin1 , LOW); //play message
            bot.sendMessage("841056557", "play message", "");
          }

          // Serial.print("text ");
          // Serial.println(bot.messages[i].text);
          // bot.sendMessage(bot.messages[i].chat_id, bot.messages[i].text, "");
          // bot.sendMessage("########", "youwin", ""); //bot.sendMessage ("chat id", "text","")
          // bot.sendMessage(bot.messages[i].chat_id, "youwin", "");
          // bot.sendMessage("############", bot.messages[i].text, "");
        }
        numNewMessages = bot.getUpdates(bot.last_message_received + 1);
      }
      Bot_lasttime = millis();
    }
  }
}
