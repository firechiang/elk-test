package com.firecode.elktest.elasticsearch.helloword;

import java.io.IOException;

import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import com.firecode.elktest.elasticsearch.ElasticClient;

/**
 * 搜索结果建议使用scroll API，做分页
 * 
 * @author ChiangFire
 *
 */
public class SearchScrollTest extends ElasticClient {

	@SuppressWarnings("deprecation")
	@Test
	public void test() {

		SearchRequest searchRequest = new SearchRequest("user_index");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		MatchAllQueryBuilder mqb = QueryBuilders.matchAllQuery();
		searchSourceBuilder.query(mqb);
		// 一次取回多少数据
		searchSourceBuilder.size(10);
		searchRequest.source(searchSourceBuilder);
		// 设置scroll间隔
		searchRequest.scroll(TimeValue.timeValueMinutes(1L));
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			String scrollId = searchResponse.getScrollId();
			// 得到文档数组
			SearchHit[] hits = searchResponse.getHits().getHits();
			System.out.println("first scroll:");
			for (SearchHit searchHit : hits) {
				System.out.println(searchHit.getSourceAsString());
			}
			Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
			System.out.println("loop scroll:");
			while (hits != null && hits.length > 0) {
				/**
				 * 第二步，得到的scroll id 和新的scroll间隔要设置到
				 * SearchScrollRequest里，再调用searchScroll方法。 ES会返回一批带有新scroll
				 * id的查询结果。以此类推，新的scroll
				 * id可以用于子查询，来得到另一批新数据。这个过程应该在一个循环内，直到没有数据返回为止,这意味着scroll消耗殆尽，所有匹配上的数据都已经取回
				 */
				// 传入scroll id并设置间隔
				SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
				// 使用Clear scroll API来检测到最后一个scroll id
				// 来释放scroll上下文.虽然在scroll过期时，这个清理行为会最终自动触发，但是最好的实践是当scroll
				// session结束时，马上释放它
				// 设置60S的scroll存活时间
				scrollRequest.scroll(TimeValue.timeValueSeconds(60L));
				// 设置60S的scroll存活时间
				// scrollRequest.scroll("60s");
				scrollRequest.scroll(scroll);
				// 执行scroll搜索
				searchResponse = client.searchScroll(scrollRequest, RequestOptions.DEFAULT);
				// 得到本次scroll id
				scrollId = searchResponse.getScrollId();
				hits = searchResponse.getHits().getHits();
				for (SearchHit searchHit : hits) {
					System.out.println(searchHit.getSourceAsString());
				}
			}
			ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
			clearScrollRequest.addScrollId(scrollId);
			ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
			boolean succeeded = clearScrollResponse.isSucceeded();
			System.out.println("cleared:" + succeeded);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
