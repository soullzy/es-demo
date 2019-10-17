# es note

###document
https://www.elastic.co/guide/en/elasticsearch/reference/current/elasticsearch-intro.html

###api document
https://www.elastic.co/guide/en/elasticsearch/client/index.html
	
###ik analysis
https://github.com/medcl/elasticsearch-analysis-ik/releases

###note
[参考文档1](https://blog.csdn.net/chen_2890/article/details/83895646)
[参考文档2](https://blog.csdn.net/yejingxuan01/article/details/96312333)

###常用操作
* （1）统计某个字段的数量
  *  ValueCountBuilder vcb=  AggregationBuilders.count("count_uid").field("uid");
* （2）去重统计某个字段的数量（有少量误差）
  *  CardinalityBuilder cb= AggregationBuilders.cardinality("distinct_count_uid").field("uid");
* （3）聚合过滤
  * FilterAggregationBuilder fab= AggregationBuilders.filter("uid_filter").filter(QueryBuilders.queryStringQuery("uid:001"));
* （4）按某个字段分组
  * TermsBuilder tb=  AggregationBuilders.terms("group_name").field("name");
* （5）求和
  * SumBuilder  sumBuilder=	AggregationBuilders.sum("sum_price").field("price");
* （6）求平均
  * AvgBuilder ab= AggregationBuilders.avg("avg_price").field("price");
* （7）求最大值
  * MaxBuilder mb= AggregationBuilders.max("max_price").field("price"); 
* （8）求最小值
  * MinBuilder min=	AggregationBuilders.min("min_price").field("price");
* （9）按日期间隔分组
  * DateHistogramBuilder dhb= AggregationBuilders.dateHistogram("dh").field("date");
* （10）获取聚合里面的结果
  * TopHitsBuilder thb=  AggregationBuilders.topHits("top_result");
* （11）嵌套的聚合
  * NestedBuilder nb= AggregationBuilders.nested("negsted_path").path("quests");
* （12）反转嵌套
  *AggregationBuilders.reverseNested("res_negsted").path("kps ");

#示例  
```java
TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
        .addTransportAddress(new TransportAddress(InetAddress.getByName("host1"), 9300))
        .addTransportAddress(new TransportAddress(InetAddress.getByName("host2"), 9300));

client.close();

```
