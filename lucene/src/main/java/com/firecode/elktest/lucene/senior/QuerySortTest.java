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
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 搜索排序简单使用
 * @author JIANG
 */
public class QuerySortTest {
	
	@Test
	public void test() throws ParseException, IOException {
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser p = new QueryParser("content",analyzer);
		/**
		 * 查找content属性值，包含responsibility或者aa的数据（注意：空格默认是or）
		 */
		Query query = p.parse("responsibility aa");
		/**
		 * 根据name属性值排序（注意：要同时按多个字段排序，传入多个SortField即可）
         * @param field   排序字段
         * @param type    排序类型（文档，评分等等）
         * @param reverse 是否降序
		 */
		Sort sort = new Sort(new SortField("name", SortField.Type.DOC,true));
		/**
		 * Sort.INDEXORDER 以索引序号排序
		 * Sort.RELEVANCE  默认排序
		 */
		TopDocs search = indexSearcher.search(query, 10,sort);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd:scoreDocs) {
		    Document doc = indexSearcher.doc(sd.doc);
		    System.err.println(doc.get("name"));
		}
	}

}
