package com.shone.redlock.service;

import lombok.extern.slf4j.Slf4j;

/**
 * 模拟秒杀商品减库存
 *
 * @author Xiao GuoJian
 * @date 2019-11-06 16:56
 */
public interface ProductService {

    /**
     * 购买商品
     * @param name 姓名
     * @return 结果
     */
    String buy(String name);
}
