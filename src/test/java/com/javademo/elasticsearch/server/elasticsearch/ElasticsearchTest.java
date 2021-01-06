package com.javademo.elasticsearch.server.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hong-2000
 * @email 2560612959@qq.com
 * @description
 * @create 2021/1/4 14:42
 */
public class ElasticsearchTest {
    private RestHighLevelClient client = null;

    private static final String SCHEME = "http";
    private static final String HOST_NAME = "192.168.12.160";
    private static final HttpHost[] HTTP_HOSTS = {
            new HttpHost(HOST_NAME, 9200, SCHEME),
            new HttpHost(HOST_NAME, 9201, SCHEME),
            new HttpHost(HOST_NAME, 9202, SCHEME),
    };
    private String INDEX_ONE = "index";
    private String INDEX_TWO = "index2";

    @Before
    public void connect() {
        client = new RestHighLevelClient(RestClient.builder(HTTP_HOSTS));
    }

    @After
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }

    @Test
    public void testCreate() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王五五");
        map.put("age", "18");
        map.put("addr", "beijing");
        IndexRequest request = new IndexRequest().index(INDEX_TWO).id("12").source(map);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    public void testGet() throws IOException {
        GetRequest request = new GetRequest().index(INDEX_ONE).id("5");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    public void testUpdate() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "lisa");
        map.put("age", "20");
        map.put("addr", "beijing");
        UpdateRequest request = new UpdateRequest().index(INDEX_ONE).id("5").doc(map);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    public void testDelete() throws IOException {
        DeleteRequest request = new DeleteRequest().index(INDEX_ONE).id("5");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    /**
     * 批量查询
     *
     * @return: void
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/6 10:01
     */
    @Test
    public void testBatchCURD() throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest().index(INDEX_ONE).id("6").source(XContentType.JSON, "name", "tom", "age", 18, "addr", "wuhan"));
        request.add(new IndexRequest().index(INDEX_ONE).id("7").source(XContentType.JSON, "name", "linda", "age", 21, "addr", "wuhan"));
        request.add(new UpdateRequest().index(INDEX_ONE).id("6").doc(XContentType.JSON, "addr", "shanghai"));
        request.add(new DeleteRequest().index(INDEX_ONE).id("5"));
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    /**
     * search搜索
     *
     * @return: void
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/6 10:02
     */
    @Test
    public void testSearch() throws IOException {
        System.out.println(client.search(new SearchRequest().indices(INDEX_ONE, INDEX_TWO).source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery())), RequestOptions.DEFAULT));
    }

    /**
     * 匹配查询
     *
     * @return: void
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/6 10:02
     */
    @Test
    public void testSearchMatch() throws IOException {
        // 指定索引库
        SearchRequest request = new SearchRequest(INDEX_ONE, INDEX_TWO);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 关键字
        String key = "wuhan";
        String key2 = "18";
        // 匹配查询
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(key2, "addr", "age"));
        request.source(searchSourceBuilder);
        // 执行查询请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 输出
        long value = response.getHits().getTotalHits().value;
        System.out.println(value);
        Arrays.stream(response.getHits().getHits()).forEach(hit -> {
            System.out.println(hit.getIndex());
            System.out.println(hit.getId());
            System.out.println(hit.getScore());
            System.out.println(hit.getSourceAsMap().get("name"));
            System.out.println(hit.getSourceAsMap().get("age"));
            System.out.println(hit.getSourceAsMap().get("addr"));
        });
    }

    /**
     * 匹配、分页、排序
     *
     * @return: void
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/6 10:02
     */
    @Test
    public void testSearchPage() throws IOException {
        // 指定索引库
        SearchRequest request = new SearchRequest(INDEX_ONE, INDEX_TWO);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 关键字
        String key = "18";
        // 匹配查询
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(key, "age"));
        // 分页开始索引
        int index = 1;
        // 分页大小
        int size = 1;
        // 分页查询
        searchSourceBuilder.from(index).size(size);
        // 排序
        searchSourceBuilder.sort(SortBuilders.scoreSort().order(SortOrder.ASC));
        //searchSourceBuilder.sort(SortBuilders.fieldSort("_id").order(SortOrder.DESC));
        request.source(searchSourceBuilder);
        // 执行查询请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 输出
        long value = response.getHits().getTotalHits().value;
        System.out.println("total-->" + value);
        Arrays.stream(response.getHits().getHits()).forEach(hit -> {
            System.out.println("index-->" + hit.getIndex());
            System.out.println("id-->" + hit.getId());
            System.out.println("score-->" + hit.getScore());
            hit.getSourceAsMap().forEach((k, v) -> System.out.println(k + "-->" + v));
        });
    }

    /**
     * 高亮
     *
     * @return: void
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/6 10:03
     */
    @Test
    public void testSearchHighlight() throws IOException {
        // 指定索引库
        SearchRequest request = new SearchRequest(INDEX_ONE, INDEX_TWO);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 关键字
        String key = "王五";
        // 匹配查询
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(key, "name"));
        // 高亮字段
        String field = "name";
        // 高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder().field(field).preTags("<span style='color:red'>").postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        request.source(searchSourceBuilder);
        // 执行查询请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 输出
        long value = response.getHits().getTotalHits().value;
        System.out.println("total-->" + value + "\n================");
        Arrays.stream(response.getHits().getHits()).forEach(hit -> {
            System.out.println("index-->" + hit.getIndex());
            System.out.println("id-->" + hit.getId());
            System.out.println("score-->" + hit.getScore());
            hit.getSourceAsMap().forEach((k, v) -> System.out.println(k + "-->" + v));
            System.out.println("highlight-->" + hit.getHighlightFields().get(field).fragments()[0]);
            System.out.println("============");
        });
    }

}
