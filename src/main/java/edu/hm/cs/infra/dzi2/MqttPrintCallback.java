package edu.hm.cs.infra.dzi2;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttPrintCallback implements MqttCallback {
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Lost connection to MQTT broker.");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        System.out.println("MQTT message: " + topic + " = " + mqttMessage.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
