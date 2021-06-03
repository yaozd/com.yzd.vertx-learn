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
  private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

  /**
   * 启动命令：
   * java -Dport=8888 -cp 01-vertx-websocket-client-1.0.0-SNAPSHOT-fat.jar  com.yzd.vertx.websocket.server.WebsocketServer
   *
   * @param args
   */
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(WebsocketServer.class.getName());
    log.info("Listen port[{}]", PORT);

  }

  @Override
  public void start() throws Exception {
    int port = PORT;
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
    String wsHtml = "\n" +
      "<html><head><title>Web Socket Test</title></head>\n" +
      "<body>\n" +
      "<script type=\"text/javascript\">\n" +
      "  var socket;\n" +
      "  if (!window.WebSocket) {\n" +
      "    window.WebSocket = window.MozWebSocket;\n" +
      "  }\n" +
      "  if (window.WebSocket) {\n" +
      "    socket = new WebSocket(\"ws://localhost:8080/websocket\");\n" +
      "    socket.onmessage = function(event) {\n" +
      "      var ta = document.getElementById('responseText');\n" +
      "      ta.value = ta.value + '\\n' + event.data\n" +
      "    };\n" +
      "    socket.onopen = function(event) {\n" +
      "      var ta = document.getElementById('responseText');\n" +
      "      ta.value = \"Web Socket opened!\";\n" +
      "    };\n" +
      "    socket.onclose = function(event) {\n" +
      "      var ta = document.getElementById('responseText');\n" +
      "      ta.value = ta.value + \"Web Socket closed\";\n" +
      "      alert(\"Web Socket closed.\");\n" +
      "    };\n" +
      "  } else {\n" +
      "    alert(\"Your browser does not support Web Socket.\");\n" +
      "  }\n" +
      "\n" +
      "  function send(message) {\n" +
      "    if (!window.WebSocket) { return; }\n" +
      "    if (socket.readyState == WebSocket.OPEN) {\n" +
      "      socket.send(message);\n" +
      "    } else {\n" +
      "      alert(\"The socket is not open.\");\n" +
      "    }\n" +
      "  }\n" +
      "</script>\n" +
      "<form onsubmit=\"return false;\">\n" +
      "  <input type=\"text\" name=\"message\" value=\"Hello, World!\"/><input type=\"button\" value=\"Send Web Socket Data\"\n" +
      "                                                                  onclick=\"send(this.form.message.value)\" />\n" +
      "  <h3>Output</h3>\n" +
      "  <textarea id=\"responseText\" style=\"width:500px;height:300px;\"></textarea>\n" +
      "</form>\n" +
      "</body>\n" +
      "</html>\n";
    return wsHtml;
  }
}
