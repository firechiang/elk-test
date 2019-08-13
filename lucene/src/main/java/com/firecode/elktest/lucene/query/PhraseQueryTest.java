package com.firecode.elktest.lucene.query;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 短语查询
 * 注意：使用短语查询的数据必须使用 IndexOptions.DOCS_AND_FREQS_AND_POSITIONS（就是文档 词频 位置都被索引），因为短语查询需要使用位置索引
 *       
 * @author JIANG
 */
public class PhraseQueryTest {
	
	@Test
	public void test() throws IOException{
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		PhraseQuery query = new PhraseQuery.Builder()
				                           /**
				                            * 跳数（一个单词为一跳）
				                            * 数据：for a previous  
				                            * 从上面的数据可以得出：for到previous中间有一跳a
				                            */
				                           .setSlop(1)
				                           /**
				                            * 跳，开始位置的数据
				                            */
				                           .add(new Term("content","for"))
				                           /**
				                            * 跳，结束位置的数据
				                            */
				                           .add(new Term("content","previous"))
				                           .build();
		TopDocs search = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd: scoreDocs){
			Document doc = indexSearcher.doc(sd.doc);
			System.err.println(doc.get("name"));
		}
	}

}
