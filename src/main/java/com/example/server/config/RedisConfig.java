package com.example.server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Configuration
@ConditionalOnClass(JedisPool.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    private RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    public JedisPoolConfig jedisPoolConfig() {
            return new JedisPoolConfig();
        }

    @Bean(name = "jedisPool")
    @ConditionalOnMissingBean(JedisPool.class)
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        poolConfig.setMaxTotal(redisProperties.getJedis().getPool().getMaxIdle());
//        poolConfig.setMaxWait(redisProperties.getTimeout());

        JedisPool jedisPool = new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort(),
                (int) (redisProperties.getTimeout().getSeconds()*1000),redisProperties.getPassword());
        return jedisPool;
    }

}
