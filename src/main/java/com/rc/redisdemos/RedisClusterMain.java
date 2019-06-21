package com.rc.redisdemos;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.*;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName RedisClusterMain
 * @Description JedisCluster 整合 Spring 运行类
 * @Author liux
 * @Date 19-6-19 下午11:36
 * @Version 1.0
 */
public class RedisClusterMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
        scan(applicationContext);
        ((ClassPathXmlApplicationContext) applicationContext).close();
    }

    private static void scan(ApplicationContext applicationContext) {
        JedisCluster jedisCluster = applicationContext.getBean("jedisCluster", JedisCluster.class);
        var clusterNodes = jedisCluster.getClusterNodes().values();

        for (JedisPool jedisPool : clusterNodes) {

            Jedis jedis = jedisPool.getResource();


            ScanResult<String> scanResult = jedis.scan("0");

            Pattern pattern = Pattern.compile("tcp_port:\\d+");
            if (jedis.info("replication").contains("role:master")) {
                String server = jedis.info("Server");
                Matcher matcher = pattern.matcher(server);
                if (matcher.find()) {
                    System.out.println(matcher.group());
                }
                scanResult.getResult().forEach(System.out::println);
                System.out.println("=========");
            }
        }
    }

    private void getAndSet(ApplicationContext applicationContext) {
        RedisService redisClusterService = applicationContext.getBean("redisClusterService", RedisClusterServiceImpl.class);
        String value = redisClusterService.get("hello");
        System.out.println("hello: " + value);

        String statusCode = redisClusterService.set("jedisClusterKey", "jedisClusterValue");
        System.out.println("statusCode: " + statusCode);
        System.out.println("jedisClusterKey: " + redisClusterService.get("jedisClusterKey"));
    }
}