package com.javademo.elasticsearch.server.qidian.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author hong-2000
 * @email 2560612959@qq.com
 * @description 商品实体
 * @create 2021/1/5 15:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "qidian_books", shards = 5, replicas = 1, createIndex = true)
public class Goods {
    @Id
    private Integer id;

    /**
     * 图片
     */
    @Field(type = FieldType.Keyword)
    private String img;

    /**
     * 最细粒度划分
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Text)
    private String author;

    /**
     * 图书简介
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String intro;
}
