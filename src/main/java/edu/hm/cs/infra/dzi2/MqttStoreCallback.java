package edu.hm.cs.infra.dzi2;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttStoreCallback implements MqttCallback {
    private final DbConnection dbConnection;

    public MqttStoreCallback(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Lost connection to MQTT broker.");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        final int lastPartPos = topic.lastIndexOf('/');
        if (lastPartPos < 0) {
            System.out.println("No valid topic: " + topic);
            return;
        }
        final String metric = topic.substring(lastPartPos+1);
        final String sensor = topic.substring(0,lastPartPos);
        final String value = mqttMessage.toString();
        System.out.println("Sensor: " + sensor + ", Key: " + metric + ", Value: " + value);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
