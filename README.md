# 应用
~~SpringBoot2+Lettuce连接池方式连接Redis单机/主备/Proxy集群。~~

~~SpringBoot2+Lettuce连接池方式连接Redis单机/主从复制/哨兵sentinel模式/Cluster集群模式~~

SpringBoot2+Lettuce连接池方式/Redisson+连接 +Redis单机/主从复制/哨兵sentinel模式/Cluster集群模式

# 技术思路
![技术思路](https://github.com/huashijun/huashijun.github.io/raw/master/redis-starter.jpg)
![技术思路](https://raw.githubusercontent.com/huashijun/huashijun.github.io/master/redis-starter1.jpg)
![技术思路](https://github.com/huashijun/huashijun.github.io/raw/master/redis-starter2.jpg)

# boot版本
2.0.x/2.1.x/2.2.x/2.3.x/2.4.x/2.5.x/2.6.x

# 依赖
```
<dependency>
    <groupId>io.github.huashijun</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
```
<dependency>
    <groupId>io.github.huashijun</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```
```
#最新
<dependency>
    <groupId>io.github.huashijun</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
    <version>1.2.0</version>
</dependency>
```

# 配置
```
#redis单机配置
spring:
  redis:
    database: 6  #Redis索引0~15，默认为0
    host: 127.0.0.1
    port: 6379
    password:  #密码（默认为空）
    lettuce: # 这里标明使用lettuce配置
      shutdown-timeout: 1000
      pool:
        max-active: 8   #连接池最大连接数（使用负值表示没有限制）
        max-wait: -1  #连接池最大阻塞等待时间（使用负值表示没有限制）
        max-idle: 5     #连接池中的最大空闲连接
        min-idle: 0     #连接池中的最小空闲连接
    timeout: 10000    #连接超时时间（毫秒）
```
```
#redis集群配置
spring:
  redis:
    database: 6
    password: 123456
    lettuce:
      shutdown-timeout: 1000
      pool:
        max-active: 8
        max-wait: -1
        max-idle: 5
        min-idle: 0
    cluster:
      nodes:
        - 127.0.0.1:8080
        - 127.0.0.1:8081
        - 127.0.0.1:8082
      max-redirects: 3
```
```
#redis哨兵配置/主从配置
spring:
  redis:
    database: 6
    password: 123456
    lettuce:
      shutdown-timeout: 1000
      pool:
        max-active: 8
        max-wait: -1
        max-idle: 5
        min-idle: 0
    sentinel:
      master:
      nodes:
```
```
#redisson.yml
#redisson Cluster mode 集群模式
clusterServersConfig:
  idleConnectionTimeout: 10000 #连接空闲超时，单位：毫秒
  connectTimeout: 10000 #连接超时，单位：毫秒
  timeout: 3000 #命令等待超时，单位：毫秒
  retryAttempts: 3 #命令失败重试次数
  retryInterval: 1500   #命令重试发送时间间隔，单位：毫秒
  failedSlaveReconnectionInterval: 3000
  failedSlaveCheckInterval: 60000
  password: null    #密码
  subscriptionsPerConnection: 5 #单个连接最大订阅数量
  clientName: null  #客户端名称
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {} #负载均衡算法类
  subscriptionConnectionMinimumIdleSize: 1 #从节点发布和订阅连接的最小空闲连接数
  subscriptionConnectionPoolSize: 50 #从节点发布和订阅连接池大小
  slaveConnectionMinimumIdleSize: 24 #从节点最小空闲连接数
  slaveConnectionPoolSize: 64 #从节点连接池大小
  masterConnectionMinimumIdleSize: 24 #masterConnectionMinimumIdleSize
  masterConnectionPoolSize: 64 #主节点连接池大小
  readMode: "SLAVE" #读取操作的负载均衡模式
  subscriptionMode: "SLAVE" #订阅操作的负载均衡模式
  nodeAddresses: #添加节点地址
  - "redis://127.0.0.1:7004"
  - "redis://127.0.0.1:7001"
  - "redis://127.0.0.1:7000"
  scanInterval: 1000 #集群扫描间隔时间
  pingConnectionInterval: 30000 #ping连接间隔
  keepAlive: false 
  tcpNoDelay: true
threads: 16
nettyThreads: 32
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: "NIO"
```
```
#redisson.yml
#redisson Replicated mode 云托管模式
replicatedServersConfig:
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  failedSlaveReconnectionInterval: 3000
  failedSlaveCheckInterval: 60000
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 50
  slaveConnectionMinimumIdleSize: 24
  slaveConnectionPoolSize: 64
  masterConnectionMinimumIdleSize: 24
  masterConnectionPoolSize: 64
  readMode: "SLAVE"
  subscriptionMode: "SLAVE"
  nodeAddresses:
  - "redis://redishost1:2812"
  - "redis://redishost2:2815"
  - "redis://redishost3:2813"
  scanInterval: 1000
  monitorIPChanges: false
threads: 16
nettyThreads: 32
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: "NIO"
```
```
#redisson.yml
#redisson Single instance mode 单Redis节点模式
singleServerConfig:
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  address: "redis://127.0.0.1:6379"
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 50
  connectionMinimumIdleSize: 24
  connectionPoolSize: 64
  database: 6
  dnsMonitoringInterval: 5000
threads: 16
nettyThreads: 32
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: "NIO"
```
```
#redisson.yml
#redisson Sentinel mode 哨兵模式
sentinelServersConfig:
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  failedSlaveReconnectionInterval: 3000
  failedSlaveCheckInterval: 60000
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 50
  slaveConnectionMinimumIdleSize: 24
  slaveConnectionPoolSize: 64
  masterConnectionMinimumIdleSize: 24
  masterConnectionPoolSize: 64
  readMode: "SLAVE"
  subscriptionMode: "SLAVE"
  sentinelAddresses:
  - "redis://127.0.0.1:26379"
  - "redis://127.0.0.1:26389"
  masterName: "mymaster"
  database: 6
threads: 16
nettyThreads: 32
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: "NIO"
```
```
#redisson.yml
#redisson Master slave mode 主从模式
masterSlaveServersConfig:
  idleConnectionTimeout: 10000
  connectTimeout: 10000
  timeout: 3000
  retryAttempts: 3
  retryInterval: 1500
  failedSlaveReconnectionInterval: 3000
  failedSlaveCheckInterval: 60000
  password: null
  subscriptionsPerConnection: 5
  clientName: null
  loadBalancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
  subscriptionConnectionMinimumIdleSize: 1
  subscriptionConnectionPoolSize: 50
  slaveConnectionMinimumIdleSize: 24
  slaveConnectionPoolSize: 64
  masterConnectionMinimumIdleSize: 24
  masterConnectionPoolSize: 64
  readMode: "SLAVE"
  subscriptionMode: "SLAVE"
  slaveAddresses:
  - "redis://127.0.0.1:6381"
  - "redis://127.0.0.1:6380"
  masterAddress: "redis://127.0.0.1:6379"
  database: 6
threads: 16
nettyThreads: 32
codec: !<org.redisson.codec.JsonJacksonCodec> {}
transportMode: "NIO"
```

# 注入
```
#redis注入
@Autowired
private RedisUtils redisUtils;
```
```
#redisson注入
@Autowired
private RedissonClient redissonClient;
```

# 测试
```
@Test
public void keys() {
    System.out.println("hello world");
    Set<String> set = redisUtils.keys("*");
    System.out.println(set);
    for (String str : set) {
        System.out.println(str);
    }
}

@Test
public void stringSet() {
    System.out.println("stringSet");
    redisUtils.set("aaa","aaa");
    redisUtils.set("bbb","bbb");
    redisUtils.set("ccc","CCC");
    redisUtils.set("DDD","你好");
}

@Test
public void stringGet() {
    System.out.println("stringGet");
    Object aaa = redisUtils.get("aaa");
    Object bbb = redisUtils.get("bbb");
    Object ccc = redisUtils.get("ccc");
    Object ddd = redisUtils.get("DDD");
    System.out.println(aaa);
    System.out.println(bbb);
    System.out.println(ccc);
    System.out.println(ddd);
}

@Test
public void stringSetWithTime() {
    System.out.println("stringSetWithTime");
    redisUtils.set("eee",111,1000000L);
    redisUtils.set("fff",222,1000000L);
    redisUtils.set("ggg","CCC",1000000L);
    redisUtils.set("hhh","你好",1000000L);
}

@Test
public void stringIncrement() {
    System.out.println("stringIncrement");
    redisUtils.increment("eee",111L);
    redisUtils.increment("fff",222L);
}

@Test
public void stringdecrease() {
    System.out.println("stringdecrease");
    redisUtils.increment("eee",-2L);
    redisUtils.increment("fff",-2L);
}

@Test
public void hashPut() {
    System.out.println("hashPut");
    redisUtils.hashPut("hashPut-aaa","aaa","aaa");
    redisUtils.hashPut("hashPut-aaa","bbb","bbb");
    redisUtils.hashPut("hashPut-aaa","ccc","CCC");
    redisUtils.hashPut("hashPut-aaa","ddd","你好");
    redisUtils.hashPut("hashPut-aaa","eee",111);
    redisUtils.hashPut("hashPut-aaa","FFF",222);
}
@Test
public void hashPutWithTime() {
    System.out.println("hashPutWithTime");
    redisUtils.hashPut("hashPut-aaa","ggg",333,1000000L);
}

@Test
public void hashPutAll() {
    System.out.println("hashPutAll");
    Map<String,Object> map = new HashMap<>(6);
    map.put("aaa","aaa");
    map.put("bbb","bbb");
    map.put("ccc","CCC");
    map.put("ddd","你好");
    map.put("eee",111);
    map.put("FFF",222);
    redisUtils.hashPutAll("hashPut-bbb",map);
}

@Test
public void hashPutAllWithTime() {
    System.out.println("hashPutAllWithTime");
    Map<String,Object> map = new HashMap<>(6);
    map.put("aaa","aaa");
    map.put("bbb","bbb");
    map.put("ccc","CCC");
    map.put("ddd","你好");
    map.put("eee",111);
    map.put("FFF",222);
    redisUtils.hashPutAll("hashPut-ccc",map,1000000L);
}

@Test
public void hashHasKey() {
    System.out.println("hashHasKey");
    boolean fff = redisUtils.hashHasKey("hashPut-ccc", "FFF");
    System.out.println(fff);
}

@Test
public void hashEntries() {
    System.out.println("hashEntries");
    Map<Object,Object> map = redisUtils.hashEntries("hashPut-ccc");
    for (Map.Entry<Object, Object> entry : map.entrySet()) {
        System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
    }
}

@Test
public void hashDecrease() {
    System.out.println("hashDecrease");
    double eee = redisUtils.hashDecrease("hashPut-ccc", "eee", -4.0);
    System.out.println(eee);
}
```

# redisson 锁使用
```
public Object addLock() throws Throwable {
    String lockStr = "lockStr";
    Object result = null;
    RReadWriteLock lock = redissonClient.getReadWriteLock(lockStr);
    try {
        //使用写锁，等待0秒，锁持续时间为5秒
        boolean canLock = lock.writeLock().tryLock(0,5, TimeUnit.SECONDS);
        if (canLock) {
            try {
                // 执行方法
                result = 具体执行业务的方式
            } finally {
                lock.writeLock().unlock();
            }
        }
    } catch (InterruptedException e) {
        throw "抛出异常";
    }
    if(result == null){
        return "错误的提示文案";
    }
    return result;
}
```

# 反馈
如果该组件有问题请留言反馈，受到反馈后会及时修复问题

# 点赞
如果使用该组件觉得还不错，请留下您的足迹，点个赞