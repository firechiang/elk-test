package com.firecode.elktest.lucene.query;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 模糊查询
 * @author JIANG
 */
public class FuzzyQueryTest {
	
	@Test
	public void test() throws IOException {
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		/**
		 * term           要匹配的数据
		 * maxEdits      
		 * prefixLength   匹配程度
		 * maxExpansions
		 * transpositions
		 */
		FuzzyQuery query = new FuzzyQuery(new Term("name","maomao"));
		TopDocs search = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd:scoreDocs){
			Document doc = indexSearcher.doc(sd.doc);
			System.err.println(doc.get("name"));
		}
	}

}
