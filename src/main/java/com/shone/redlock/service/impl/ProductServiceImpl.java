package com.shone.redlock.service.impl;

import com.shone.redlock.service.DistributedLocker;
import com.shone.redlock.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 模拟秒杀商品减库存
 *
 * @author Xiao GuoJian
 * @date 2019-11-06 16:56
 */
@Slf4j
@Service("productService")
public class ProductServiceImpl implements ProductService {

    private final DistributedLocker locker;

    private final static String PRODUCT_KEY = "product";

    public ProductServiceImpl(DistributedLocker locker){
        this.locker = locker;
    }

    private Integer counts = 20;

    /**
     * 购买商品
     * @param name 姓名
     * @return 结果
     */
    @Override
    @Async
    public String buy(String name){
        String msg;
        locker.lock(PRODUCT_KEY);
        try{
            log.info(name + "获取锁成功");
            if(counts == 0){
                //如果库存小于等于0，则说明已经卖完了
                return name+" 购买商品，但是已经卖完啦";
            }
            //模拟耗时100毫秒
            Thread.sleep(100);

            //如果商品没卖完，则数据库数量减一
            msg = name + " 购买第"+counts+"个商品成功";
            counts = counts - 1;
            return msg;
        } catch (Exception e){
            log.info(name + "尝试获取锁异常");
            e.printStackTrace();
        } finally {
            //释放锁
            try {
                locker.unlock(PRODUCT_KEY);
                log.info(name + "释放锁成功");
            } catch (Exception e){
                log.info(name + "释放锁失败");
//                e.printStackTrace();
            }
        }
        return name+" 购买商品失败";
    }
}
