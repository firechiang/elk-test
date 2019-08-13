package com.firecode.elktest.lucene.query;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 多个条件组合查询
 * @author JIANG
 */
public class BooleanQueryTest {
	
	@Test
	public void test() throws IOException{
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		BooleanQuery query = new BooleanQuery.Builder()
				                             /**
				                              * Occur.MUST     表示必须出现（类似于and）
				                              * Occur.SHOULD   表示可以出现（类似于or）
				                              * Occur.MUST_NOT 表示不等于（类似于and）
				                              */
				                             .add(new TermQuery(new Term("name", "maomao")), Occur.MUST)
				                             .add(new TermQuery(new Term("content", "field")), Occur.MUST)
				                             .build();
		
		TopDocs search = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd : scoreDocs){
			Document doc = indexSearcher.doc(sd.doc);
			System.err.println(doc.get("name"));
		}
	}

}
