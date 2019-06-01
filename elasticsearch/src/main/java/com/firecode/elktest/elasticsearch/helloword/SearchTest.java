package com.firecode.elktest.elasticsearch.helloword;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import com.firecode.elktest.elasticsearch.ElasticClient;

/**
 * 高级查询使用
 * @author JIANG
 */
@SuppressWarnings("unused")
public class SearchTest extends ElasticClient {
	
	/**
	 * 查询全部索引全部数据
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void match_all() throws IOException {
		// 构造search request .在这里无参，查询全部索引
		SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder builder = getBuilder();
		// 增加match_all的条件
        builder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(builder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		
		// 返回状态
		RestStatus status = response.status();
		// 元数据
		TimeValue took = response.getTook();
		Boolean terminatedEarly = response.isTerminatedEarly();
		boolean timedOut = response.isTimedOut();
		//返回对象里面包含关于分片的信息和分片失败的处理
		int totalShards = response.getTotalShards();
		int successfulShards = response.getSuccessfulShards();
		int failedShards = response.getFailedShards();
		for (ShardSearchFailure failure:response.getShardFailures()) {
		    // failures should be handled here
		}
		
		//获取实际的数据
		SearchHits searchHits = response.getHits();
		SearchHit[] searchHit = searchHits.getHits();
        for (SearchHit hit:searchHit) {
            System.out.println(hit.getSourceAsString());
            //转换数据
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String documentTitle = (String) sourceAsMap.get("title");
            List<Object> users = (List<Object>) sourceAsMap.get("user");
            Map<String, Object> innerObject = (Map<String, Object>) sourceAsMap.get("innerObject");
        }
		System.err.println(response);
	}
	
	/**
	 * 查询指定索引全部数据
	 * @throws IOException
	 */
	@Test
	public void match_all_name() throws IOException {
		SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder builder = getBuilder();
		// 增加match_all的条件
        builder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(builder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		System.err.println(response);
	}
	
	
	/**
	 * 根据属性值查询
	 * @throws IOException
	 */
	@Test
	public void match() throws IOException {
		SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder builder = getBuilder();
		// 查询name=mao的数据
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", "mao");
        // 是否模糊查询
        matchQueryBuilder.fuzziness(Fuzziness.AUTO);
        // 设置前缀长度
        matchQueryBuilder.prefixLength(3); 
        // 设置最大膨胀系数 ？？？
        matchQueryBuilder.maxExpansions(10);
        builder.query(matchQueryBuilder);
		searchRequest.source(builder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		System.err.println(response);
	}
	
	
	/**
	 * 查询带排序
	 * @throws IOException
	 */
	@Test
	public void order() throws IOException {
		SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder builder = getBuilder();
        // 按照score倒序排列
        builder.sort(new ScoreSortBuilder().order(SortOrder.DESC)); 
        // 并且按照id正序排列
        //builder.sort(new FieldSortBuilder("_uid").order(SortOrder.ASC)); 
        
        //默认情况下，searchRequest返回文档内容，与REST API一样，这里你可以重写search行为。例如，你可以完全关闭"_source"检索
        builder.fetchSource(false);
		searchRequest.source(builder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		System.err.println(response);
	}
	
	/**
	 * 更细粒度的查询
	 * @throws IOException
	 */
	@Test
	public void grained() throws IOException {
		SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder builder = getBuilder();
        //默认情况下，searchRequest返回文档内容，与REST API一样，这里你可以重写search行为。例如，你可以完全关闭"_source"检索
        builder.fetchSource(false);
        //接受一个或多个通配符模式的数组，以更细粒度地控制包含或排除哪些字段
        //查询包含字段
        String[] includeFields = new String[] {"title", "user", "innerObject.*"};
        //查询过滤字段
        String[] excludeFields = new String[] {"_type"};
        builder.fetchSource(includeFields, excludeFields);
		searchRequest.source(builder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		System.err.println(response);
	}
	
	
	/**
	 * 分组查询
	 * @throws IOException
	 */
	@Test
	public void polymerization() throws IOException {
		SearchRequest searchRequest = new SearchRequest(indexName);
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("group_by_state").field("state.name");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(aggregation);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		
		//取出返回的数据
		Aggregations aggs = response.getAggregations();
        Terms byStateAggs = aggs.get("group_by_state");
        Terms.Bucket b = byStateAggs.getBucketByKey("ID"); //只取key是ID的bucket
        System.out.println(b.getKeyAsString()+","+b.getDocCount());
        System.out.println("!!!");
        List<? extends Bucket> aggList = byStateAggs.getBuckets();//获取bucket数组里所有数据
        for (Bucket bucket : aggList) {
            System.out.println("key:"+bucket.getKeyAsString()+",docCount:"+bucket.getDocCount());;
        }
		System.err.println(response);
	}
	
	
	
	
	private SearchSourceBuilder getBuilder() {
		// 大多数查询参数要写在searchSourceBuilder里
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		// 设置查询超时时间
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		// 设置从哪里开始
		sourceBuilder.from(0);
		// 每页5条
		sourceBuilder.size(5);
		return sourceBuilder;
	}

}
