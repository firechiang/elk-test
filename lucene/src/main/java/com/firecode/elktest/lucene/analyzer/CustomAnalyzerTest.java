package com.firecode.elktest.lucene.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

/**
 * 自定义英文分词器简单使用
 * 
 * @author JAING
 */
public class CustomAnalyzerTest {

	@Test
	public void test() throws IOException {
		String text = "Unit test for simple App";
		CustomAnalyzer analyzer = new CustomAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream("data", text);
		// 创建一个属性，将其添加到分词数据流当中，用来装分词信息（每一个Token（分词）都包含一个这样的属性）
		CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		// 开始分词之前调用（注意：重置函数必须先调用，否则会报错）
		tokenStream.reset();
		// 增加Token（就是增加分词，是否还有下一个词）
		while (tokenStream.incrementToken()) {
			System.err.println(addAttribute + " ");
		}
		// 关闭分词器
		analyzer.close();
	}

	/**
	 * 自定义英文分词器
	 * 
	 * @author JAING
	 */
	private static class CustomAnalyzer extends Analyzer {

		/**
		 * 分词核心实现
		 */
		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			
			String[] stopWords = fieldName.split(" ");
			/**
			 * 通过一组数据生成分词数据源对象
			 * @param stopWords  一组数据
			 * @param ignoreCase 是否忽略大小写
			 */
			CharArraySet stops = StopFilter.makeStopSet(stopWords, true);
			/**
			 * 将数据分词成为一个个的短语或单词的对象
			 */
			StandardTokenizer standardTokenizer = new StandardTokenizer();
			/**
			 * 分词过滤器（这个是将分词数据转换为小写）
			 * @param standardTokenizer 分词对象
			 */
			LowerCaseFilter lowerCaseFilter = new LowerCaseFilter(standardTokenizer);
			/**
			 * 停用词过滤器
			 * @param tokenStream
			 * @param stopWords
			 */
			StopFilter stopFilter = new StopFilter(lowerCaseFilter, stops);
			TokenStreamComponents tokenStreamComponents = new TokenStreamComponents(standardTokenizer, stopFilter);
			return tokenStreamComponents;
		}
	}
}
