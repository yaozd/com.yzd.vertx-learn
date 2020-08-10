package com.yzd.vertx.websocket.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class NewClientTest extends AbstractVerticle {
  CountDownLatch downLatch;
  @Getter
  AtomicInteger failedCount;

  public NewClientTest(CountDownLatch downLatch) {
    this.downLatch = downLatch;
    this.failedCount = new AtomicInteger(0);
  }

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 1000; i++) {
      callWebsocket();
    }

  }

  private static void callWebsocket() throws InterruptedException {
    CountDownLatch downLatch = new CountDownLatch(2000);
    NewClientTest newClientTest = new NewClientTest(downLatch);
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(newClientTest);
    boolean await = downLatch.await(30, TimeUnit.SECONDS);
    if (!await) {
      log.error("FAILED!Await timeout");
    }
    //
    log.error("FAILED_COUNT:" + newClientTest.getFailedCount().get());
    //
    Thread.sleep(2000);
    //
    vertx.close();
  }

  @Override
  public void start() throws Exception {
    HttpClientOptions clientOptions = new HttpClientOptions();
    clientOptions.setMaxPoolSize(65535);
    HttpClient client = vertx.createHttpClient(clientOptions);
    long size = downLatch.getCount();
    for (int i = 0; i < size; i++) {
      //对应server端，http-demo中的websocket
      client.webSocket(9007, "127.0.0.1", "/websocket", further -> {
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
