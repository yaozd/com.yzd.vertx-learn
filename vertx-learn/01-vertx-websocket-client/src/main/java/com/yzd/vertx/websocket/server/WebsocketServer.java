package com.yzd.vertx.websocket.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: yaozh
 * @Description:
 */
@Slf4j
public class WebsocketServer extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(WebsocketServer.class.getName());

  }

  @Override
  public void start() throws Exception {
    int port = 8080;
    vertx.createHttpServer().webSocketHandler(ws ->
      ws.handler(data -> {
        log.error("deploymentID:{},binaryHandlerID:{}", context.deploymentID(), ws.binaryHandlerID());
        log.info("RECEIVED_DATA: {}", data.toString("ISO-8859-1"));
        ws.writeFinalTextFrame("SERVER_DATA:" + System.nanoTime());
      }))
      .requestHandler(req -> {
        if (req.uri().equals("/")) {
          req.response().sendFile("ws.html", res -> {
              if (res.failed()) {
                log.error("send file failed!", res.cause());
                req.response().end(getWsHtml(port));

              }
            }
          );
          return;
        }
      }).listen(port);
  }

  private String getWsHtml(int port) {
    String wsHtml = "<html>\n" +
      "<head><title>Web Socket Test</title></head>\n" +
      "<body>\n" +
      "<script>\n" +
      "    var socket;\n" +
      "    if (window.WebSocket) {\n" +
      "        socket = new WebSocket(\"ws://localhost:" + port + "/myapp\");\n" +
      "        socket.onmessage = function (event) {\n" +
      "            alert(\"Received data from websocket: \" + event.data);\n" +
      "        }\n" +
      "        socket.onopen = function (event) {\n" +
      "            alert(\"Web Socket opened!\");\n" +
      "        };\n" +
      "        socket.onclose = function (event) {\n" +
      "            alert(\"Web Socket closed.\");\n" +
      "        };\n" +
      "    } else {\n" +
      "        alert(\"Your browser does not support Websockets. (Use Chrome)\");\n" +
      "    }\n" +
      "\n" +
      "    function send(message) {\n" +
      "        if (!window.WebSocket) {\n" +
      "            return;\n" +
      "        }\n" +
      "        if (socket.readyState == WebSocket.OPEN) {\n" +
      "            socket.send(message);\n" +
      "        } else {\n" +
      "            alert(\"The socket is not open.\");\n" +
      "        }\n" +
      "    }\n" +
      "</script>\n" +
      "<form onsubmit=\"return false;\">\n" +
      "    <input type=\"text\" name=\"message\" value=\"Hello, World!\"/>\n" +
      "    <input type=\"button\" value=\"Send Web Socket Data\" onclick=\"send(this.form.message.value)\"/>\n" +
      "</form>\n" +
      "</body>\n" +
      "</html>";
    return wsHtml;
  }
}
