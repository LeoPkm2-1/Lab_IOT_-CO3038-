package com.example.myiotandroiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.graphics.Color;


import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumi,txtBright;
    LabeledSwitch btnLED,btnPUMP,btnDOOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTemp = findViewById(R.id.txtTemperature);
        txtHumi = findViewById(R.id.txtHumidity);
        btnLED = findViewById(R.id.btnLED);
        btnPUMP = findViewById(R.id.btnPUMP);
        txtBright = findViewById(R.id.txtBrightness);
        btnDOOR = findViewById(R.id.btnDOOR);

        btnLED.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    sendDataMQTT("thinhphatmai2001/feeds/nutnhan1","1");
                }else{
                    sendDataMQTT("thinhphatmai2001/feeds/nutnhan1","0");
                }
            }
        });
        btnPUMP.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    sendDataMQTT("thinhphatmai2001/feeds/nutnhan2","1");
                }else{
                    sendDataMQTT("thinhphatmai2001/feeds/nutnhan2","0");
                }
            }
        });
        btnDOOR.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    sendDataMQTT("thinhphatmai2001/feeds/nutnhan3","1");
                }else{
                    sendDataMQTT("thinhphatmai2001/feeds/nutnhan3","0");
                }
            }
        });

        startMQTT();
    }

    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){
        }
    }

    public void startMQTT(){
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + " --- " + message.toString());
                if(topic.contains("cambien1")){
                    String cambien1_data_str = message.toString();
                    float cambien1_data = Float.parseFloat(cambien1_data_str);
                    txtTemp.setText(cambien1_data_str+"°C");
                    if(cambien1_data<10){
                        String hexColor = "#2b80b4"; // Example color code (gold color)
                        int color = Color.parseColor(hexColor);
                        txtTemp.setBackgroundColor(color);
                    }else if(cambien1_data<30){
                        String hexColor = "#01afd7"; // Example color code (gold color)
                        int color = Color.parseColor(hexColor);
                        txtTemp.setBackgroundColor(color);
                    }else{
                        String hexColor = "#f35757"; // Example color code (gold color)
                        int color = Color.parseColor(hexColor);
                        txtTemp.setBackgroundColor(color);
                    }


                } else if(topic.contains("cambien3")) {

                    String cambien3_data_str = message.toString();
                    float cambien3_data = Float.parseFloat(cambien3_data_str);
                    txtHumi.setText(cambien3_data_str + "%");
                     if(cambien3_data<60){
                        String hexColor = "#18469e"; // Example color code (gold color)
                        int color = Color.parseColor(hexColor);
                        txtHumi.setBackgroundColor(color);
                        // open pump
                         btnPUMP.setOn(true);
                         sendDataMQTT("thinhphatmai2001/feeds/nutnhan2","1");

                    }else {
                        String hexColor = "#01afd7"; // Example color code (gold color)
                        int color = Color.parseColor(hexColor);
                        txtHumi.setBackgroundColor(color);
                         // close pump
                         btnPUMP.setOn(false);
                         sendDataMQTT("thinhphatmai2001/feeds/nutnhan2","0");
                    }
                }else if(topic.contains("cambien2")){
                    String cambien2_data_str = message.toString();
                    float cambien2_data = Float.parseFloat(cambien2_data_str);
                    txtBright.setText(cambien2_data +"-LUX");
                    if(cambien2_data<50){

                        String hexColor = "#705b0a"; // Example color code (gold color)
                        int color = Color.parseColor(hexColor);
                        txtBright.setBackgroundColor(color);

                        // On led
                        btnLED.setOn(true);
                        sendDataMQTT("thinhphatmai2001/feeds/nutnhan1","1");
                    }else if(cambien2_data<400){
                        String hexColor = "#fcbf4a"; // Example color code (gold color)
                        int color = Color.parseColor(hexColor);
                        txtBright.setBackgroundColor(color);
                    }else{
                        String hexColor = "#f7f2c7"; // Example color code (gold color)
                        int color = Color.parseColor(hexColor);
                        txtBright.setBackgroundColor(color);
                        // Off led
                        btnLED.setOn(false);
                        sendDataMQTT("thinhphatmai2001/feeds/nutnhan1","0");
                    }
                }else if(topic.contains("nutnhan1")){
                    if(message.toString().equals("1")){
                        btnLED.setOn(true);
                    }else{
                        btnLED.setOn(false);
                    }
                }else if(topic.contains("nutnhan2")){
                    if(message.toString().equals("1")){
                        btnPUMP.setOn(true);
                    }else{
                        btnPUMP.setOn(false);
                    }
                }else if(topic.contains("nutnhan3")){
                    if(message.toString().equals("1")){
                        btnDOOR.setOn(true);
                    }else{
                        btnDOOR.setOn(false);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}