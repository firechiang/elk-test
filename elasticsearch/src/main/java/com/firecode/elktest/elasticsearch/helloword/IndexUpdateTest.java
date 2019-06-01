package com.firecode.elktest.elasticsearch.helloword;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.Test;

import com.firecode.elktest.elasticsearch.ElasticClient;

/**
 * 数据修改测试
 * 
 * @author JIANG
 *
 */
public class IndexUpdateTest extends ElasticClient {

	/**
	 * 同步修改数据
	 * @throws IOException
	 */
	@Test
	public void update() throws IOException {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("createDate", new Date());
		jsonMap.put("name", "update maowen");
		UpdateRequest updateRequest = new UpdateRequest(indexName, "2").doc(jsonMap);
		UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
	    System.out.println("数据修改成功：" + update);
	}
	
	/**
	 * 异步修改数据
	 * @throws IOException
	 */
	@Test
	public void updateAsync() throws IOException {
		Thread currentThread = Thread.currentThread();
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("createDate", new Date());
		jsonMap.put("name", "save maowen");
		UpdateRequest updateRequest = new UpdateRequest(indexName, "2").doc(jsonMap);
		client.updateAsync(updateRequest, RequestOptions.DEFAULT, new ActionListener<UpdateResponse>(){

			@Override
			public void onResponse(UpdateResponse response) {
		    	System.out.println("异步修改成功："+response);
		    	LockSupport.unpark(currentThread);
			}

			@Override
			public void onFailure(Exception e) {
				System.err.println("异步修改失败："+e);
				LockSupport.unpark(currentThread);
			}
			
		});
		LockSupport.park();
	}
	
	/**
	 * 批量修改数据
	 * 
	 * @throws IOException
	 */
	@Test
	public void updateAll() throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("name", i + random.nextInt(100));
			jsonMap.put("createDate", new Date());
			jsonMap.put("deptName", "第" + i + "工程部");
			UpdateRequest updateRequest = new UpdateRequest(indexName, ""+i).doc(jsonMap);
			bulkRequest.add(updateRequest);
		}
		BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		System.err.println("批量修改数据："+response);
	}
}
