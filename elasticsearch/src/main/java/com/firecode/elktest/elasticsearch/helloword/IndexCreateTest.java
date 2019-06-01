package com.firecode.elktest.elasticsearch.helloword;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.Test;

import com.firecode.elktest.elasticsearch.ElasticClient;

/**
 * 创建索引测试
 * @author JIANG
 */
public class IndexCreateTest extends ElasticClient {

	/**
	 * 创建索引
	 * 
	 * @throws IOException
	 */
	@Test
	public void createIndex() throws IOException {
        if(!exists(indexName)){
    		CreateIndexRequest request = new CreateIndexRequest(indexName);
    		Map<String, Object> settings = new HashMap<>();
    		// 切片数2
    		settings.put("number_of_shards", 2);
    		// 副本数2
    		settings.put("number_of_replicas", 2);
    		request.settings(settings);
    		CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
    		System.err.println("是否创建成功："+response.isAcknowledged());
        }else{
            System.err.print("索引："+indexName+"已存在");
        }
	}
	
	
	
	/**
	 * 索引是否存在
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	private boolean exists(String indexName) throws IOException {
		GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        return client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
	}
}
