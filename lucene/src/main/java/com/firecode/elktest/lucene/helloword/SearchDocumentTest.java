package com.firecode.elktest.lucene.helloword;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSLockFactory;
import org.apache.lucene.store.NIOFSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 索引相关操作简单使用
 * 
 * @author JIANG
 */
public class SearchDocumentTest {

	/**
	 * 读取索引目录对象
	 */
	private IndexReader reader;
	/**
	 * 索引目录
	 */
	private Directory directory;
	
	private boolean create = false;

	@Before
	public void init() throws IOException {
		// 创建索引目录
		getDirectory();
		/**
		 * 索引写入相关配置
		 * @param analyzer 分词器
		 *            
		 */
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		if (create) {
			// 在目录中创建新索引，删除以前的索引
			config.setOpenMode(OpenMode.CREATE);
		} else {
			// 将新文档添加到现有索引:
			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		// 读取索引目录对象
		this.reader = DirectoryReader.open(directory);
	}

	/**
	 * 获取索引目录
	 * 
	 * @return
	 * @throws IOException
	 */
	public void getDirectory() throws IOException {
		Path path = Paths.get("d:\\lucene\\data", "project");
		// 如果数据目录已存在，直接返回索引目录
		if (Files.exists(path)) {
            this.create = true;
		}
		/**
		 * 创建内存级索引目录
		 * 
		 * @param path         数据落地磁盘的路劲或文件
		 * @param lockFactory  锁工厂
		 * @param maxChunkSize 单个数据文件最大大小
		 *            
		 */
		//this.directory  = MMapDirectory.open(path, FSLockFactory.getDefault(), MMapDirectory.DEFAULT_MAX_CHUNK_SIZE);
		/**
		 * 创建磁盘级索引
		 * 
		 * @param path         数据落地磁盘的路劲或文件
		 * @param lockFactory  锁工厂
		 */
		this.directory = NIOFSDirectory.open(path, FSLockFactory.getDefault());
	}

	/**
	 * 查询文档
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void searchDocument() throws IOException, ParseException {
		// 构建索引查询对象
		IndexSearcher searcher = new IndexSearcher(reader);
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		/**
		 * 构建查询解析器
		 * 
		 * @param fieldName 要匹配文档里面的哪个字段
		 * @param analyzer  分词器
		 *           
		 */
		QueryParser parser = new QueryParser("name", analyzer);
		// 查询内容包含 previous 的文档
		Query query = parser.parse("maomao");
		/**
		 * 查询文档
		 * 
		 * @param query 查询对象
		 * @param topN  取几条
		 *            
		 */
		TopDocs tds = searcher.search(query, 10);
		// 所有文档的评分对象
		ScoreDoc[] scoreDocs = tds.scoreDocs;
		for (ScoreDoc sd : scoreDocs) {
			// 根据ID获取文档
			Document doc = searcher.doc(sd.doc);
			// 获取文档的属性值
			System.err.println(doc.get("name"));
		}
	}

	@After
	public void close() throws IOException {
		this.reader.close();
	}

}
