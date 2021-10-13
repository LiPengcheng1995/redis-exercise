package com.lpc.learn.redis.master.slave;

import com.lpc.learn.redis.master.slave.clients.RedisClientFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Package: com.lpc.learn.redis.master.slave
 * User: 李鹏程
 * Email: 
 * Date: 2021/10/11
 * Time: 19:59
 * Description: 失败，只能在主片里写
 */
@Slf4j
public class SlaveWrite {
    private static final String LPC_KEY="lpc";
    private static final String LPC_VALUE="从主分片设置的值1";
    public static void main(String[] args) {
        RedisClientFactory.init();
        RedisClientFactory.getSlave1().set(LPC_KEY,LPC_VALUE);
        log.info("slave1 读取值,{}",RedisClientFactory.getSlave1().get(LPC_KEY));
        log.info("slave1 读取值,{}",RedisClientFactory.getSlave2().get(LPC_KEY));
        log.info("slave1 读取值,{}",RedisClientFactory.getSlave3().get(LPC_KEY));
        RedisClientFactory.destroy();

    }
}
