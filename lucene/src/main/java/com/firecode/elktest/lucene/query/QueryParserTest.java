package com.firecode.elktest.lucene.query;

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

import com.firecode.elktest.lucene.IndexSearchers;

/**
 * QueryParser通用查询
 * 注意：QueryParser实际是将表达式解析成对应的Query
 * @author JIANG
 */
public class QueryParserTest {
	
	
	public static void main(String[] args) throws ParseException, IOException {
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
		//Query query = p.parse("responsibility aa");
	    
	    /**
	     * 查询content属性值包含responsibility aa的所有数据（注意：双引号包着的表示一段语句）
	     */
	    //Query query = p.parse("\"responsibility aa\"");
	    
	    /**
	     * 查找name属性值等于maomao的所有数据
	     */
	    //Query query = p.parse("name:maomao");
	    
	    /**
	     * 查找name属性值，以mao开头的所有数据
	     */
	    //Query query = p.parse("name:mao*");
	    
	    /**
	     * 查找name属性值，以mao结尾的所有数据（注意：这个要开启前置匹配，否则会报错，我们在上面开启了）
	     */
	    //Query query = p.parse("name:*mao");
	    
	    /**
	     * 查询name属性不等于maomao，或者content属性值里面包含responsibility的所有数据（+号取正，-号取反）
	     */
	    //Query query = p.parse("- name:maomao + responsibility");
	    
	    /**
	     * 查询age属性值在1到33之间的数据，相当于in（注意：TO必须是大写，如果是数字的值要存储成String才能匹配得到）
	     */
	    //Query query = p.parse("age:[1 TO 33]");
	    
	    /**
	     * 查询content属性值包含aaa或者responsibility，并且name属性值等于maomao所有数据
	     */
	    //Query query = p.parse("(aaa OR responsibility) AND name:maomao");
	    
	    /**
	     * 查询content属性值包含aaa或者responsibility，并且name属性值不等于maomao所有数据
	     */
	    //Query query = p.parse("(aaa OR responsibility) AND NOT name:maomao");
	    
	    /**
	     * 查询content属性值里面to和check距离一个单词的所有数据（注意：这个匹配规则好像不是这样的）
	     */
	    //Query query = p.parse("\"to check\"~1");
	    
	    /**
	     * 查询name属性值以maom开头的所有数据（注意：这个其实是模糊查询，而且只支持前置匹配，后置会报错）
	     */
	    Query query = p.parse("name:maom~");
	    
		TopDocs search = indexSearcher.search(query, 10);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc sd : scoreDocs){
			Document doc = indexSearcher.doc(sd.doc);
			System.err.println(doc.get("name"));
		}
	}
}
