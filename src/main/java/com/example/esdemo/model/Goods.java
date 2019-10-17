package com.example.esdemo.model;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author Li Zhengyue
 * @date 2019/10/15
 * @since JDK1.8
 */
@Document(indexName = "es-goods", type = "docs", shards = 1, replicas = 0)
public class Goods implements Serializable {
  @Id
  private Long id;
  @Field(type = FieldType.Text, analyzer = "ik_max_word")
  private String title;
  @Field(type = FieldType.Keyword)
  private String category;
  @Field(type = FieldType.Keyword)
  private String brand;
  @Field(type = FieldType.Double)
  private Double price;
  @Field(index = false, type = FieldType.Keyword)
  private String images;

  public Goods() {
  }

  public Goods(Long id, String title, String category, String brand, Double price, String images) {
    this.id = id;
    this.title = title;
    this.category = category;
    this.brand = brand;
    this.price = price;
    this.images = images;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getImages() {
    return images;
  }

  public void setImages(String images) {
    this.images = images;
  }

  @Override public String toString() {
    return "Goods{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", category='" + category + '\'' +
        ", brand='" + brand + '\'' +
        ", price=" + price +
        ", images='" + images + '\'' +
        '}';
  }
}
