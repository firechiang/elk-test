package com.firecode.elktest.lucene.query;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.After;
import org.junit.Test;

import com.firecode.elktest.lucene.BaseDirectory;

/**
 * TermQuery查询简单使用（注意：这是精确查询）
 * 
 * @author JIANG
 */
public class TermQueryTest extends BaseDirectory {
	
	/**
	 * 读取索引目录对象
	 */
	private IndexReader reader;

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

	/**
	 * 查询文档
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void searchDocument() throws IOException, ParseException {
		
		System.err.println("文档总数量："+reader.maxDoc());
		System.err.println("存储的文档数："+reader.numDocs());
		// 构建索引查询对象
		IndexSearcher searcher = new IndexSearcher(reader);
		/**
		 * 查询name等于maomao的所有文档
		 */
		TermQuery query = new TermQuery(new Term("name", "maomao"));
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
			System.err.println("name："+doc.get("name")+"，age："+doc.get("age"));
		}
	}

	@After
	public void close() throws IOException {
		this.reader.close();
		super.close();
	}
	
	
}
