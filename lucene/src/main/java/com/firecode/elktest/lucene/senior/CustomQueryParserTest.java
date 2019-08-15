package com.firecode.elktest.lucene.senior;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 自定义QueryParser查询，简单使用
 * @author JIANG
 */
public class CustomQueryParserTest {
	
	@Test
	public void test() throws ParseException, IOException {
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		CustomQueryParser p = new CustomQueryParser("content",analyzer);
	    /**
	     * 解析表达式，生成对应的Query（模糊查询以responsibility开头的数据）
	     */
	    Query query = p.parse("responsibility~");
	    
		TopDocs search = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd : scoreDocs){
			Document doc = indexSearcher.doc(sd.doc);
			System.err.println(doc.get("name"));
		}
	}
	
	/**
	 * 自定义QueryParser查询
	 * 注意：QueryParser实际是将表达式解析成对应的Query
	 * @author 37982
	 *
	 */
	private static class CustomQueryParser extends QueryParser {

		public CustomQueryParser(String f, Analyzer a) {
			super(f, a);
		}
		
		@Override
		protected Query getFuzzyQuery(String field, String termStr, float minSimilarity) throws ParseException {
			System.err.println("正在使用模糊查询搜索");
			return super.getFuzzyQuery(field, termStr, minSimilarity);
		}
	}
}
