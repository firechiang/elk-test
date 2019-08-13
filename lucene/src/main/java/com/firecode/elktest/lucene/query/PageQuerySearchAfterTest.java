package com.firecode.elktest.lucene.query;

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

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 分页查询（注意：这种分页方式要记录每一页最后的那个文档标识（推荐使用））
 * @author JIANG
 */
public class PageQuerySearchAfterTest {
	
	public static void main(String[] args) throws ParseException, IOException {
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser p = new QueryParser("content",analyzer);
		/**
		 * 查找content属性值，包含responsibility或者aa的数据（注意：空格就是or，我们在上面配的）
		 */
		Query query = p.parse("responsibility aa");
		/**
		 * after    从那个文档标识后面开始检索
		 * query    检索
		 * numHits  返回多少条数据
		 */
		TopDocs search = indexSearcher.searchAfter(null, query, 10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(int i=0;i<scoreDocs.length;i++) {
			Document doc = indexSearcher.doc(scoreDocs[i].doc);
			System.err.println(doc.get("name"));
		}
	}
}
