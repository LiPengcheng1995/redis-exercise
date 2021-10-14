package com.lpc.learn.redis.cluster;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;

/**
 * Package: com.lpc.learn.redis.cluster
 * User: 李鹏程
 * Email: lipengcheng3@jd.com
 * Date: 2021/10/14
 * Time: 18:33
 * Description:
 */
@Slf4j
public class ClusterBatchWrite {
    private  static JedisCluster client;
    public static void main(String[] args) {
        RedisClusterUtil.init();
        JedisCluster cluster = RedisClusterUtil.getJedis();
        client = cluster;
        batchAddKeys();
    }

    private static void batchAddKeys(){
        String keyPrefix = "batchAddKey-20211009-";
        String resp,key,value;
        for(int i=0;i<10000000;i++){
            key = keyPrefix+i;
            value = key+"-"+"value";
            resp = client.set(key,value);
            log.info("执行结果，key:{},value:{},resp:{}",key,value,resp);
        }
    }
}
