package com.firecode.elktest.lucene.senior;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * 高亮显示简单使用
 * @author JIANG
 */
public class HighlighterTest {
	
	@Test
	public void test() throws ParseException, IOException, InvalidTokenOffsetsException {
	    IndexSearcher indexSearcher = IndexSearchers.newInstance();
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser p = new QueryParser("content",analyzer);
		// 用空格隔开的数据的搜索关系
	    p.setDefaultOperator(Operator.OR);
	    // 是否开启前置通配符匹配，默认关闭（不建议开启，影响效率）
	    p.setAllowLeadingWildcard(true);
		/**
		 * 查找content属性值，包含responsibility或者aa的数据（注意：空格就是or，我们在上面配的）
		 */
		Query query = p.parse("responsibility aa");
		
		TopDocs search = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd:scoreDocs) {
		    Document doc = indexSearcher.doc(sd.doc);
		    String content = doc.get("content");
			Fragmenter fragmenter = new SimpleFragmenter();
			QueryScorer queryScorer = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter("<span>","</span>"),queryScorer);
			highlighter.setTextFragmenter(fragmenter);
			// 获取高亮后的数据
			String bestFragment = highlighter.getBestFragment(analyzer, null,content);
			System.out.println(bestFragment);
		}
	}
}
