package com.lpc.learn.redis.master.slave.sentinel;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * Package: com.lpc.learn.redis.master.slave.sentinel
 * User: 李鹏程
 * Email: lipengcheng3@jd.com
 * Date: 2021/10/12
 * Time: 16:06
 * Description:
 */
@Slf4j
public class MasterWriteAndSlaveRead {
    private static final String LPC_KEY="lpc";
    private static final String LPC_VALUE="从主分片设置的值";
    public static void main(String[] args) {
        //服务IP
        String ip = "127.0.0.1";
        Set<String> sentinels = new HashSet<>();
        //Sentine端口
        sentinels.add(new HostAndPort(ip, 26371).toString());
        sentinels.add(new HostAndPort(ip, 26372).toString());
        sentinels.add(new HostAndPort(ip, 26373).toString());
        JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
        log.info("current master:{}",sentinelPool.getCurrentHostMaster().toString());

        while (true){
            Jedis master = sentinelPool.getResource();
            try {
                master.set(LPC_KEY,LPC_VALUE);
                String value = master.get(LPC_KEY);
                log.info("value info:{}",value);
            }catch (Throwable e){
                log.error("加载失败,",e);
                master = sentinelPool.getResource();
            }
            master.close();
        }

//        sentinelPool.destroy();
    }

}
