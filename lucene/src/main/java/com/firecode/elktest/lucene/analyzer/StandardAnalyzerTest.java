package com.firecode.elktest.lucene.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

/**
 * 英文分词器 StandardAnalyzer 简单使用
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
		// 创建一个属性，将其添加到分词数据流当中，用来装分词信息（每一个Token（分词）都包含一个这样的属性）
		CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		// 开始分词之前调用（注意：重置函数必须先调用，否则会报错）
		tokenStream.reset();
		// 增加Token（是否还有下一个词）
		while(tokenStream.incrementToken()) {
		    System.err.print(addAttribute+" ");
		}
		// 关闭分词器
		analyzer.close();
	}
}
