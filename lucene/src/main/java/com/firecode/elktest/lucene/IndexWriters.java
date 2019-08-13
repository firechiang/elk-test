package com.firecode.elktest.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;

/**
 * 索引数据写入对象 IndexWriter 相关工具类
 * 注意：IndexReader 最好是单立的，以防止资源消耗
 * @author JIANG
 */
public class IndexWriters {
	
	
	private static IndexWriter indexWriter;
	
	/**
	 * 获取写入数据的 IndexWriter 对象
	 * @return
	 */
	public static final IndexWriter newInstance() {
		
		if(null == indexWriter){
			synchronized (IndexWriters.class) {
				if(null == indexWriter){
					// 索引目录
					Directory directory = Directorys.openNIOFSDirectory();
					/**
					 * 索引写入相关配置
					 * @param analyzer 分词器
					 */
					IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
					/**
					 * OpenMode.CREATE            在目录中创建新索引，删除以前的索引
					 * OpenMode.CREATE_OR_APPEND  将新文档添加到现有索引
					 */
					config.setOpenMode(OpenMode.CREATE_OR_APPEND);
					try {
						return new IndexWriter(directory, config);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return indexWriter;
	}
	
}
