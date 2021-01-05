package cn.zqhblog.elasticsearch;

import cn.zqhblog.elasticsearch.dao.GoodsRepository;
import cn.zqhblog.elasticsearch.entity.Goods;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QingHong
 * @description
 * @QQ 2560612959
 * @create 2021/1/4 14:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class SpringDataEsTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    public SpringDataEsTest() {
    }

    /**
     * 一般不用这样创建索引，在实体类的注释中修改createIndex，默认就是true
     */
    @Test
    public void testIndex() {
        // 获取索引对象
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Goods.class);
        // 创建索引
        indexOperations.create();
        // 获取映射
        Document mapping = indexOperations.createMapping(Goods.class);
        // 将映射放入索引
        indexOperations.putMapping(mapping);

        boolean exists = indexOperations.exists();
        System.out.println(exists);
//        indexOperations.delete();
    }

    @Test
    public void testRepository() {
        ArrayList<Goods> goodsList = new ArrayList<>();
        goodsList.add(new Goods(1, "测试1", new BigDecimal("199"), "png"));
        goodsList.add(new Goods(2, "测试2", new BigDecimal("299"), "png"));
        goodsRepository.saveAll(goodsList);

        List<Goods> goods = goodsRepository.findByGoodsName("%测试%");
        goods.forEach(System.out::println);

        Goods byIdValue = goodsRepository.findByIdValue(1);
        System.out.println(byIdValue);
    }

    @Test
    public void testCURD() {
        // save兼顾修改
        elasticsearchRestTemplate.save(new Goods(100, "测试10", new BigDecimal("199"), "png"));

        //elasticsearchRestTemplate.delete("10", IndexCoordinates.of("shop1"));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("goodsName", "测试")).build();
        SearchHits<Goods> result = elasticsearchRestTemplate.search(searchQuery, Goods.class, IndexCoordinates.of("shop1"));
        result.getSearchHits().forEach(System.out::println);

        elasticsearchRestTemplate.delete(
                new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("goodsName", "测试")).build(),
                Goods.class,
                IndexCoordinates.of("shop1"));
    }

    @Test
    public void testSearchHighlight() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(QueryBuilders.matchQuery("goodsName", "测试"))
                .withPageable(PageRequest.of(0, 10, Sort.Direction.DESC, "goodsId"))
                //.withSort(SortBuilders.scoreSort().order(SortOrder.ASC))
                //.withSort(SortBuilders.fieldSort("goodsId").order(SortOrder.DESC))
                //.withHighlightFields(new HighlightBuilder.Field("goodsName"))
                .withHighlightBuilder(new HighlightBuilder().field("goodsName").preTags("<span style='color:red'>").postTags("</span>"))
                .build();
        SearchHits<Goods> result = elasticsearchRestTemplate.search(searchQuery, Goods.class, IndexCoordinates.of("shop1"));
//        result.getSearchHits().forEach(System.out::println);
        result.forEach(hit -> {
            System.out.println("score-->" + hit.getScore());
            System.out.println("id-->" + hit.getId());
            List<Object> sortValues = hit.getSortValues();
            sortValues.forEach(System.out::println);
            System.out.println("highlight-->" + hit.getHighlightFields().get("goodsName"));
            System.out.println(hit.getContent().toString());
        });
    }
}
