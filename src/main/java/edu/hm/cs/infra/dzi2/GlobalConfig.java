package edu.hm.cs.infra.dzi2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GlobalConfig {
    public static final String BROKER_ADDRESS = "tcp://localhost:1883";

    private ArrayList<String> topics = new ArrayList<>();

    public Iterable<String> getTopics() {
        return topics;
    }

    void readConfig() {

        try {
            final String configString = Files.readString(Path.of("config.json"));
            final JsonObject configObject = JsonParser.parseString(configString).getAsJsonObject();
            final JsonArray jsonTopics = configObject.getAsJsonArray("topics");

            for (JsonElement jsonTopic : jsonTopics) {
                final String topic = jsonTopic.getAsString();
                this.topics.add(topic);
            }
        } catch (Exception e) {
            // ignore, fallback to default topic
            e.printStackTrace();
            topics.clear();
            topics.add("#");
        }
    }
}
