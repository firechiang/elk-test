package com.firecode.elktest.elasticsearch.helloword;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse.ShardInfo;
import org.elasticsearch.action.support.replication.ReplicationResponse.ShardInfo.Failure;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import com.firecode.elktest.elasticsearch.ElasticClient;

/**
 * 数据插入测试
 * @author JIANG
 */
public class IndexSaveTest extends ElasticClient {

	/**
	 * 自动生成ID并同步插入数据
	 * 
	 * @throws IOException
	 */
	@Test
	public void save1() throws IOException {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("name", "maowen");
		jsonMap.put("createDate", new Date());
		jsonMap.put("deptName", "集团总部");
		// 插入Map数据
		IndexRequest indexRequest = new IndexRequest(indexName).source(jsonMap);
		// 插入JSON字符串数据
		// IndexRequest indexRequest = new
		// IndexRequest(indexName).source(jsonString, XContentType.JSON);
		IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
		
		
		//-------------------------返回值信息解析-------------------------------//
		System.out.println("索引名称："+response.getIndex());
		System.out.println("当前数据ID："+response.getId());
		System.out.println("当前数据版本："+response.getVersion());
		System.out.println("当前操作状态："+response.status());
		//判断当前操作是否创建操作
		if (response.getResult() == Result.CREATED) {
		} 
		//判断当前操作是否修改操作
		if (response.getResult() == Result.UPDATED) {
		}
		ShardInfo shardInfo = response.getShardInfo();
		//对分片使用的判断
		if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
		    
		}
		if (shardInfo.getFailed() > 0) {
		    for (Failure failure : shardInfo.getFailures()) {
		    	System.out.println("原因："+failure.reason());
		    }
		}
		System.err.println(response);
	}

	/**
	 * 手动指定ID并同步插入数据
	 * 
	 * @throws IOException
	 */
	@Test
	public void save2() throws IOException {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("name", "jiang");
		jsonMap.put("createDate", new Date());
		jsonMap.put("deptName", "研发部");
		// 插入Map数据
		IndexRequest indexRequest = new IndexRequest(indexName).id("1").source(jsonMap);
		// 插入JSON字符串数据
		// IndexRequest indexRequest = new IndexRequest(indexName).id("1").source(jsonString,XContentType.JSON);
		IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
		System.err.println(response);

	}

	/**
	 * 以 XContentBuilder 的方式构建数据并插入
	 * 
	 * @throws IOException
	 */
	@Test
	public void save3() throws IOException {
		XContentBuilder builder = XContentFactory.jsonBuilder();
		builder.startObject();
		{
			builder.field("name", "kimchy");
			builder.field("createDate", new Date());
			builder.field("deptName", "行政部");
		}
		builder.endObject();
		IndexRequest indexRequest = new IndexRequest(indexName).source(builder);
		IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
		System.err.println(response);
	}

	/**
	 * 批量同步插入数据
	 * 
	 * @throws IOException
	 */
	@Test
	public void savesAll() throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("name", i + random.nextInt(100));
			jsonMap.put("createDate", new Date());
			jsonMap.put("deptName", "第" + i + "工程部");
			IndexRequest request = new IndexRequest(indexName).source(jsonMap);
			bulkRequest.add(request);
		}
		BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		System.err.println(response);
	}
	
	/**
	 * 异步插入数据
	 * @throws IOException 
	 */
	@Test
	public void asyncSave() throws IOException {
		Thread currentThread = Thread.currentThread();
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("name", "maowj");
		jsonMap.put("createDate", new Date());
		jsonMap.put("deptName", "houqing");
		// 插入Map数据
		IndexRequest indexRequest = new IndexRequest(indexName).source(jsonMap);
		// 插入JSON字符串数据
		// IndexRequest indexRequest = new
		// IndexRequest(indexName).source(jsonString, XContentType.JSON);
		client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
		    @Override
			public void onFailure(Exception e) {
		    	System.err.println("异步插入失败");
		    	LockSupport.unpark(currentThread);
			}
			@Override
			public void onResponse(IndexResponse response) {
				System.err.println("异步插入成功："+response);
				LockSupport.unpark(currentThread);
			}
		});
		LockSupport.park();
	}
}
