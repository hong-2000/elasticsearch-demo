package com.demo.elasticsearch.server.springdataes.dao;

import com.demo.elasticsearch.server.springdataes.entity.Goods;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author hong-2000
 * @description
 * @create 2021/1/4 14:42
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Integer> {
    /**
     * 通过商品名获取信息
     *
     * @param name
     * @return
     */
    List<Goods> findByGoodsName(String name);

    /**
     * 根据id值查找产品
     *
     * @param id
     * @return
     */
    @Query("{\"match\":{\"goodsId\":{\"query\":\"?0\"}}}")
    Goods findByIdValue(Integer id);
}
