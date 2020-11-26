package com.yzd.vertx.event;

/**
 * @Author: yaozh
 * @Description:
 */
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class DatabaseVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();

    MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("com.javafm.vertx.database");
    consumer.handler(msg -> {
      // 加装从数据库查询出数据，然后返回
      System.out.println(msg.body());

      JsonObject json = new JsonObject();
      json.put("id", 1);
      json.put("name", "dev-tang");
      msg.reply(json);
    });
  }
}
