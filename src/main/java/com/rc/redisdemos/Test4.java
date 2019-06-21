package com.rc.redisdemos;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * @ClassName Test4
 * @Description pipeline
 * @Author liux
 * @Date 19-6-10 下午4:57
 * @Version 1.0
 */
public class Test4 {
    public static void main(String[] args) {
        withoutPipeline();
        withPipeline();
    }

    private static void withPipeline() {
        Jedis jedis = new Jedis("localhost", 6380);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Pipeline pipeline = jedis.pipelined();
            for (int j = 0; j < 100; j++) {
                pipeline.hset("hashkey_:" + i + "_" + j, "fileld" + i, "value" + i);
            }
            pipeline.syncAndReturnAll();
        }
        long end = System.currentTimeMillis();
        System.out.println("withPipeline: " + (end - begin));
    }

    private static void withoutPipeline() {
        Jedis jedis = new Jedis("localhost", 6380);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            jedis.hset("hashkey:" + i, "fileld" + i, "value" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("withoutPipeline: " + (end - begin));
    }
}