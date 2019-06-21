package com.rc.redisdemos;

/**
 * @ClassName RedisService
 * @Description Redis 服务接口类
 * @Author liux
 * @Date 19-6-19 下午11:31
 * @Version 1.0
 */
public interface RedisService {

    String get(String key);

    String set(String key, String value);
}
