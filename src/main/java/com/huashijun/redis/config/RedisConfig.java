package com.huashijun.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huashijun.redis.util.RedisUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huashijun
 */
@Configuration
@ConditionalOnClass(RedisUtils.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        //使用Jackson2JsonRedisSerializer替换默认的JdkSerializationRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        //设置redis事物一致
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisProperties properties) {
        // 连接池通用配置
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(properties.getLettuce().getPool().getMaxIdle());
        genericObjectPoolConfig.setMinIdle(properties.getLettuce().getPool().getMinIdle());
        genericObjectPoolConfig.setMaxTotal(properties.getLettuce().getPool().getMaxActive());
        genericObjectPoolConfig.setMaxWaitMillis(properties.getLettuce().getPool().getMaxWait().toMillis());

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(properties.getTimeout())
                .shutdownTimeout(properties.getLettuce().getShutdownTimeout())
                .poolConfig(genericObjectPoolConfig)
                .build();
        //LettuceConnectionFactory连接工厂
        LettuceConnectionFactory lettuceConnectionFactory;
        RedisSentinelConfiguration sentinelConfig = getSentinelConfiguration(properties);
        RedisClusterConfiguration clusterConfiguration = getClusterConfiguration(properties);
        if (sentinelConfig != null) {
            lettuceConnectionFactory = new LettuceConnectionFactory(sentinelConfig, clientConfig);
        } else if (clusterConfiguration != null) {
            lettuceConnectionFactory = new LettuceConnectionFactory(clusterConfiguration, clientConfig);
            lettuceConnectionFactory.setDatabase(properties.getDatabase());
        } else {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setDatabase(properties.getDatabase());
            redisStandaloneConfiguration.setHostName(properties.getHost());
            redisStandaloneConfiguration.setPort(properties.getPort());
            redisStandaloneConfiguration.setPassword(RedisPassword.of(properties.getPassword()));
            lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
        }
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisUtils redisUtils(@Qualifier("redisTemplate") RedisTemplate<String,Object> redisTemplate){
        return new RedisUtils(redisTemplate);
    }

    /**
     * 配置主从集群信息
     * @param properties redis配置信息
     * @return 主从集群信息
     */
    private RedisClusterConfiguration getClusterConfiguration(RedisProperties properties) {
        RedisProperties.Cluster cluster = properties.getCluster();
        if (cluster != null) {
            RedisClusterConfiguration config = new RedisClusterConfiguration(cluster.getNodes());
            if (properties.getPassword() != null){
                config.setPassword(RedisPassword.of(properties.getPassword()));
            }
            if (cluster.getMaxRedirects() != null) {
                config.setMaxRedirects(cluster.getMaxRedirects());
            }
            return config;
        }
        return null;
    }

    /**
     * 配置哨兵集群信息
     * @param properties redis配置信息
     * @return 哨兵集群信息
     */
    private RedisSentinelConfiguration getSentinelConfiguration(RedisProperties properties) {
        RedisProperties.Sentinel sentinel = properties.getSentinel();
        if (sentinel != null) {
            RedisSentinelConfiguration config = new RedisSentinelConfiguration();
            config.master(sentinel.getMaster());
            //创建哨兵集群节点
            List<RedisNode> nodes = new ArrayList<>();
            for (String node: sentinel.getNodes()) {
                String[] parts = StringUtils.split(node, ":");
                assert parts != null;
                Assert.state(parts.length == 2, "redis哨兵地址配置不合法！");
                nodes.add(new RedisNode(parts[0], Integer.parseInt(parts[1])));
            }
            config.setSentinels(nodes);
            if (properties.getPassword() != null){
                config.setPassword(RedisPassword.of(properties.getPassword()));
            }
            config.setDatabase(properties.getDatabase());
            return config;
        }
        return null;
    }
}