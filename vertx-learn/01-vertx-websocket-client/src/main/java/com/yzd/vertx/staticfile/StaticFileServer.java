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
 * 动态模板与静态文件的结合
 * https://blog.csdn.net/weixin_34074740/article/details/93945123
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
    //StaticHandler staticHandler = StaticHandler.create();
    Router router = Router.router(vertx);
    //DEFAULT_WEB_ROOT = "webroot";
    router.route("/*").handler(StaticHandler.create());
    //http://127.0.0.1:18080/lib/jquery.min.js 访问失败，被过滤掉了。
    //http://127.0.0.1:18080/js/jquery.min.js 访问成功
    //静态文件代理：对目录有过滤功能（lib)，
    //静态文件代理：对xx.xx.js支持不是很好
    //http://127.0.0.1:18080/lib/sockja.min.js 访问失败，被过滤掉了。
    //http://127.0.0.1:18080/lib/sockj.js 访问成功
    server.requestHandler(router);
    server.listen(18080);

  }

}
