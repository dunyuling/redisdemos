package com.rc.redisdemos;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Test6
 * @Description redis cluster
 * @Author liux
 * @Date 19-6-19 下午6:38
 * @Version 1.0
 */
public class Test6 {
    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1024);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        Set<HostAndPort> nodeSet = new HashSet<>();
        nodeSet.add(new HostAndPort("127.0.0.1",8000));
        nodeSet.add(new HostAndPort("127.0.0.1",8001));
        nodeSet.add(new HostAndPort("127.0.0.1",8002));
        nodeSet.add(new HostAndPort("127.0.0.1",8003));
        nodeSet.add(new HostAndPort("127.0.0.1",8004));
        nodeSet.add(new HostAndPort("127.0.0.1",8005));

        JedisCluster jedisCluster = new JedisCluster(nodeSet,1000, config);
        String value = jedisCluster.get("hello");
        System.out.println("value: " + value);

    }



}
