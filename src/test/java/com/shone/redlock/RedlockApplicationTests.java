package com.shone.redlock;

import com.shone.redlock.service.DistributedLocker;
import com.shone.redlock.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Slf4j
class RedlockApplicationTests {

	private static final String KEY = "LOCK_KEY";

	@Autowired
	private DistributedLocker locker;

	@Autowired
	private ProductService productService;

	@Test
	void contextLoads() {
	}

	/**
	 * 测试分布式锁
	 */
	@Test
	void testLock(){

		//加锁
		locker.lock(KEY);
		log.info("获取锁");

		try {
			//模拟处理业务
			Thread.sleep(10000);

			log.info("处理业务结束");
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			//释放锁
			locker.unlock(KEY);
			log.info("释放锁");
		}
	}

	/**
	 * 测试尝试获取锁
	 */
	@Test
	void testTryLock(){
		try {
			//尝试去获取锁
			if(locker.tryLock(KEY)){
				log.info("获取锁成功");
				//模拟处理业务
				Thread.sleep(10000);
				log.info("处理业务结束");
			} else {
				log.info("获取锁失败");
			}

		} catch (Exception e){
			e.printStackTrace();
		} finally {
			//释放锁
			try {
				locker.unlock(KEY);
				log.info("释放锁成功");
			} catch (Exception e){
				log.info("释放锁失败");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 购买商品接口是异步接口
	 * 为了模拟秒杀，多个用户购买商品，商品数量顺序减少，并且不会出现负数
	 */
	@Test
	void testThreadTryLock(){
		for (int i=1; i<25; i++) {
			log.info(productService.buy("a"+ i));
		}

	}

}
