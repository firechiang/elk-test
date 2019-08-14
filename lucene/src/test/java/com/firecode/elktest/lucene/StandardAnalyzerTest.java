package com.firecode.elktest.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

/**
 * 英文分词器 StandardAnalyzer 测试
 * 
 * @author JIANG
 */
public class StandardAnalyzerTest {
	
	
	@Test
	public void test() throws IOException {
		String text = "Unit test for simple App";
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream("data",text);
		CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		// 开始分词之前调用（注意：重置函数必须先调用，否则会报错）
		tokenStream.reset();
		// 增加Token（是否还有下一个词）
		while(tokenStream.incrementToken()) {
		    System.err.println(addAttribute);
		}
		// 关闭分词器
		analyzer.close();
	}
}
