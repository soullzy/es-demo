package com.example.esdemo;

import com.example.esdemo.model.Goods;
import com.example.esdemo.repository.GoodsRepository;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsDemoApplicationTests {
  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;
  @Autowired
  private GoodsRepository goodsRepository;

  @Test
  public void contextLoads() {
  }

  @Test
  public void createIndex() {
    elasticsearchTemplate.createIndex(Goods.class);
  }

  @Test
  public void deleteIndex() {
    elasticsearchTemplate.deleteIndex(Goods.class);
  }

  @Test
  public void insert() {
    goodsRepository.save(new Goods(6L, "xiao mi redme8", "phone",
        "xiao mi", 3899.00, "http://image.baidu.com/13123.jpg"));
  }

  @Test
  public void batchInsert() {
    List<Goods> goodsList = new ArrayList<Goods>();
    goodsList.add(new Goods(1L, "小米手机7", " phone",
        "xiao mi", 3499.00, "http://image.baidu.com/13123.jpg"));
    goodsList.add(new Goods(2L, "小米手机8", " phone",
        "xiao mi", 3699.00, "http://image.baidu.com/13123.jpg"));

    goodsList.add(new Goods(3L, "华为荣耀20", "phone", "huawei", 2499.00, "http://image.baidu.com/13123.jpg"));
    goodsList.add(new Goods(4L, "iphoneX", "phone", "apple", 4299.00, "http://image.baidu.com/13123.jpg"));
    goodsList.add(new Goods(5L, "iphone11", "phone", "apple", 9799.00, "http://image.baidu.com/13123.jpg"));

    goodsRepository.saveAll(goodsList);
  }

  @Test
  public void queryAll() {
    //Iterable<Goods> list = goodsRepository.findAll();
    Iterable<Goods> list = goodsRepository.findAll(Sort.by("price").descending());
    for (Goods goods : list) {
      System.out.println(goods);
    }
  }

  @Test
  public void update() {
    goodsRepository.save(new Goods(1L, "华为META30", " 手机",
        "华为", 5499.00, "http://image.baidu.com/13123.jpg"));
  }

  @Test
  public void deleteAll() {
    goodsRepository.deleteAll();
  }

  /**
   * 自定义查询
   */

  @Test
  public void matchQuery() {
    //构建查询条件
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    //添加基本分词查询
    //queryBuilder.withQuery(QueryBuilders.matchQuery("title", "华为META30"));
    queryBuilder.withQuery(QueryBuilders.matchQuery("brand", "xiao mi"));
    Page<Goods> goodsList = goodsRepository.search(queryBuilder.build());

    // 总条数
    long total = goodsList.getTotalElements();
    System.out.println("total = " + total);
    for (Goods goods : goodsList) {
      System.out.println(goods);
    }
  }

  @Test
  public void termQuery() {
    NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
    //builder.withQuery(QueryBuilders.termQuery("price", 5499));
    builder.withQuery(QueryBuilders.termQuery("brand", "xiao mi"));
    // 查找
    Page<Goods> page = goodsRepository.search(builder.build());

    for (Goods item : page) {
      System.out.println(item);
    }
  }

  @Test
  public void booleanQuery() {
    NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

    builder.withQuery(
        QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", "华为"))
            .must(QueryBuilders.matchQuery("brand", "华为"))
    );

    Page<Goods> page = goodsRepository.search(builder.build());
    for (Goods item : page) {
      System.out.println(item);
    }
  }

  /**
   * 模糊查询
   */
  @Test
  public void fuzzyQuery() {
    NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
    builder.withQuery(QueryBuilders.fuzzyQuery("title", "iphone"));
    Page<Goods> page = goodsRepository.search(builder.build());
    for (Goods item : page) {
      System.out.println(item);
    }
  }

  @Test
  public void prefixQuery() {
    NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
    builder.withQuery(QueryBuilders.prefixQuery("title", "iphone"));
    Page<Goods> page = goodsRepository.search(builder.build());
    for (Goods item : page) {
      System.out.println(item);
    }
  }

  @Test
  public void searchByPage() {
    // 构建查询条件
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    // 添加基本分词查询
    queryBuilder.withQuery(QueryBuilders.termQuery("title", "redme8"));
    // 分页：
    int page = 0;
    int size = 5;
    queryBuilder.withPageable(PageRequest.of(page, size));

    // 搜索，获取结果
    Page<Goods> items = goodsRepository.search(queryBuilder.build());
    // 总条数
    long total = items.getTotalElements();
    System.out.println("总条数 = " + total);
    // 总页数
    System.out.println("总页数 = " + items.getTotalPages());
    // 当前页
    System.out.println("当前页：" + items.getNumber());
    // 每页大小
    System.out.println("每页大小：" + items.getSize());

    for (Goods item : items) {
      System.out.println(item);
    }
  }

  @Test
  public void searchAndSort() {
    // 构建查询条件
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    // 添加基本分词查询
    queryBuilder.withQuery(QueryBuilders.termQuery("category", "phone"));

    // 排序
    queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));

    // 搜索，获取结果
    Page<Goods> items = goodsRepository.search(queryBuilder.build());
    // 总条数
    long total = items.getTotalElements();
    System.out.println("总条数 = " + total);

    for (Goods item : items) {
      System.out.println(item);
    }
  }

  /**
   * Fielddata默认情况下禁用文本字段，因为Fielddata可以消耗大量的堆空间，特别是在加载高基数text字段时。
   * 一旦fielddata被加载到堆中，它将在该段的生命周期中保持在那里。
   * 此外，加载fielddata是一个昂贵的过程，可以导致用户体验延迟命中
   * 处理方式参考如下方式：
   * 1、可以使用使用该my_field.keyword字段进行聚合，排序或脚本
   * 2、启用fielddata（不建议使用）
   */
  @Test
  public void agg() {
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    // 不查询任何结果
    queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{ "" }, null));
    // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
    queryBuilder.addAggregation(
        AggregationBuilders.terms("brands").field("brand.keyword"));
    // 2、查询,需要把结果强转为AggregatedPage类型
    AggregatedPage<Goods> aggPage = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());
    // 3、解析
    // 3.1、从结果中取出名为brands的那个聚合，
    // 因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
    StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
    // 3.2、获取桶
    List<StringTerms.Bucket> buckets = agg.getBuckets();
    // 3.3、遍历
    for (StringTerms.Bucket bucket : buckets) {
      // 3.4、获取桶中的key，即品牌名称
      System.out.println(bucket.getKeyAsString());
      // 3.5、获取桶中的文档数量
      System.out.println(bucket.getDocCount());
    }
  }

  @Test
  public void subAgg() {
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    // 不查询任何结果
    queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{ "title" }, null));
    // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
    queryBuilder.addAggregation(
        AggregationBuilders.terms("brands").field("brand.keyword")
            .subAggregation(AggregationBuilders.avg("priceAvg").field("price")) // 在品牌聚合桶内进行嵌套聚合，求平均值
    );
    // 2、查询,需要把结果强转为AggregatedPage类型
    AggregatedPage<Goods> aggPage = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());
    // 3、解析
    // 3.1、从结果中取出名为brands的那个聚合，
    // 因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
    StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
    // 3.2、获取桶
    List<StringTerms.Bucket> buckets = agg.getBuckets();
    // 3.3、遍历
    for (StringTerms.Bucket bucket : buckets) {
      // 3.4、获取桶中的key，即品牌名称  3.5、获取桶中的文档数量
      System.out.println(bucket.getKeyAsString() + "，共" + bucket.getDocCount() + "台");

      // 3.6.获取子聚合结果：
      InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("priceAvg");
      System.out.println("平均售价：" + avg.getValue());
    }
  }
}
