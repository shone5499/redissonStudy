package com.shone.redlock;


import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * DemoClass
 *
 * @author Xiao GuoJian
 * @date 2019-11-06 10:18
 */

@Slf4j
public class RedissonLockTest {

    //尝试加锁的超时时间
    private final static Long timeout = 300L;
    //锁过期时间
    private final static Long expire = 10000L;

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("root");

        RedissonClient redisson = Redisson.create(config);
        log.info("连接redis");

        //定义锁
        RLock lock = redisson.getLock("myLock");

        try {
            //获取锁
            if(lock.tryLock(timeout,expire, TimeUnit.MILLISECONDS)){
                //获取锁成功
                log.info("获取锁成功！");
                log.info("do something");
                Thread.sleep(10000);
                log.info("使用完毕！");
            } else {
                //获取锁失败
                log.info("加锁失败");
                log.info("其他处理！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放锁
            try {
                lock.unlock();
                log.info("释放锁成功");
            } catch (Exception e){
                log.info("释放锁失败");
                e.printStackTrace();
            }
        }

        //关闭redis连接
        redisson.shutdown();
        log.info("关闭redis连接成功");

    }
}
