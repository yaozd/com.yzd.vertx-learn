package com.yzd.vertx.websocket.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class PusherClient extends AbstractVerticle {
  private static CountDownLatch downLatch = new CountDownLatch(10);

  private List<WebSocket> webSocketList = new LinkedList<WebSocket>();

  /**
   * ReBalance 功能测试
   * 场景说明：
   * 10个客户端连接，平滑断开。
   *
   * @param args
   * @throws InterruptedException
   */
  public static void main(String[] args) throws InterruptedException {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(PusherClient.class.getName());
    downLatch.await();
    vertx.close();
  }

  @Override
  public void start() throws Exception {
    HttpClientOptions clientOptions = new HttpClientOptions();
    clientOptions.setMaxPoolSize(65535);
    Vertx vertx = Vertx.vertx();
    HttpClient client = vertx.createHttpClient(clientOptions);
    WebSocketConnectOptions webSocketConnectOptions = new WebSocketConnectOptions();
    webSocketConnectOptions.setHost("localhost");
    webSocketConnectOptions.setPort(9007);
    webSocketConnectOptions.addHeader("groupID", "28241");
    webSocketConnectOptions.addHeader("shopID", "6666");
    webSocketConnectOptions.setURI("/websocket");
    for (int i = 0; i < downLatch.getCount(); i++) {
      newClient(vertx, client, webSocketConnectOptions);
    }
    //周期性计时器
    vertx.setPeriodic(2000, event -> {
      Iterator<WebSocket> iterator = webSocketList.iterator();
      while (iterator.hasNext()) {
        WebSocket next = iterator.next();
        if (next.isClosed()) {
          downLatch.countDown();
          iterator.remove();
          continue;
        }
        next.writeTextMessage("hello world");
      }
      log.info("Websocket size:[{}]", webSocketList.size());
      if (webSocketList.isEmpty()) {
        downLatch.countDown();
        vertx.close();
      }
    });
  }

  private void newClient(Vertx vertx, HttpClient client, WebSocketConnectOptions webSocketConnectOptions) {
    client.webSocket(webSocketConnectOptions, further -> {
      if (further.failed()) {
        log.error("Connection failed!", further.cause());
        vertx.close();
        downLatch.countDown();
        return;
      }
      WebSocket channel = further.result();
      webSocketList.add(channel);
      String msg1 = "data" + System.nanoTime();
      log.info("MSG1:[{}]", msg1);
      channel.writeTextMessage("data" + System.nanoTime());
      channel.exceptionHandler(event -> {
        log.error("Exception ", event);
        channel.close();
      });
      channel.handler(data -> {
        log.info("NewClient:Received data:" + data.toString("ISO-8859-1"));
      });
    });
  }
}
