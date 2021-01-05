package cn.zqhblog.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author QingHong
 * @description
 * @QQ 2560612959
 * @create 2021/1/4 13:55
 */
@Document(indexName = "shop1", shards = 5, replicas = 1, createIndex = true)
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    private Integer goodsId;

    /**
     * 商品名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String goodsName;

    /**
     * 商品价格
     */
    @Field(type = FieldType.Double)
    private BigDecimal marketPrice;

    /**
     * 商品图片
     */
    @Field(type = FieldType.Keyword)
    private String originalImg;

    public Goods() {
    }

    public Goods(Integer goodsId, String goodsName, BigDecimal marketPrice, String originalImg) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.marketPrice = marketPrice;
        this.originalImg = originalImg;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", marketPrice=" + marketPrice +
                ", originalImg='" + originalImg + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Goods goods = (Goods) o;
        return Objects.equals(goodsId, goods.goodsId) &&
                Objects.equals(goodsName, goods.goodsName) &&
                Objects.equals(marketPrice, goods.marketPrice) &&
                Objects.equals(originalImg, goods.originalImg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, goodsName, marketPrice, originalImg);
    }
}
