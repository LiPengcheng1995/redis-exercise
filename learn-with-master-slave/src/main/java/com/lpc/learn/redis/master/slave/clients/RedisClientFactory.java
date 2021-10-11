package com.lpc.learn.redis.master.slave.clients;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

/**
 * Package: com.lpc.learn.redis.master.slave.clients
 * User: 李鹏程
 * Email: lipengcheng3@jd.com
 * Date: 2021/10/11
 * Time: 18:40
 * Description:
 */
@Slf4j
public class RedisClientFactory {
    private static Jedis master;
    private static Jedis slave1;
    private static Jedis slave2;
    private static Jedis slave3;

    public static void init(){
        master = new Jedis("localhost", 6370);
        log.error("master 服务正在运行:{}", master.ping());

        slave1 = new Jedis("localhost", 6371);
        log.error("slave1 服务正在运行:{}", slave1.ping());

        slave2 = new Jedis("localhost", 6372);
        log.error("slave2 服务正在运行:{}", slave2.ping());

        slave3 = new Jedis("localhost", 6373);
        log.error("slave3 服务正在运行:{}", slave3.ping());
    }

    public static void destroy(){
        master.close();
        slave1.close();
        slave2.close();
        slave3.close();
    }

    public static Jedis getMaster() {
        return master;
    }

    public static Jedis getSlave1() {
        return slave1;
    }

    public static Jedis getSlave2() {
        return slave2;
    }

    public static Jedis getSlave3() {
        return slave3;
    }
}
