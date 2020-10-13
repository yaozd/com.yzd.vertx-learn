package com.yzd.vertx.websocket.util;

import io.leopard.javahost.JavaHost;

import java.util.Properties;

/**
 * javahost(JVM虚拟DNS)解决hosts程序中hosts配置问题
 * https://blog.csdn.net/eff666/article/details/52061223/
 * @Author: yaozh
 * @Description:
 */
public class JavaHostsTest {
  private static Properties props = new Properties();

  static {
    props.put("www.baidu.com", "127.0.0.1");
  }

  public static void loadDns() {
    JavaHost.updateVirtualDns(props);
  }
}
