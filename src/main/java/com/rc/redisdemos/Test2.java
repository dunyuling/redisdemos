package com.rc.redisdemos;

import redis.clients.jedis.Jedis;

/**
 * @ClassName Test2
 * @Description 操作 有序集合
 * @Author liux
 * @Date 19-6-10 下午3:25
 * @Version 1.0
 */
public class Test2 {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6380);
        jedis.zrangeWithScores("myzset", 0, 1).forEach(System.out::println);
        System.out.println(jedis.zrangeWithScores("myzset", 0, 1));
        System.out.println( new String(jedis.ping("abc".getBytes()))); ;
        jedis.disconnect();
    }
}
