package com.lpc.learn.redis.implement.funnel;

import java.util.Date;

/**
 * Package: com.lpc.learn.redis.implement.funnel
 * User: 李鹏程
 *
 * Date: 2021/11/4
 * Time: 16:41
 * Description:
 */
public class TokenBucketLimiter {

    private int capaticy;//令牌桶容量
    private int rate;//令牌产生速率
    private int tokenAmount;//令牌数量

    public TokenBucketLimiter(int capaticy, int rate) {
        this.capaticy = capaticy;
        this.rate = rate;
        tokenAmount = capaticy;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //以恒定速率放令牌
                while (true) {
                    synchronized (this) {
                        tokenAmount++;
                        if (tokenAmount > capaticy) {
                            tokenAmount = capaticy;
                        }
                    }
                    try {
                        Thread.sleep(1000 / rate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public synchronized boolean tryAcquire(Request request) {
        if (tokenAmount > 0) {
            tokenAmount--;
            handleRequest(request);
            return true;
        } else {
            return false;
        }

    }

    /**
     * 处理请求
     *
     * @param request
     */
    private void handleRequest(Request request) {
        request.setHandleTime(new Date());
        System.out.println(request.getCode() + "号请求被处理，请求发起时间："
                + request.getLaunchTime() + ",请求处理时间：" + request.getHandleTime() + ",处理耗时："
                + (request.getHandleTime().getTime() - request.getLaunchTime().getTime()) + "ms");
    }

    /**
     * 请求类，属性只包含一个名字字符串
     */
    static class Request {
        private int code;
        private Date launchTime;
        private Date handleTime;

        private Request() {
        }

        public Request(int code, Date launchTime) {
            this.launchTime = launchTime;
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Date getLaunchTime() {
            return launchTime;
        }

        public void setLaunchTime(Date launchTime) {
            this.launchTime = launchTime;
        }

        public Date getHandleTime() {
            return handleTime;
        }

        public void setHandleTime(Date handleTime) {
            this.handleTime = handleTime;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        TokenBucketLimiter tokenBucketLimiter = new TokenBucketLimiter(5, 2);
        for (int i = 1; i <= 10; i++) {
            Request request = new Request(i, new Date());
            if (tokenBucketLimiter.tryAcquire(request)) {
                System.out.println(i + "号请求被接受");
            } else {
                System.out.println(i + "号请求被拒绝");
            }
        }
    }

}
