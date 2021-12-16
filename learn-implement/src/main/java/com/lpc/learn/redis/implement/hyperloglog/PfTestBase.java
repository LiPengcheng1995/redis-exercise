package com.lpc.learn.redis.implement.hyperloglog;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Package: com.lpc.learn.redis.implement.hyperloglog
 * User: 李鹏程
 * Date: 2021/12/14
 * Time: 17:39
 * Description:
 */
@Slf4j
public class PfTestBase {
    static class BitKeeper {
        private int maxbits;

        public void random() {
            long value = ThreadLocalRandom.current().nextLong(1L << 32);
            int bits = lowZeros(value);
            if (bits > this.maxbits) {
                this.maxbits = bits;
            }
        }

        private int lowZeros(long value) {
            int i = 1;

            for (; i < 32; i++) {
                if (value >> i << i != value) {
                    break;
                }
            }
            return i - 1;
        }
    }

    static class Experiment {
        private int n;
        private BitKeeper keeper;

        public Experiment(int n) {
            this.n = n;
            this.keeper = new BitKeeper();
        }

        public void work() {
            for (int i = 0; i < n; i++) {
                this.keeper.random();
            }
        }

        public void debug() {
            log.info("实验次数：{} ,log2(n): {} , 低位连续0: {}", this.n,
                    new BigDecimal(Math.log(this.n) / Math.log(2)).setScale(2, BigDecimal.ROUND_UP).doubleValue(),
                    this.keeper.maxbits);

//            log.info("{},{}",
//                    new BigDecimal(Math.log(this.n) / Math.log(2)).setScale(2, BigDecimal.ROUND_UP).doubleValue(),
//                    this.keeper.maxbits);
        }
    }

    public static void main(String[] args) {
        for (int i = 1000; i < 10000; i += 1000) {
            Experiment exp = new Experiment(i);
            exp.work();
            exp.debug();
        }
    }
}
