package com.firecode.elktest.lucene;

import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;

/**
 * 索引数据读取对象 IndexReader 相关工具类
 * 注意：IndexReader 最好是单立的，以防止资源消耗
 * @author JIANG
 */
public class IndexReaders {
	
	private static IndexReader indexReader;
	
	/**
	 * 获取读取数据的 IndexReader 对象
	 * @return
	 */
	public static final IndexReader get(){
		
		if(null == indexReader){
			synchronized (IndexReaders.class) {
				if(null == indexReader){
					// 索引目录
					Directory directory = Directorys.openNIOFSDirectory();
					// 打开读取索引目录对象
					try {
						indexReader =  DirectoryReader.open(directory);
						
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		//return DirectoryReader.openIfChanged((DirectoryReader) indexReader);  基于某个indexReader打开一个新的Reader管道
		return indexReader;
	}
	
}
