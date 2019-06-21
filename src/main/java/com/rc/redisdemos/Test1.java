package com.rc.redisdemos;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName com.rc.redisdemos.Test1
 * @Description 分布式主键自增
 * @Author liux
 * @Date 19-6-10 上午9:37
 * @Version 1.0
 */
public class Test1 {
    public static void main(String[] args) {
        List<Map<String, List<Long>>> lists = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new Worker(countDownLatch, lists));
            thread.setName("线程-" + (i + 1));
            thread.start();
            countDownLatch.countDown();
        }
    }
}

//构造能够同时开始的竞态环境以利于充分竞争
class Worker implements Runnable {

    private final CountDownLatch countDownLatch;
    private List<Map<String, List<Long>>> lists;

    Worker(CountDownLatch countDownLatch, List<Map<String, List<Long>>> lists) {
        this.countDownLatch = countDownLatch;
        this.lists = lists;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
            Jedis jedis = new Jedis("localhost", 6380);
            jedis.get("myId");
            Map<String, List<Long>> map = new HashMap<>();
            List<Long> list = new ArrayList<>();
            int i = 0;
            while (i < 10) {
                list.add(jedis.incr("myId"));
                i++;
            }
            map.put(Thread.currentThread().getName(), list);
            lists.add(map);
            String key = map.keySet().iterator().next();
            System.out.println(key + map.get(key));
            jedis.disconnect();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}