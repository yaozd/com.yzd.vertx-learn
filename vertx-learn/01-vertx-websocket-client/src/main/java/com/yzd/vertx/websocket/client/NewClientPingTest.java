package com.yzd.vertx.websocket.client;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class NewClientPingTest {
  public static void main(String[] args) throws InterruptedException {
    HttpClientOptions clientOptions = new HttpClientOptions();
    clientOptions.setMaxPoolSize(65535);
    Vertx vertx = Vertx.vertx();
    HttpClient client = vertx.createHttpClient(clientOptions);
    client.webSocket(9007, "localhost", "/websocket", further -> {
      if (further.failed()) {
        log.error("Connection failed!", further.cause());
        vertx.close();
        return;
      }
      WebSocket channel = further.result();
      String msg1 = "data" + System.nanoTime();
      log.info("MSG1:[{}]", msg1);
      channel.writeTextMessage("data" + System.nanoTime());
      vertx.setPeriodic(1000, event -> {
        Buffer data = Buffer.buffer("ping frame" + System.nanoTime());
        try {
          channel.writePing(data);
        } catch (Exception ex) {
          log.info("Write ping failed!", ex);
          vertx.close();
        }

      });
      long periodicId = vertx.setPeriodic(1000, event -> {
        String msg2 = "data" + System.nanoTime();
        log.info("MSG1:[{}]", msg2);
        try {
          channel.writeTextMessage(msg2).exceptionHandler(throwable -> {
            log.error("write failed", throwable);
            vertx.close();
          });
        } catch (Exception ex) {
          log.info("Write text message failed!", ex);
          vertx.close();
        }
      });
      channel.exceptionHandler(event -> {
        log.error("Exception ", event);
        channel.close();
      });
      channel.handler(data -> {
        log.info("NewClient:Received data:" + data.toString("ISO-8859-1"));
      });
      vertx.setTimer(3000,event -> {
        channel.writeTextMessage("{\"msgType\":1}");
      });
      vertx.setPeriodic(2000,event -> {
        channel.writeTextMessage("{\"msgType\":1}");
      });

    });
  }

}
