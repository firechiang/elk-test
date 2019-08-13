package com.firecode.elktest.lucene.query;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 通配符搜索（注意：只能使用?和*号）
 * ? 匹配一个字符
 * * 匹配多个字符
 * @author JIANG
 */
public class WildcardQueryTest {
	
	
	@Test
	public void test() throws IOException{
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		WildcardQuery query = new WildcardQuery(new Term("name","*mao"));
		TopDocs search = indexSearcher.search(query, 10);
		
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd: scoreDocs){
			Document doc = indexSearcher.doc(sd.doc);
			System.err.println(doc.get("name"));
		}
	}
}
