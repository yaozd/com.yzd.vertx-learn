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

### vertx:基础
- [Vert.x系列](https://dev-tang.com/post/2020/03/vert.x-01.html)
- [https://gitee.com/dev-tang/learning-vertx]()
```
Vert.x系列（零），开篇，认识Vert.x并创建一个Http服务
Vert.x系列（一），Http Router
Vert.x系列（二），编写REST API并解析请求参数
Vert.x系列（三），加载静态资源文件
Vert.x系列（四），EventBus事件总线
Vert.x系列（五），基于EventBus构建分布式应用
```
