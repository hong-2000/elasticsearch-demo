package com.demo.elasticsearch.server.qidian.service.impl;

import com.demo.elasticsearch.server.qidian.entity.Goods;
import com.demo.elasticsearch.server.qidian.service.EsService;
import com.demo.elasticsearch.server.qidian.service.IndexService;
import com.demo.elasticsearch.server.qidian.utils.HtmlParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hong-2000
 * @version 1.0
 * @description
 * @create 2021/1/6 14:15
 */
@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    private final EsService esService;

    public IndexServiceImpl(EsService esService) {
        this.esService = esService;
    }

    @Override
    public List<Goods> getGoods(String keyword) {
        // 从网站获取信息，再保存到es
        esService.saveGoods(HtmlParseUtil.getHtmlByJsoup(keyword));
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            log.error("{0}", e);
        }
        // 从es获取信息
        return esService.getGoods(keyword);
    }
}
