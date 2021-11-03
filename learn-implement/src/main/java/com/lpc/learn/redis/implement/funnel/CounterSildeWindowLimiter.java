package com.lpc.learn.redis.implement.funnel;

/**
 * Package: com.lpc.learn.redis.implement.funnel
 * User: 李鹏程
 * Email: lipengcheng3@jd.com
 * Date: 2021/11/3
 * Time: 10:25
 * Description: https://juejin.cn/post/6870396751178629127
 */
public class CounterSildeWindowLimiter {

    private int windowSize; //窗口大小，【时间长短】毫秒为单位
    private int limit;//窗口内限流大小
    private int splitNum;//切分小窗口的数目大小，窗口对应的计数格子数量
    private int[] counters;//每个小窗口的计数数组
    private int index;//当前小窗口计数器的索引
    private long startTime;//窗口开始时间

    private CounterSildeWindowLimiter() {
    }

    public CounterSildeWindowLimiter(int windowSize, int limit, int splitNum) {
        this.limit = limit;
        this.windowSize = windowSize;
        this.splitNum = splitNum;
        counters = new int[splitNum];
        index = 0;
        startTime = System.currentTimeMillis();
    }

    //请求到达后先调用本方法，若返回true，则请求通过，否则限流
    public synchronized boolean tryAcquire() {
        long curTime = System.currentTimeMillis();
        //curTime - startTime， 从开始到现在过去的时间
        // curTime - windowSize - startTime ，在上面基础上减去一个窗口时间长度，因为最开始初始化的时候给足了，所以这里算累加的时候去掉这个窗口
        //
        // windowSize / splitNum，窗口时间长度/窗口占的小格子数量 = 一个格子表示的时间长度

        // Math.max(curTime - windowSize - startTime, 0) / (windowSize / splitNum) ，累增的时间/一个格子表示的时间长度，
        // 从开始到现在经历的时间要滑动多少个小格子
        long windowsNum = Math.max(curTime - windowSize - startTime, 0) / (windowSize / splitNum);//计算滑动小窗口的数量
        slideWindow(windowsNum);//滑动窗口
        int count = 0;
        for (int i = 0; i < splitNum; i++) {
            count += counters[i];
        }
        if (count >= limit) {
            return false;
        } else {
            counters[index]++;
            return true;
        }
    }

    private synchronized void slideWindow(long windowsNum) {
        if (windowsNum == 0)
            return;
        // 最多整个窗口滑动到新格子上，再多也躲不了
        long slideNum = Math.min(windowsNum, splitNum);
        for (int i = 0; i < slideNum; i++) {
            // lpctodo 这里要把最老的重制，这里是一个回环数组，后面一个就是最老的，前面一个是最近的一个。可以考虑用链表更直观
            index = (index + 1) % splitNum;
            counters[index] = 0;
        }
        startTime = startTime + windowsNum * (windowSize / splitNum);//更新滑动窗口时间
    }

    public static void main(String[] args) throws InterruptedException {
        // 10s可以进来30个，一个窗口内分成10个小窗口
        CounterSildeWindowLimiter time = new CounterSildeWindowLimiter(10000,30,10);
        for (int i=0;i<100000;i++){
            boolean res = time.tryAcquire();
            if (!res){
                Thread.sleep(500);
            }
            System.out.println();
        }
    }
//    //测试
//    public static void main(String[] args) throws InterruptedException {
//        //每秒20个请求
//        int limit = 20;
//        CounterSildeWindowLimiter counterSildeWindowLimiter = new CounterSildeWindowLimiter(1000, limit, 10);
//        int count = 0;
//
//        Thread.sleep(3000);
//        //计数器滑动窗口算法模拟100组间隔30ms的50次请求
//        System.out.println("计数器滑动窗口算法测试开始");
//        System.out.println("开始模拟100组间隔150ms的50次请求");
//        int faliCount = 0;
//        for (int j = 0; j < 100; j++) {
//            count = 0;
//            for (int i = 0; i < 50; i++) {
//                if (counterSildeWindowLimiter.tryAcquire()) {
//                    count++;
//                }
//            }
//            Thread.sleep(150);
//            //模拟50次请求，看多少能通过
//            for (int i = 0; i < 50; i++) {
//                if (counterSildeWindowLimiter.tryAcquire()) {
//                    count++;
//                }
//            }
//            if (count > limit) {
//                System.out.println("时间窗口内放过的请求超过阈值，放过的请求数" + count + ",限流：" + limit);
//                faliCount++;
//            }
//            Thread.sleep((int) (Math.random() * 100));
//        }
//        System.out.println("计数器滑动窗口算法测试结束，100组间隔150ms的50次请求模拟完成，限流失败组数：" + faliCount);
//        System.out.println("===========================================================================================");
//
//
//        //计数器固定窗口算法模拟100组间隔30ms的50次请求
//        System.out.println("计数器固定窗口算法测试开始");
//        //模拟100组间隔30ms的50次请求
//        CounterLimiter counterLimiter = new CounterLimiter(1000, limit);
//        System.out.println("开始模拟100组间隔150ms的50次请求");
//        faliCount = 0;
//        for (int j = 0; j < 100; j++) {
//            count = 0;
//            for (int i = 0; i < 50; i++) {
//                if (counterLimiter.tryAcquire()) {
//                    count++;
//                }
//            }
//            Thread.sleep(150);
//            //模拟50次请求，看多少能通过
//            for (int i = 0; i < 50; i++) {
//                if (counterLimiter.tryAcquire()) {
//                    count++;
//                }
//            }
//            if (count > limit) {
//                System.out.println("时间窗口内放过的请求超过阈值，放过的请求数" + count + ",限流：" + limit);
//                faliCount++;
//            }
//            Thread.sleep((int) (Math.random() * 100));
//        }
//        System.out.println("计数器滑动窗口算法测试结束，100组间隔150ms的50次请求模拟完成，限流失败组数：" + faliCount);
//    }

}
