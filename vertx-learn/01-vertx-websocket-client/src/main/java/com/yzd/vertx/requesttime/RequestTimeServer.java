package com.yzd.vertx.requesttime;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * 记录整个流程耗时
 *
 * @Author: yaozh
 * @Description:
 */
public class RequestTimeServer extends AbstractVerticle {
  /**
   * http://127.0.0.1:48080/xx
   *
   * @param args
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(RequestTimeServer.class.getName());

  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    //记录整个流程耗时
    Router router = Router.router(vertx);
    router.route().handler(r -> {
      r.put("start", System.currentTimeMillis());
      r.next();
    });

    router.route("/xx").handler(r -> {
      r.response().end("ok", e -> System.out.println(System.currentTimeMillis() - (long) r.get("start")));
    });
    server.requestHandler(router);
    server.listen(48080);
  }
}

