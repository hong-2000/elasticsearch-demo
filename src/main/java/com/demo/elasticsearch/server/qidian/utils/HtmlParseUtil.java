package com.demo.elasticsearch.server.qidian.utils;

import com.demo.elasticsearch.server.qidian.entity.Goods;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hong-2000
 * @description 页面爬取
 * @create 2021/1/5 15:30
 */
@Component
@Slf4j
public class HtmlParseUtil {

    /**
     * 工具类构造函数私有化
     *
     * @return: null
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/6 11:21
     */
    private HtmlParseUtil() {
    }

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
        Document doc = new Document("");
        try {
            doc = Jsoup.parse(new URL(url + URLEncoder.encode(keyword, "UTF-8")), 20000);
        } catch (IOException e) {
            log.error("{0}", e);
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
