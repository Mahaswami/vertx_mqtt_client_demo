package com.mahaswami.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;

import java.io.UnsupportedEncodingException;

public class MainVerticle extends AbstractVerticle {


  public static void main(String[] arg) {
    Runner.runExample(com.mahaswami.verticles.MainVerticle.class);
  }

  @Override
  public void start() {
     MqttClientOptions options = new MqttClientOptions();
      options.setMaxMessageSize(100_000_000);
      options.setCleanSession(false);
      //options.setClientId("guru_krupa");

    MqttClient client = MqttClient.create(vertx, options);

    client.publishHandler(s -> {
      try {
        String message = new String(s.payload().getBytes(), "UTF-8");
        System.out.println(String.format("Receive message with content: \"%s\" from topic \"%s\"", message, s.topicName()));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    });
    
    
	client.connect(1883, "192.168.0.177", s -> {
      client.subscribe("my_app/report/#", 0);   //For some confusing reason setting 2 doesn't receive 2 level messages so it needs to be 0
      client.publish("my_app/report/new", Buffer.buffer("Hello from Vertx(++): multi level my_app message"),  MqttQoS.EXACTLY_ONCE, false, false);	 
	});
    
  }
}
