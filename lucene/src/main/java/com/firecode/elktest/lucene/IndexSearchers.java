package com.firecode.elktest.lucene;

import org.apache.lucene.search.IndexSearcher;

/**
 * 索引数据查询对象 IndexSearcher 相关工具类
 * @author JIANG
 */
public class IndexSearchers {
	
	
	/**
	 * 创建一个索引查询对象
	 * @return
	 */
	public static final IndexSearcher newInstance(){
		
		return new IndexSearcher(IndexReaders.get());
	}
}
