package com.lpc.learn.redis.make.data;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * Package: com.lpc.learn.redis.make.data
 * User: 李鹏程
 * Date: 2021/10/9
 * Time: 10:28
 * Description:
 */
@Slf4j
public class BatchAddTestData {
    private static Jedis client;
    public static void main(String[] args) {
        connectRedisAndGetClient();
        testGet();
        batchAddKeys();
        client.close();
    }

    private static void batchAddKeys(){
        String keyPrefix = "batchAddKey-20211009-";
        String resp,key,value;
        for(int i=0;i<10000;i++){
            key = keyPrefix+i;
            value = key+"-"+"value";
            resp = client.set(key,value);
            log.info("执行结果，key:{},value:{},resp:{}",key,value,resp);
        }
    }
    private static void testGet(){
        String value = client.get("lpc");
        log.info("得到的结果:{}",value);
    }

    private static void connectRedisAndGetClient(){
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        // 如果 Redis 服务设置了密码，需要下面这行，没有就不需要
        // jedis.auth("123456");
        log.error("连接成功");
        //查看服务是否运行
        log.error("服务正在运行:{}",jedis.ping());
        client = jedis;
    }
}
