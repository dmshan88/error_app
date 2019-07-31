package com.example.shan.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyMqttService extends Service {
    private  MqttAndroidClient mqttAndroidClient;
    private  ErrorLog errorLog = null;
    final String serverUri = "tcp://192.168.3.159:1883";
    final String clientId = "mnchip_mqtt_app000";
    final String username = "";
    final String password = "";
    final String subscriptionTopic = "error_app/#";
    final boolean isDebug = true;
    private final String appMessageTopic = "error_app/error_message";
    private final String appMessageTopicDebug = "error_app/error_message_debug";
    private final String appControlErrorTopic = "error_app/control/error";
    private final String appControlErrorTopicDebug = "error_app/control_debug/error";
    private final String appControlDispatherTopic = "error_app/control/dispather";
    private final String appControlDispatherTopicDebug = "error_app/control_debug/dispather";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        errorLog = ErrorLog.getInstance(getApplicationContext());
//        errorLog.initNewLog();
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext() ,serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Toast.makeText(getApplicationContext(), "连接成功",Toast.LENGTH_SHORT).show();
                try {
                    mqttAndroidClient.subscribe(subscriptionTopic,2);
                } catch (MqttException e) {
                    Toast.makeText(getApplicationContext(), "subscribe MqttException"
                            + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(getApplicationContext(), "离线",Toast.LENGTH_LONG).show();
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                analyzeMessage(topic, new String(message.getPayload()));
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Toast.makeText(getApplicationContext(), "发送成功",Toast.LENGTH_SHORT).show();
            }
        });
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        try {
            mqttAndroidClient.connect(mqttConnectOptions);
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), " connect MqttException"
                    + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    private void analyzeMessage(String topic , String message) {
        if (topic.equals(isDebug ? appMessageTopicDebug : appMessageTopic)) {
            Toast.makeText(getApplicationContext(), "收到消息",Toast.LENGTH_SHORT).show();
            errorLog.addLog(message);
        } else if (topic.equals(isDebug ? appControlDispatherTopicDebug : appControlDispatherTopic)) {
            Toast.makeText(getApplicationContext(), "Dispather service "
                    + message, message.equals("off") ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        } else if (topic.equals(isDebug ? appControlErrorTopicDebug: appControlErrorTopic)) {
            Toast.makeText(getApplicationContext(), "Error message service "
                    + message, message.equals("off") ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        }
    }
}
