package com.yzd.vertx.router;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Http Router
 * https://dev-tang.com/post/2020/03/vert.x-01.html
 *
 * @Author: yaozh
 * @Description:
 */
public class RouterServer extends AbstractVerticle {
  /**
   * http://127.0.0.1:28080/users
   *
   * @param args
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(RouterServer.class.getName());

  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    //包含请求类型：get post delete
    Router router = Router.router(vertx);
    router.route("/login").handler(this::login);
    router.post("/user").handler(this::addUser);
    router.delete("/user").handler(this::deleteUser);
    router.get("/users").handler(this::getUserList);

    server.requestHandler(router);
    server.listen(28080);
  }

  private void getUserList(RoutingContext ctx) {
    out(ctx, "getUserList");
  }

  private void deleteUser(RoutingContext ctx) {
    out(ctx, "deleteUser");
  }

  private void addUser(RoutingContext ctx) {
    out(ctx, "addUser");
  }

  private void login(RoutingContext ctx) {
    out(ctx, "login");
  }

  private void out(RoutingContext ctx, String msg) {
    ctx.response().putHeader("Content-Type", "text/plain; charset=utf-8")
      .end(msg);
  }
}
