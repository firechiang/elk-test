package com.firecode.elktest.lucene.query;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 范围查询简单使用（注意：这个适用于String类型字段，Number类型需转换为String再使用）
 * @author JIANG
 */
public class TermRangeTest {
	
	
	@Test
	public void test() throws IOException{
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		/**
		 * field        属性名
		 * lowerTerm    范围结束
		 * upperTerm    范围开始
		 * includeLower 是否包含最低下的
		 * includeUpper 是否包含最上面的
		 */
		TermRangeQuery query = new TermRangeQuery("age", new BytesRef("10"), new BytesRef("35"), true, true);
		TopDocs search = indexSearcher.search(query, 10);
		TotalHits totalHits = search.totalHits;
		System.err.println("查询到总记录数："+totalHits.value);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd: scoreDocs){
			Document doc = indexSearcher.doc(sd.doc);
			// 打印name的值
			System.err.println(doc.get("name"));
		}
	}
}
