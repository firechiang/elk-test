package com.firecode.elktest.elasticsearch.helloword;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.Test;

import com.firecode.elktest.elasticsearch.ElasticClient;

/**
 * 数据获取测试
 * 
 * @author JIANG
 *
 */
public class IndexGetTest extends ElasticClient {

	/**
	 * 根据ID同步查询
	 * @throws IOException
	 */
	@Test
	public void queryById() throws IOException {
		GetRequest getRequest = new GetRequest(indexName).id("1");
		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
		//如果数据存在
		if(getResponse.isExists()){
			Map<String, Object> maps = getResponse.getSource();
			for(String key:maps.keySet()){
				System.err.println(key+": "+maps.get(key));
			}
		}
	}
	
	/**
	 * 根据ID异步查询
	 * @throws IOException
	 */
	@Test
	public void asyncQueryById() throws IOException {
		Thread currentThread = Thread.currentThread();
		GetRequest getRequest = new GetRequest(indexName).id("1");
		client.getAsync(getRequest, RequestOptions.DEFAULT, new ActionListener<GetResponse>() {
		    @Override
			public void onFailure(Exception e) {
		    	System.err.println("异步查询失败");
		    	LockSupport.unpark(currentThread);
			}
			@Override
			public void onResponse(GetResponse response) {
				System.out.println("异步查询成功："+response);
				LockSupport.unpark(currentThread);
			}
		});
		LockSupport.park();
	}
}
