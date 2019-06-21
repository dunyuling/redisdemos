package com.rc.redisdemos;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName Test5
 * @Description redis sentinel
 * @Author liux
 * @Date 19-6-10 下午5:13
 * @Version 1.0
 */
public class Test5 {
    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1024);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);


        Set<String> sentinelSet = new HashSet<>(Arrays.asList("localhost:26380",
                "localhost:26381",
                "localhost:26382"));

        JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinelSet, config, 2000);

        Jedis jedis = null;
        try {
            jedis = sentinelPool.getResource();
            jedis.set("hello","redis sentinel");
            System.out.println(jedis.get("hello"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }
}
