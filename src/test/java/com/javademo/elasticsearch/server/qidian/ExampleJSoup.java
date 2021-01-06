package com.javademo.elasticsearch.server.qidian;

import com.javademo.elasticsearch.ElasticsearchApplication;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author hong-2000
 * @email 2560612959@qq.com
 * @description
 * @create 2021/1/4 14:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
@Slf4j
public class ExampleJSoup {

    @Test
    public void testJSoup() throws IOException {
        String url = "https://www.baidu.com/";
        Document doc = Jsoup.connect(url).get();
        log.info(doc.title());
        log.info(doc.outerHtml());
        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            log.info("{}\n\t{}", headline.attr("title"), headline.absUrl("href"));
        }
    }
}
