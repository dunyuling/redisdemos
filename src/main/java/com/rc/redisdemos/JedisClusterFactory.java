package com.rc.redisdemos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName JedisClusterFactory
 * @Description JedisCluster 工厂类
 * @Author liux
 * @Date 19-6-19 下午11:10
 * @Version 1.0
 */
public class JedisClusterFactory {

    private JedisCluster jedisCluster;

    private List<String> hostPortList;

    private int timeOut;//单位为毫秒

    private Logger logger = LoggerFactory.getLogger(JedisClusterFactory.class);

    public void init() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        Set<HostAndPort> nodeSet = new HashSet<>();
        for (String hostPort : hostPortList) {
            String[] arr = hostPort.split(":");
            if (arr.length != 2) {
                continue;
            }
            nodeSet.add(new HostAndPort(arr[0], Integer.valueOf(arr[1])));
        }
        try {
            jedisCluster = new JedisCluster(nodeSet, timeOut, jedisPoolConfig);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void destroy() {
        if (jedisCluster != null) {
            try {
                jedisCluster.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setHostPortList(List<String> hostPortList) {
        this.hostPortList = hostPortList;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
}