package com.demo.elasticsearch.server.qidian.service.impl;

import com.demo.elasticsearch.server.qidian.entity.Goods;
import com.demo.elasticsearch.server.qidian.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hong-2000
 * @version 1.0
 * @description
 * @create 2021/1/6 13:12
 */
@Service
@Slf4j
public class EsServiceImpl implements EsService {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    public EsServiceImpl(ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    @Override
    public boolean saveGoods(List<Goods> goodsList) {
        if (ObjectUtils.isEmpty(elasticsearchRestTemplate.save(goodsList))) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Goods> getGoods(String keyword) {
        return elasticsearchRestTemplate.search(
                new NativeSearchQueryBuilder()
                        .withQuery(QueryBuilders.multiMatchQuery(keyword, "name", "author", "intro"))
                        .build(),
                Goods.class,
                IndexCoordinates.of("qi-dian-books"))
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
