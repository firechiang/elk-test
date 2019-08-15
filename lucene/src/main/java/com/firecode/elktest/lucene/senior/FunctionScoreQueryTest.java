package com.firecode.elktest.lucene.senior;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 评分搜索简单使用
 * @author JIANG
 */
public class FunctionScoreQueryTest {
	
	@Test
	public void test() throws ParseException, IOException {
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser p = new QueryParser("content",analyzer);
		/**
		 * 查找content属性值，包含responsibility或者aa的数据（注意：空格默认是or）
		 */
		Query q = p.parse("responsibility aa");
		/**
		 * 评分属性只能是 NumericDocValuesField 的类型
		 */
		FunctionScoreQuery query = new FunctionScoreQuery(q,DoubleValuesSource.fromDoubleField("age1"));
		TopDocs search = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd:scoreDocs) {
		    Document doc = indexSearcher.doc(sd.doc);
		    System.err.println("name："+doc.get("name")+"，age1："+doc.get("age1"));
		}
	}

}
