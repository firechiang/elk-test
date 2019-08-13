package com.firecode.elktest.lucene.query;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 前缀搜索
 * @author JIANG
 */
public class PrefixQueryTest {
	
	
	@Test
	public void test() throws IOException{
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		/**
		 * 搜索name以mao开头的所有数据
		 */
		PrefixQuery query = new PrefixQuery(new Term("name","mao"));
		TopDocs search = indexSearcher.search(query, 10);
		
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd: scoreDocs){
			Document doc = indexSearcher.doc(sd.doc);
			System.err.println(doc.get("name"));
		}
	}

}
