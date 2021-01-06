package com.javademo.elasticsearch.server.qidian.utils;

import com.javademo.elasticsearch.server.qidian.entity.Goods;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hong-2000
 * @email 2560612959@qq.com
 * @description 页面爬取
 * @create 2021/1/5 15:30
 */
@Component
@Slf4j
public class HtmlParseUtil {
    /**
     * 爬取页面的html进行解析
     *
     * @param keyword: 搜索关键字
     * @return: java.util.List<com.javademo.elasticsearch.server.qidian.entity.Goods>
     * @author hong-2000
     * @date 2021/1/6 9:55
     */
    public static List<Goods> getHtmlByJsoup(String keyword) {
        List<Goods> goodsList = new ArrayList<>();
        String url = "https://www.qidian.com/search?kw=";
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(url + keyword), 20000);
        } catch (IOException e) {
            log.error("{}", e);
        }
        Elements elements = doc.getElementById("result-list").getElementsByTag("li");
        elements.forEach(element -> goodsList.add(Goods.builder()
                .name(element.getElementsByTag("h4").eq(0).text())
                .author(ObjectUtils.isEmpty(element.getElementsByClass("name").text()) ? element.getElementsByTag("i").text() : element.getElementsByClass("name").text())
                .intro(element.getElementsByClass("intro").eq(0).text())
                .img(element.getElementsByTag("img").eq(0).attr("src"))
                .build())
        );
        return goodsList;
    }
}
