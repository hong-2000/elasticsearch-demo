package com.javademo.elasticsearch.server.qidian.service;

import com.javademo.elasticsearch.server.qidian.entity.Goods;

import java.util.List;

/**
 * @author hong-2000
 * @version 1.0
 * @description
 * @email 2560612959@qq.com
 * @create 2021/1/6 13:45
 */
public interface IndexService {
    /**
     * 通过关键字获取信息
     *
     * @param keyword:
     * @return: java.util.List<com.javademo.elasticsearch.server.qidian.entity.Goods>
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/6 14:19
     */
    List<Goods> getGoods(String keyword);
}
