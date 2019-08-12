package com.firecode.elktest.lucene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSLockFactory;
import org.apache.lucene.store.NIOFSDirectory;
import org.junit.Before;

/**
 * 索引目录相关
 * @author JIANG
 */
public class BaseDirectory {
	
	/**
	 * 索引目录
	 */
	protected Directory directory;
	
	protected boolean exists = false;
	
	@Before
	public void init() throws IOException {
		Path path = Paths.get("d:\\lucene\\data", "project");
		// 如果数据目录已存在，直接返回索引目录
		if (Files.exists(path)) {
            this.exists = true;
		}
		/**
		 * 创建内存级索引目录
		 * 
		 * @param path         数据落地磁盘的路劲或文件
		 * @param lockFactory  锁工厂
		 * @param maxChunkSize 单个数据文件最大大小
		 *            
		 */
		//this.directory  = MMapDirectory.open(path, FSLockFactory.getDefault(), MMapDirectory.DEFAULT_MAX_CHUNK_SIZE);
		/**
		 * 创建磁盘级索引
		 * 
		 * @param path         数据落地磁盘的路劲或文件
		 * @param lockFactory  锁工厂
		 */
		this.directory = NIOFSDirectory.open(path, FSLockFactory.getDefault());
		
		this.before();
	}
	
	public void close() throws IOException {
		this.directory.close();
	}
	
	protected void before() throws IOException {}
		
		
	

}
