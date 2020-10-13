package com.yzd.vertx.websocket.util;

import com.alibaba.dcm.DnsCacheEntry;
import com.alibaba.dcm.DnsCacheManipulator;

/**
 * @Author: yaozh
 * @Description:
 */
public class DnsManager {
    public static void loadDnsCache(){
        DnsCacheManipulator.clearDnsCache();
        DnsCacheManipulator.setDnsCache("www.hello.com", "192.168.1.1");
        DnsCacheManipulator.setDnsCache("www.world.com", "1234:5678:0:0:0:0:0:200e");
      DnsCacheEntry dnsCache = DnsCacheManipulator.getDnsCache("www.hello.com");
      System.out.println(dnsCache.toString());
    }
}
