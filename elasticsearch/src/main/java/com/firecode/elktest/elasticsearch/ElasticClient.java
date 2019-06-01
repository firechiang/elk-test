package com.firecode.elktest.elasticsearch;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;

/**
 * @author JIANG
 */
public abstract class ElasticClient {

	protected String indexName = "user_index";
	protected RestHighLevelClient client;

	@Before
	public void init() {
		Node server001 = new Node(new HttpHost("192.168.229.133", 9200, "http"));
		Node server002 = new Node(new HttpHost("192.168.229.129", 9200, "http"));
		Node server003 = new Node(new HttpHost("192.168.229.134", 9200, "http"));
		RestClientBuilder restClientBuilder = RestClient.builder(server001, server002, server003)
				                                        .setRequestConfigCallback(new requestConfigCallback());
		client = new RestHighLevelClient(restClientBuilder);
	}
	
	/**
	 * 请求连接配置
	 * @author JIANG
	 */
	private static class requestConfigCallback implements RestClientBuilder.RequestConfigCallback{
		@Override
		public Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
			requestConfigBuilder.setConnectTimeout(3000);
			requestConfigBuilder.setSocketTimeout(3000);
			requestConfigBuilder.setConnectionRequestTimeout(6000);
			return requestConfigBuilder;
		}
	}

	@After
	public void close() throws IOException {
		client.close();
	}
}
