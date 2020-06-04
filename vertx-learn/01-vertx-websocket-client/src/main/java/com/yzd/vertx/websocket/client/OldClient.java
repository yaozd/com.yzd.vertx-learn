package com.yzd.vertx.websocket.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class OldClient extends AbstractVerticle {
  private static CountDownLatch downLatch = new CountDownLatch(10000);

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) throws InterruptedException {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(OldClient.class.getName());
    downLatch.await();
    vertx.close();
  }

  @Override
  public void start() throws Exception {
    HttpClientOptions clientOptions = new HttpClientOptions();
    clientOptions.setMaxPoolSize(65535);
    HttpClient client = vertx.createHttpClient(clientOptions);
    long size = downLatch.getCount();
    for (int i = 0; i < size; i++) {
      client.websocket(8080, "localhost", "/some-uri", websocket -> {
        websocket.handler(data -> {
          log.info("OldClient:Received data:" + data.toString("ISO-8859-1"));
          //主动关闭
          //websocket.close();
          downLatch.countDown();
          log.error("client size:" + downLatch.getCount());
        });
        websocket.writeBinaryMessage(Buffer.buffer("OldClient:Hello world:" + System.nanoTime()));
      });
    }
  }
}
