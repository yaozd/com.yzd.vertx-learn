package com.yzd.vertx.event;

/**
 * EventBus事件总线
 * https://dev-tang.com/post/2020/03/vert.x-04.html
 *
 * @Author: yaozh
 * @Description:
 */

import io.vertx.core.Vertx;

public class EvenServer {
  /**
   * http://localhost:38080/user/1
   * @param args
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new DatabaseVerticle());
    vertx.deployVerticle(new HttpVerticle());
  }
}
