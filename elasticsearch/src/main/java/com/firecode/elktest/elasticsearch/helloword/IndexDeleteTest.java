package com.firecode.elktest.elasticsearch.helloword;

import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.Test;

import com.firecode.elktest.elasticsearch.ElasticClient;

/**
 * 数据删除测试
 * 
 * @author JIANG
 *
 */
public class IndexDeleteTest extends ElasticClient {

	/**
	 * 根据ID同步删除
	 * @throws IOException
	 */
	@Test
	public void deleteByID() throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest(indexName).id("1");
		DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
		System.err.println(delete);
	}
	
	
	/**
	 * 根据ID异步删除
	 * @throws IOException
	 */
	@Test
	public void asyncDeleteById() throws IOException {
		Thread currentThread = Thread.currentThread();
		DeleteRequest deleteRequest = new DeleteRequest(indexName).id("1");
		client.deleteAsync(deleteRequest, RequestOptions.DEFAULT, new ActionListener<DeleteResponse>() {
		    @Override
			public void onFailure(Exception e) {
		    	System.err.println("异步删除失败："+e);
		    	LockSupport.unpark(currentThread);
			}
			@Override
			public void onResponse(DeleteResponse response) {
				System.out.println("异步删除成功："+response);
				LockSupport.unpark(currentThread);
			}
		});
		LockSupport.park();
	}
	
	/**
	 * 批量删除数据
	 * 
	 * @throws IOException
	 */
	@Test
	public void deleteAll() throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		for (int i = 0; i < 5; i++) {
			DeleteRequest deleteRequest = new DeleteRequest(indexName, ""+i);
			bulkRequest.add(deleteRequest);
		}
		BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		System.err.println("批量删除数据成功："+response);
	}
}
