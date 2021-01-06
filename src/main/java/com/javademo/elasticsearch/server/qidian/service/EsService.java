package com.javademo.elasticsearch.server.qidian.service;

import com.javademo.elasticsearch.server.qidian.entity.Goods;

import java.util.List;

/**
 * @author hong-2000
 * @version 1.0
 * @description
 * @email 2560612959@qq.com
 * @create 2021/1/6 13:12
 */
public interface EsService {
    /**
     * 保存数据到es
     *
     * @param goodsList:
     * @return: boolean
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/6 13:25
     */
    boolean saveGoods(List<Goods> goodsList);

    /**
     * 根据关键字获取信息
     *
     * @param keyword:
     * @return: void
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/6 14:23
     */
    List<Goods> getGoods(String keyword);
}
