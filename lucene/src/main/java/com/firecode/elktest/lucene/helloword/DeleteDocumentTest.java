package com.firecode.elktest.lucene.helloword;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.junit.After;
import org.junit.Test;

import com.firecode.elktest.lucene.BaseDirectory;

/**
 * 删除文档相关操作
 * 
 * @author JIANG
 */
public class DeleteDocumentTest extends BaseDirectory {

	/**
	 * 索引写入对象
	 */
	private IndexWriter indexWriter;
	
	public void before() throws IOException {
		/**
		 * 索引写入相关配置
		 * @param analyzer 分词器
		 *            
		 */
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		if (exists) {
			// 在目录中创建新索引，删除以前的索引
			config.setOpenMode(OpenMode.CREATE);
		} else {
			// 将新文档添加到现有索引:
			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		this.indexWriter = new IndexWriter(directory, config);
	}
	
	/**
	 * 根据Query（查询）删除文档
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void deleteDocumentByQuery() throws IOException, ParseException {
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
		
		long deleteDocuments = indexWriter.deleteDocuments(query);
		System.err.println("删除数量："+deleteDocuments);
	}
	
	/**
	 * 根据Term（属性值）删除文档
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void deleteDocumentByTerm() throws IOException, ParseException {
		/**
		 * 删除name等于maomao的所有文档
		 */
		Term term = new Term("name","maomao");
		long deleteDocuments = indexWriter.deleteDocuments(term);
		System.err.println("删除数量："+deleteDocuments);
	}

	@After
	public void close() throws IOException {
		this.indexWriter.close();
		super.close();
	}

}
