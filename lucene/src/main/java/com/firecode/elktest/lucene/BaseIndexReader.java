package com.firecode.elktest.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

/**
 * 初始化IndexReader最好做成单立
 * 
 * @author JIANG
 */
public class BaseIndexReader extends BaseDirectory {

	/**
	 * 读取索引目录对象
	 */
	protected IndexReader reader;

	public void before() throws IOException {
		/**
		 * 索引写入相关配置
		 * @param analyzer 分词器
		 */
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		if (exists) {
			// 在目录中创建新索引，删除以前的索引
			config.setOpenMode(OpenMode.CREATE);
		} else {
			// 将新文档添加到现有索引:
			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		// 读取索引目录对象
		this.reader = DirectoryReader.open(directory);
	}

	public void close() throws IOException {
		this.reader.close();
		super.close();
	}
}
