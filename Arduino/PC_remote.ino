#include <IRremote.h>

int RECV_PIN = 7;

String s[]={
"ff02fd",
"ff827d",
"ffba45",
"ff3ac5",
"ff22dd",
"ffa25d",
"ff9a65",
"ff1ae5",
"ff12ed",
"ff926d",
"ffaa55",
"ff2ad5",
"ff32cd",
"ffb24d",
"ff8a75",
"ff0af5",
"ffc23d",
"ff629d",
"ff52ad",
"ff42bd",
"ffe21d",
"ffb847",
"ff7887",
"fff807",
"ff38c7",
"ffd827",
"ff58a7",
"ff9867",
"ff18e7",
"ffe817",
"ff6897",
"ffa857",
"ff28d7",
"ffc837",
"ff48b7",
"ff8877",
"ff08f7",
"fff00f",
"ff708f",
"ffb04f",
"ffb04f",
"ff30cf",
"ffd02f",
"ff50af",
"ff906f",
"ff10ef",
"ffe01f",
"ff609f",
"ffa05f",
"ff20df",
"ffc03f",
"ff40bf",
"ff807f",
"ff00ff"
};
String prvCode="";
IRrecv irrecv(RECV_PIN);

decode_results results;

void setup()
{
  Serial.begin(9600);
  irrecv.enableIRIn(); // Start the receiver
}

void loop() {
  if (irrecv.decode(&results)) {
    String code=String(results.value,HEX);
    if(code.equalsIgnoreCase("FFFFFFFF")){  
      if(prvCode.equalsIgnoreCase(s[18]) || prvCode.equalsIgnoreCase(s[19]) ||prvCode.equalsIgnoreCase(s[21]) ||prvCode.equalsIgnoreCase(s[22])||prvCode.equalsIgnoreCase(s[46])||prvCode.equalsIgnoreCase(s[47])){
        code=prvCode;
      }
    }
    for(int i=0;i<54;i++){
      //Serial.println(s[i]+"            "+String(results.value,HEX));      
      prvCode=code;
      if(s[i]==code){
        Serial.print(code+"\n");
        //Serial.flush();
      }
      else{
        //Serial.println("No Code");
      }
    }
    irrecv.resume(); // Receive the next value
  }
}
