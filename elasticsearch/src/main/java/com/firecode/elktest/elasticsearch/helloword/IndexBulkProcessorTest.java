package com.firecode.elktest.elasticsearch.helloword;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import com.firecode.elktest.elasticsearch.ElasticClient;

/**
 * 使用线程池的方式添加或删除数据
 * @author JIANG
 *
 */
public class IndexBulkProcessorTest extends ElasticClient {

	@Test
	public void test() throws InterruptedException {
		BulkProcessor.Listener listener = new BulkProcessor.Listener() {
			/**
			 * 在每次bulk request发出前执行,在这个方法里面可以知道在本次批量操作中有多少操作数
			 */
			@Override
			public void beforeBulk(long executionId, BulkRequest request) {
				//int numberOfActions = request.numberOfActions();
			}
			/**
			 * 每次批量请求结束后执行，可以在这里知道是否有错误发生。
			 */
			@Override
			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
				if (response.hasFailures()) {
					System.err.println("执行失败");
				} else {
					System.out.println("执行成功耗时：" + response.getTook().getMillis());
				}
			}

			/**
			 * 如果发生错误就会调用
			 */
			@Override
			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
				System.err.println("发生错误");
			}

		};
		// 使用builder做批量操作的控制
		BulkProcessor.Builder builder = BulkProcessor.builder((request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener), listener);
		// 执行多少次动作后刷新bulk.默认1000，-1禁用
		builder.setBulkActions(500);
		// 执行的动作大小超过多少时，刷新bulk。默认5M，-1禁用
		builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
		// 最多允许多少请求同时执行。默认是1，0是只允许一个。
		builder.setConcurrentRequests(0);
		// 设置刷新bulk的时间间隔。默认是不刷新的。
		builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
		// 设置补偿机制参数。由于资源限制（比如线程池满），批量操作可能会失败，在这定义批量操作的重试次数。
		builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));
		
		// 使用调用build()方法构造bulkProcessor,在底层实际上是用了bulk的异步操作
		BulkProcessor bulkProcessor = builder.build();
		
		
		// 新建三个插入数据的请求
		IndexRequest one = new IndexRequest(indexName).source(XContentType.JSON, "name", "jobbna");
		IndexRequest two = new IndexRequest(indexName).source(XContentType.JSON, "name", "sticsearch");
		IndexRequest three = new IndexRequest(indexName).source(XContentType.JSON, "name", "mao");
		// 将请求加入到上面配置好的bulkProcessor里面。
		bulkProcessor.add(one);
		bulkProcessor.add(two);
		bulkProcessor.add(three);
		// bulkProcess必须被关闭才能使上面添加的操作生效
		// 立即关闭
		bulkProcessor.close();
		// 在规定的时间内关闭，是否所有批量操作完成。全部完成，返回true,未完成返//回false
		// boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
	}

}
