#include <BluetoothSerial.h>
#include <HX711_ADC.h>

#define flow_sensor 26
#define loadcell_dout  32
#define loadcell_sck 33
#define pump_speed 4
#define pump_in1 2
#define pump_in2 15
#define touch 13

BluetoothSerial SerialBT;
HX711_ADC LoadCell(loadcell_dout, loadcell_sck);

const int pwmOutput[] = {75, 100, 125, 150, 175};
const int minWeight[] = {5, 15, 50, 75, 100};
const int interval = 1000;
const int minimum_weight = 20;

String device_name = "H2OHUB_Dispenser";
float flow_calibration_factor = 54.0;
float scale_calibration_factor = 476.53;

volatile byte pulseCount;
byte pulse1Sec = 0;
double weight = 0;
float flowRate;
unsigned int flowMilliLitres;
unsigned long totalMilliLitres;


void IRAM_ATTR pulseCounter(){
  pulseCount++;
}

boolean isConnected(){
  return SerialBT.connected();
}

boolean isAvailable(){
  return SerialBT.available() > 0;
}

String readStringBT(){
  String result = "";
  while(isAvailable() && isConnected()){
    if(LoadCell.update()){
      weight = LoadCell.getData();
    }
    char x = SerialBT.read();
    if((x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z') || (x >= '0' && x <= '9')){
      result.concat(x);
    }
  }
  return result;
}

int readIntBT(){
  int result = 0;
  while(isConnected()){
    if(LoadCell.update()){
      weight = LoadCell.getData();
    }
    if(isAvailable()){
      String x = readStringBT();
      result = x.toInt();
      break;
    }
  }
  return result;
}

int double_to_int(double target){
  int result = target;
  target = target - result;
  if(target >= 0.555555){
    result++;
  }
  return result;
}

void turn_off_pump(){
  analogWrite(pump_speed, 0);
  digitalWrite(pump_in1, LOW);
  digitalWrite(pump_in2, LOW);
}

void reset_scale(){
  do{
    if(LoadCell.update()){
      weight = LoadCell.getData();
    }
  }while(weight > minimum_weight);
}

void touch_to_dispense(boolean with_reset){
  int touchState = digitalRead(touch);
  if(touchState == HIGH){
    boolean set_on = false;
    while(touchState == HIGH){
      if(LoadCell.update()){
        weight = LoadCell.getData();
        Serial.println(weight);
      }
      if(double_to_int(weight) >= minimum_weight){
        if(!set_on){
          analogWrite(pump_speed, pwmOutput[2]);
          digitalWrite(pump_in1, HIGH);
          digitalWrite(pump_in2, LOW);
          set_on = true;
        }
      }
      else{
        turn_off_pump();
      }
      touchState = digitalRead(touch);
    }
    turn_off_pump();
    if(with_reset){
      reset_scale(); 
    }
  }
}

void new_container(){
  int empty_weight = new_container_empty();
  if(empty_weight > minimum_weight){
    new_container_full(empty_weight);
  }
  reset_scale();
}

int new_container_empty(){
  Serial.println("New container empty");
  boolean success = false;
  int weight_round = 0;
  while(isConnected()){
    if (LoadCell.update()){
      weight = LoadCell.getData();
    }
    if(isAvailable()){
      String response = readStringBT();
      if(response.equals("scale")){
        SerialBT.println("success");
        weight_round = double_to_int(weight);
        if(weight_round > minimum_weight){
          SerialBT.println(weight_round);
          success = true;
          break;
        }
        else{
          SerialBT.println("retry");
        }
      }
    }
  }
  if(success){
    Serial.println("Success");
  }
  return weight_round;
}

void new_container_full(int empty_weight){
  Serial.println("New container full");
  boolean success = false;
  int weight_round = 0;
  while(isConnected()){
    if (LoadCell.update()){
      weight = LoadCell.getData();
    }
    touch_to_dispense(false);
    if(isAvailable()){
      String response = readStringBT();
      if(response.equals("scale")){
        SerialBT.println("success");
        weight_round = double_to_int(weight);
        if(weight_round > empty_weight + minimum_weight){
          SerialBT.println(weight_round);
          success = true;
          break;
        }
        else{
          SerialBT.println("retry");
        }
      }
    }
  }
  if(success){
    Serial.println("Success");
    delay(3000);
    SerialBT.disconnect();
  }
}

void drink(){
  Serial.println("Drink");
  int empty_weight = readIntBT();
  if(empty_weight > 0){
    SerialBT.println("success");
    int full_weight = readIntBT();
    if(full_weight > 0){
      SerialBT.println("success");
      int limit = readIntBT();
      if(limit > 0){
        SerialBT.println("success");
        pre_start_dispense(empty_weight, full_weight, limit);
      } 
    }
  }
}

void pre_start_dispense(int empty_weight, int full_weight, int limit){
  Serial.println("Pre-Start Dispense");
  boolean success = false;
  int weight_round = 0;
  while(isConnected()){
    if (LoadCell.update()){
      weight = LoadCell.getData();
    }
    if(isAvailable() && isConnected()){
      String result = readStringBT();
      if(result.equals("start")){
        weight_round = double_to_int(weight);
        Serial.println(weight_round);
        if((weight_round + minimum_weight) >= full_weight){
          SerialBT.println("isFull");
        }
        else if(weight_round < empty_weight){
          SerialBT.println("underWeight");
        }
        else{
          SerialBT.println("success");
          success = true;
          break;
        }
      }
    }
  }
  if(success){
    int need_to_dispense = full_weight;
    if((full_weight - weight_round) > limit){
      need_to_dispense = limit + weight_round;
    }
    start_dispense(weight_round, need_to_dispense);
  }
  reset_scale();
}

void start_dispense(int start_weight, int full_weight){
  Serial.println("Start dispense");
  boolean success = false;
  int weight_round = 0;
  int start_speed = 0;
  
  int need_to_dispense = full_weight - start_weight;

  for(int i = 4; i >= 0; i--){
    if(need_to_dispense >=  minWeight[i]){
      start_speed = i;
      break;
    }
  }
  
  long prev = 0;
  pulseCount = 0;
  flowRate = 0.0;
  flowMilliLitres = 0;
  totalMilliLitres = 0;
  
  analogWrite(pump_speed, pwmOutput[start_speed]);
  digitalWrite(pump_in1, HIGH);
  digitalWrite(pump_in2, LOW);
  
  while(isConnected()){
      if (LoadCell.update()){
        weight = LoadCell.getData();
        weight_round = double_to_int(weight);
      }
      if(weight_round + 5 >= full_weight){
        turn_off_pump();
        success = true;
        break;
      }
      if(start_speed > 0 && (need_to_dispense - totalMilliLitres) <= minWeight[start_speed]){
        start_speed--;
        Serial.print("Speed: ");
        Serial.println(pwmOutput[start_speed]);
        analogWrite(pump_speed, pwmOutput[start_speed]);
      }
      if(millis() - prev > interval){
        pulse1Sec = pulseCount;
        pulseCount = 0;
    
        flowRate = ((1000.0 / (millis() - prev)) * pulse1Sec) / flow_calibration_factor;
        prev = millis();
        flowMilliLitres = (flowRate / 60) * 1000;
        totalMilliLitres += flowMilliLitres;
        Serial.print("Output Liquid Quantity: ");
        Serial.print(totalMilliLitres);
        Serial.print("mL / ");
        Serial.print(weight_round - start_weight);
        Serial.println(" gram");
      }
   }
   turn_off_pump();
   if(success){
     int x = 4;
     while(isConnected() && x > 0){
       if (LoadCell.update()){
         weight = LoadCell.getData();
         weight_round = double_to_int(weight);
       }
       if(millis() - prev > interval){
         x--;
         prev = millis();
       }
     }
     if(isConnected()){
       SerialBT.println(weight_round - start_weight);
     }
     else{
       SerialBT.println("failed");
     }
   }
   else{
     SerialBT.println("failed");
   }
   delay(3000);
   SerialBT.disconnect();
}

void setup() {
  Serial.begin(9600);
  SerialBT.begin(device_name);
  LoadCell.begin();
  LoadCell.start(2000, true);
  LoadCell.setCalFactor(scale_calibration_factor);
  LoadCell.tareNoDelay();

  pinMode(flow_sensor, INPUT_PULLUP);
  pinMode(touch, INPUT);
  pinMode(pump_speed, OUTPUT);
  pinMode(pump_in1, OUTPUT);
  pinMode(pump_in2, OUTPUT);
  turn_off_pump();
  
  attachInterrupt(digitalPinToInterrupt(flow_sensor), pulseCounter, FALLING);
}

void loop() {
  if(LoadCell.update()){
    weight = LoadCell.getData();
  }
  
  touch_to_dispense(true);
  
  if(isConnected() && isAvailable()){
    String result = readStringBT();
    if(result.equals("new")){
      SerialBT.println("success");
      new_container();
    }
    else if(result.equals("drink")){
      SerialBT.println("success");
      drink();
    }
    Serial.println("Back to loop");
  }
  delay(100);
}
