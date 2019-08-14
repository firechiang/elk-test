package com.firecode.elktest.lucene.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.junit.Test;

/**
 * 自定义英文分词器简单使用
 * @author JAING
 */
public class CustomAnalyzerTest {
	
	
	@Test
	public void test() {
	    String[] datas = "Unit test for simple App".split(" ");
		CustomAnalyzer analyzer = new CustomAnalyzer(datas);
		System.err.println(analyzer);
	}
	
	
	/**
	 * 自定义英文分词器
	 * @author JAING
	 */
	private static class CustomAnalyzer extends Analyzer {
		/**
		 * 分词数据源对象
		 */
		private CharArraySet stops;
		
		public CustomAnalyzer(String[] stopWords) {
			/**
			 * 通过一组数据生成分词数据源对象
			 * @param stopWords   一组数据
			 * @param ignoreCase  是否忽略大小写
			 */
			this.stops = StopFilter.makeStopSet(stopWords, true);
		}
		
		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
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
			 * 
			 * @param tokenStream           
			 * @param stopWords
			 */
			StopFilter stopFilter = new StopFilter(lowerCaseFilter, stops);
			TokenStreamComponents tokenStreamComponents = new TokenStreamComponents(standardTokenizer,stopFilter);
			return tokenStreamComponents;
		}
	}
}
