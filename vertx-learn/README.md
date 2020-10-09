### vertx 网关
- [https://gitee.com/mirrors/Gravitee-API-Gateway](https://gitee.com/mirrors/Gravitee-API-Gateway)
- [https://gitee.com/yaozd/Gravitee-API-Gateway](https://gitee.com/yaozd/Gravitee-API-Gateway) 参考
```
vert.x网关设计关键参考点：
1.
类：io.gravitee.gateway.core.invoker.EndpointInvoker
方法：invoker
2.
io.gravitee.gateway.http.connector.http.HttpProxyConnection
connect
3.
io.gravitee.gateway.http.connector.AbstractConnector
request
final HttpClient client = httpClients.computeIfAbsent(Vertx.currentContext(), createHttpClient());
4.
```
