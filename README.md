# 应用
SpringBoot2+Lettuce连接池方式连接Redis单机/主备/Proxy集群。

# 依赖
```
<dependency>
    <groupId>io.github.huashijun</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

# 配置
```
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

# 注入
```
@Autowired
private RedisUtils redisUtils;
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