package com.firecode.elktest.elasticsearch.helloword;

import java.io.IOException;

import org.elasticsearch.Build;
import org.elasticsearch.Version;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.cluster.ClusterName;
import org.junit.Test;

import com.firecode.elktest.elasticsearch.ElasticClient;

/**
 * 查询集群、节点相关的信息
 * 
 * @author JIANG
 *
 */
public class IndexInfoTest extends ElasticClient {

	@Test
	public void test() throws IOException {
		MainResponse response = client.info(RequestOptions.DEFAULT);
		ClusterName clusterName = response.getClusterName();
		String clusterUuid = response.getClusterUuid();
		String nodeName = response.getNodeName();
		Version version = response.getVersion();
		Build build = response.getBuild();
		System.out.println("cluster name:" + clusterName);
		System.out.println("cluster uuid:" + clusterUuid);
		System.out.println("node name:" + nodeName);
		System.out.println("node version:" + version);
		System.out.println("node name:" + nodeName);
		System.out.println("build info:" + build);
	}

}
