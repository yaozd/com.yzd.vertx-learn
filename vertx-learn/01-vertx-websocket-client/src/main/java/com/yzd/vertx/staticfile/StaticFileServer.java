package com.yzd.vertx.staticfile;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * 加载静态资源文件
 * https://dev-tang.com/post/2020/03/vert.x-03.html
 *
 * @Author: yaozh
 * @Description:
 */
public class StaticFileServer extends AbstractVerticle {
  /**
   * http://127.0.0.1:18080/
   * @param args
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(StaticFileServer.class.getName());

  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);
    //DEFAULT_WEB_ROOT = "webroot";
    router.route().handler(StaticHandler.create());

    server.requestHandler(router);
    server.listen(18080);

  }

}
