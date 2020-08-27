package com.yzd.vertx.websocket.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class NewClient extends AbstractVerticle {
  private static CountDownLatch downLatch = new CountDownLatch(3000);
  private static AtomicInteger failedCount=new AtomicInteger(0);
  // Convenience method so you can run it in your IDE
  public static void main(String[] args) throws InterruptedException {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(NewClient.class.getName());
    downLatch.await();
    //
    Thread.sleep(2000);
    log.error("FAILED_COUNT:"+failedCount.get());
    vertx.close();
  }

  @Override
  public void start() throws Exception {
    HttpClientOptions clientOptions = new HttpClientOptions();
    clientOptions.setMaxPoolSize(65535);
    HttpClient client = vertx.createHttpClient(clientOptions);
    long size = downLatch.getCount();
    for (int i = 0; i < size; i++) {
      client.webSocket(9007, "localhost", "/websocket", further -> {
      //client.webSocket(9007, "localhost", "/websocket", further -> {

        WebSocket channel = further.result();
        if (further.failed()) {
          failedCount.incrementAndGet();
          log.error("failed!", further.cause());
          if (channel != null) {
            channel.close();
          }
          return;
        }
        channel.handler(data -> {
          log.info("NewClient:Received data:" + data.toString("ISO-8859-1"));
          downLatch.countDown();
          //主动关闭
          //channel.close();
          //log.error("client size:" + downLatch.getCount());
        });
        //channel.writeBinaryMessage(Buffer.buffer("NewClient:Hello world:" + System.nanoTime()));
        channel.writeTextMessage("NewClient:Hello world:" + System.nanoTime());
      });
    }
  }
}
