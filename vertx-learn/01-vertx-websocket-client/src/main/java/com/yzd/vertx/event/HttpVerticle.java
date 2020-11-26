package com.yzd.vertx.event;

/**
 * @Author: yaozh
 * @Description:
 */
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


public class HttpVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.get("/user/:id").handler(this::getUser);

    server.requestHandler(router);
    server.listen(38080);
  }

  private void getUser(RoutingContext ctx) {
    int id = Integer.parseInt(ctx.request().getParam("id"));
    JsonObject json = new JsonObject().put("id", id);
    vertx.eventBus().request("com.javafm.vertx.database", json, r -> {
      if (r.succeeded()){
        out(ctx, r.result().body().toString());
      } else {
        r.cause().printStackTrace();
        ctx.fail(r.cause());
      }
    });
  }


  private void out(RoutingContext ctx, String msg) {
    ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
      .end(msg);
  }
}
