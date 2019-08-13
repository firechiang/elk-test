package com.firecode.elktest.lucene.query;

import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.After;
import org.junit.Test;

import com.firecode.elktest.lucene.BaseDirectory;

/**
 * 数字和日期索引简单使用
 * @author JIANG
 */
public class NumberDocumentTest extends BaseDirectory {

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
	 * 添加文档
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void addDocument() throws IOException, ParseException {
		// 文档对象
		Document document = new Document();
		NumericDocValuesField intField = new NumericDocValuesField("age",1);
		NumericDocValuesField dateField = new NumericDocValuesField("date",new Date().getTime());
		document.add(intField);
		document.add(dateField);
		// 建立索引
		indexWriter.addDocument(document);
	}

	@After
	public void close() throws IOException {
		this.indexWriter.close();
		super.close();
	}

}
