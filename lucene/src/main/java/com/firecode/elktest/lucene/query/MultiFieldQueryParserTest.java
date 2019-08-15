package com.firecode.elktest.lucene.query;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 同时查询多个属性简单使用
 * @author JAING
 */
public class MultiFieldQueryParserTest {
	
	
	@Test
	public void test() throws ParseException, IOException{
	    IndexSearcher indexSearcher = IndexSearchers.newInstance();
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser p = new QueryParser("content",analyzer);
		// 用空格隔开的数据的搜索关系
	    p.setDefaultOperator(Operator.OR);
	    // 是否开启前置通配符匹配，默认关闭（不建议开启，影响效率）
	    p.setAllowLeadingWildcard(true);
	    /**
	     * @param fields   属性数组
	     * @param analyzer 分词器
	     */
		MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(new String[]{"content","name"},analyzer);
		/**
		 * 查找content属性值，包含responsibility或者aa的数据（注意：空格就是or，我们在上面配的）
		 */
		Query query = multiFieldQueryParser.parse("responsibility aa");
		
		TopDocs search = indexSearcher.search(query,10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd:scoreDocs){
			Document doc = indexSearcher.doc(sd.doc);
			System.err.println("name："+doc.get("name")+"，age："+doc.get("age"));
		}
	}
}
