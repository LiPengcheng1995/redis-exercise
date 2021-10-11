package com.lpc.learn.redis.master.slave;

import com.lpc.learn.redis.master.slave.clients.RedisClientFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Package: com.lpc.learn.redis.master.slave
 * User: 李鹏程
 * Email: lipengcheng3@jd.com
 * Date: 2021/10/11
 * Time: 19:53
 * Description:
 */
@Slf4j
public class MasterWriteAndSlaveRead {
    private static final String LPC_KEY="lpc";
    private static final String LPC_VALUE="从主分片设置的值";
    public static void main(String[] args) {
        RedisClientFactory.init();
        RedisClientFactory.getMaster().set(LPC_KEY,LPC_VALUE);
        log.info("slave1 读取值,{}",RedisClientFactory.getSlave1().get(LPC_KEY));
        log.info("slave1 读取值,{}",RedisClientFactory.getSlave2().get(LPC_KEY));
        log.info("slave1 读取值,{}",RedisClientFactory.getSlave3().get(LPC_KEY));
        RedisClientFactory.destroy();
    }
}
