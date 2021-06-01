package edu.hm.cs.infra.dzi2;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Init");
        new DbConnection().initDb();

        System.out.println("MQTT Test");
        try {
            new MqttConnection().testConnection();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
