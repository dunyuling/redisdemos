package com.rc.redisdemos;

import redis.clients.jedis.JedisCluster;

/**
 * @ClassName RedisClusterServiceImpl
 * @Description RedisCluster jedis 实现类
 * @Author liux
 * @Date 19-6-19 下午11:32
 * @Version 1.0
 */
public class RedisClusterServiceImpl implements RedisService {

    private JedisCluster jedisCluster;

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public String set(String key, String value) {
        return jedisCluster.set(key,value);
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }
}
