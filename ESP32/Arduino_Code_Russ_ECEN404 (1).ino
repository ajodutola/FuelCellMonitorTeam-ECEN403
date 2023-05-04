#include <WiFi.h>
#include <Firebase_ESP_Client.h>
//#include <FirebaseESP32.h>
#include "addons/TokenHelper.h"
#include "addons/RTDBHelper.h"
#include <WiFiManager.h> // https://github.com/tzapu/WiFiManager
#include <HardwareSerial.h>
#include <SPI.h>


//#define WIFI_SSID "Russ"
//#define WIFI_PASSWORD "John1234"
#define API_KEY "AIzaSyC_oqjFI4ztj3YBz1DaZNR5FdMqr3Odo7A"
#define DATABASE_URL "https://fuelcell403-default-rtdb.firebaseio.com/"
/*#define CS1_PIN 15
#define CS2_PIN 18
#define MOSI_PIN 23
#define MISO_PIN 19
#define CLK_PIN 18*/



HardwareSerial SerialPort(2); // use UART2

// Define UART pins
#define TXD2 17
#define RXD2 16


//Define Firebase Data oject 
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
int value = 23;
bool signupOK = false;
//String i = "4";

void setup() {
  // put your setup code here, to run once: 
  Serial.begin(115200);
  //SPI.begin(CLK_PIN, MISO_PIN, MOSI_PIN, CS1_PIN);

  //Set the SPI settings 
  /*SPI.setClockDivider(SPI_CLOCK_DIV16); //Set the clock speed to 1 MHz
  SPI.setDataMode(SPI_MODE1);           //Set the data mode to Mode 1
  SPI.setBitOrder(MSBFIRST);            //Set the data order to MSB first

  //Configure the ADC for communication
  digitalWrite(CS1_PIN, HIGH);          //Set the CS pin high
  SPI.beginTransaction(SPISettings(1000000, MSBFIRST, SPI_MODE1)); //Start the SPI transaction
  SPI.transfer(0x80);                   //Send the command to write to Control Register 1
  SPI.transfer(0x09);                   //Configure the ADC for a sampling rate of 1kHz and a refernce voltage of 5V
  SPI.endTransaction();*/
  /*WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED){
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();*/
  
  //WiFiManager, Local intialization. Once its business is done, there is no need to keep it around
  WiFiManager wm;

  // reset settings - wipe stored credentials for testing
  // these are stored by the esp library
  wm.resetSettings();

    bool res;
  // res = wm.autoConnect(); // auto generated AP name from chipid
  // res = wm.autoConnect("AutoConnectAP"); // anonymous ap
  res = wm.autoConnect("AutoconnectAP","password"); // password protected ap

  if(!res) {
      Serial.println("Failed to connect");
      // ESP.restart();
  } 
  else {
      //if you get here you have connected to the WiFi    
      Serial.println("connected...yay :)");
  }
  Serial2.begin(19200, SERIAL_8N1, RXD2, TXD2);
    //Serial1.begin(115200, SERIAL_8N1, RXD2, TXD2);
  Serial.println("Serial Txd is on pin: "+String(TXD2));
  Serial.println("Serial Rxd is on pin: "+String(RXD2));
  /*Assign the api key*/
  config.api_key = API_KEY;

  /*Assign the RTDB URL*/
  config.database_url = DATABASE_URL;

    /*Sign up*/
    if (Firebase.signUp(&config, &auth, "", "")){
      Serial.println("ok");
      signupOK = true;
    }
    else{
      Serial.printf("%s\n", config.signer.signupError.message.c_str());
    }

    /*Assign the callback function for the long running token generation task*/
    config.token_status_callback = tokenStatusCallback; 

    Firebase.begin(&config, &auth);
    Firebase.reconnectWiFi(true);
}

void loop() {
  // put your main code here, to run repeatedly:
  //Serial.println("IN");
  /*if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 5000 || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();
    //Write 
    if (Firebase.RTDB.setInt(&fbdo, "fuelcells/fuelcell1/Volt", value)){
      Serial.println("PASSES");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("TYPE: " + fbdo.dataType());
    }
    else{
      Serial.println("FAILED");
      Serial.println("REASON: " + fbdo.errorReason());      
    }
  }
  value++;*/
  // Wait for UART data
  Serial.println("Above no UART WHile LOOP");
  while (!Serial2.available()) {
    delay(1);
    Serial.println("ERROR");
  }
  Serial.println("Bellow no UART While LOop");
  
  // Send array to Firebase
    if(Serial2.available()) {
      Serial.println("inside Uart If statement");
      //read the string from UART
      //String inputString = Serial2.readStringUntil("\n");
      String inputString = "";
      char incomingByte = 0;
      while (Serial2.available() > 0) {
      incomingByte = Serial2.read();
      if (incomingByte == '\n') {
      break;
      }
      inputString += incomingByte;
      }
      //Serial.println("Input STring =");
      Serial.println("Input STring="+inputString);

      //find the position of colon charater 
      int colonPos = inputString.indexOf(':');

      //extract the substring after colon character
      String fuelCellNum = inputString.substring(0,colonPos);
      Serial.println("Fuel cell Num="+fuelCellNum);

      //convert the index string to an integer
      int fuelCellIndex = fuelCellNum.toInt();

      //extract the substring after the colon character
      String valueString = inputString.substring(colonPos + 1);
      Serial.println("Cell Value =" +valueString);     

      //Convert the value string to an integer
      int VoltageHex = valueString.toInt();

      //change the value to volts
      float  voltageValue = VoltageHex * 0.0000625;
      String voltage = String(voltageValue);  

      String Cellpath_Array[] = {"fuelcells/fuelcell1/voltageLevel","fuelcells/fuelcell2/voltageLevel","fuelcells/fuelcell3/voltageLevel","fuelcells/fuelcell4/voltageLevel","fuelcells/fuelcell5/voltageLevel","fuelcells/fuelcell6/voltageLevel","fuelcells/fuelcell7/voltageLevel","fuelcells/fuelcell8/voltageLevel","fuelcells/fuelcell9/voltageLevel","fuelcells/fuelcell10/voltageLevel","fuelcells/fuelcell11/voltageLevel","fuelcells/fuelcell12/voltageLevel","fuelcells/fuelcell13/voltageLevel","fuelcells/fuelcell14/voltageLevel","fuelcells/fuelcell15/voltageLevel","fuelcells/fuelcell16/voltageLevel"};
            
      if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 5000 || sendDataPrevMillis == 0)){
      sendDataPrevMillis = millis();
      //Write 
      if (Firebase.RTDB.setString(&fbdo, Cellpath_Array[fuelCellIndex-1], voltage.c_str())){
        Serial.println("PASSES");
        Serial.println("PATH: " + fbdo.dataPath());
        Serial.println("TYPE: " + fbdo.dataType());
        
      }
      else{
        Serial.println("FAILED");
        Serial.println("REASON: " + fbdo.errorReason());      
      }
    }
  }   
}