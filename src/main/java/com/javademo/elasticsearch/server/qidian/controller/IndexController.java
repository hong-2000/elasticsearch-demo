package com.javademo.elasticsearch.server.qidian.controller;

import com.javademo.elasticsearch.server.qidian.entity.Goods;
import com.javademo.elasticsearch.server.qidian.service.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hong-2000
 * @email 2560612959@qq.com
 * @description 业务管理
 * @create 2021/1/5 15:47
 */
@ResponseBody
@Controller
public class IndexController {

    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * 后续可引入依赖，运用注解注释
     *
     * @param keyword: 搜索的关键字
     * @return: java.util.List<com.javademo.elasticsearch.server.qidian.entity.Goods>
     * @author hong-2000
     * @date 2021/1/6 9:53
     */
    @GetMapping({"/{keyword}", "index/{keyword}"})
    public List<Goods> testParseController(@PathVariable(name = "keyword") String keyword) {
        List<Goods> goodsList = new ArrayList<>();
        // 后续可引入依赖 hibernate-validator，运用注解校验
        int lenMax = 20;
        int lenMin = 0;
        if (ObjectUtils.isEmpty(keyword) || keyword.length() <= lenMin || keyword.length() >= lenMax) {
            return goodsList;
        }
        goodsList = indexService.getGoods(keyword);
        return goodsList;
    }
}
