package com.example.esdemo.repository;

import com.example.esdemo.model.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Li Zhengyue
 * @date 2019/10/16
 * @since JDK1.8
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
