package com.rc.redisdemos;

import org.apache.commons.pool2.impl.GenericObjectPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName Test3
 * @Description jedis 连接池
 * @Author liux
 * @Date 19-6-10 下午3:33
 * @Version 1.0
 */
public class Test3 {
    public static void main(String[] args) {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1024);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        JedisPool jedisPool = new JedisPool(config, "localhost", 6380);

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.zrangeWithScores("myzset", 0, 1).forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }
}
