package com.firecode.elktest.lucene.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.junit.Test;

/**
 * 英文分词器 StandardAnalyzer 简单使用
 * 
 * @author JIANG
 */
public class StandardAnalyzerTest {
	
	/**
	 * 查看简单的分词信息
	 * @throws IOException
	 */
	@Test
	public void simple() throws IOException {
		String text = "Unit test for simple App";
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream("data",text);
		// 创建一个属性，将其添加到分词数据流当中，用来装分词信息（每一个Token（分词）都包含一个这样的属性）
		CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		// 开始分词之前调用（注意：重置函数必须先调用，否则会报错）
		tokenStream.reset();
		// 增加Token（就是增加分词，是否还有下一个词）
		while(tokenStream.incrementToken()) {
		    System.err.print(addAttribute+" ");
		}
		// 关闭分词器
		analyzer.close();
	}
	
	/**
	 * 查看详细的分词信息
	 * @throws IOException
	 */
	@Test
	public void display() throws IOException {
		String text = "Unit test for simple App";
		// 分词器
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream("data",text);
		// 分词数据的位置增量属性，用来装分词数据的位置增量（注：分词与分词之间的位置增量）
		PositionIncrementAttribute positionAtt = tokenStream.addAttribute(PositionIncrementAttribute.class);
		// 分词数据的偏移量属性，用来装分词数据的所在位置的偏移量
		OffsetAttribute offsetAtt = tokenStream.addAttribute(OffsetAttribute.class);
		// 分词数据属性，用来装分词数据
		CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		// 分词数据类型属性，用来装分词数据的类型
		TypeAttribute typeAtt = tokenStream.addAttribute(TypeAttribute.class);
		// 开始分词之前调用（注意：重置函数必须先调用，否则会报错）
		tokenStream.reset();
		// 增加Token（就是增加分词，是否还有下一个词）
		for(;tokenStream.incrementToken();) {
		    System.err.println("分词信息："+new StringBuilder("      ").replace(0, addAttribute.length(), addAttribute.toString())+
		    		           "，分词类型："+typeAtt.type()+
		    		           "，位置增量："+positionAtt.getPositionIncrement()+
		    		           "，数据偏移量："+offsetAtt.startOffset()+"-"+offsetAtt.endOffset());
		}
		// 关闭分词器
		analyzer.close();
	}
}
