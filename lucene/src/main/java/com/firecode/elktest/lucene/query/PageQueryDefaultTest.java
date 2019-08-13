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
 * 全量查询，再分页（注意：这种方式不推荐使用，效率低，因为是全量查询）
 * @author JIANG
 */
public class PageQueryDefaultTest {
	
	public static void main(String[] args) throws ParseException, IOException {
		// 当前页
		int pageNo = 0;
	    //单页显示数量
		int pageSize = 10;
		IndexSearcher indexSearcher = IndexSearchers.newInstance();
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser p = new QueryParser("content",analyzer);
		/**
		 * 查找content属性值，包含responsibility或者aa的数据（注意：空格就是or，我们在上面配的）
		 */
		Query query = p.parse("responsibility aa");
		TopDocs search = indexSearcher.search(query, 100);
		// 全量数据
		ScoreDoc[] scoreDocs = search.scoreDocs;
		// 查询到数据总数量
		long totalSize = search.totalHits.value;
		// 总页数
		long totalPage = (totalSize - 1) / pageSize + 1;
		// 开始位置
		long start = pageNo * pageSize;
		// 结束位置
		long end = (pageNo + 1) == totalPage ? totalSize : (start + pageSize);
		for(long i=start;i<end;i++){
			Document doc = indexSearcher.doc(scoreDocs[Long.valueOf(i).intValue()].doc);
			System.err.println(doc.get("name"));
		}
	}
}
