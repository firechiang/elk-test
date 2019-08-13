package com.firecode.elktest.lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSLockFactory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.NIOFSDirectory;

/**
 * 索引目录相关工具类
 * 注意：Directory 最好是单立的，以防止资源消耗
 * 
 * @author JIANG
 */
public class Directorys {

	private static final Path path = Paths.get("d:\\lucene\\data", "project");

	/**
	 * 内存级索引目录
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Directory openMMapDirectory() {
		try {
			/**
			 * 创建内存级索引目录
			 * 
			 * @param path         数据落地磁盘的路劲或文件
			 * @param lockFactory  锁工厂
			 * @param maxChunkSize 单个数据文件最大大小
			 */
			// new MMapDirectory(path,FSLockFactory.getDefault(),MMapDirectory.DEFAULT_MAX_CHUNK_SIZE);
			return MMapDirectory.open(path, FSLockFactory.getDefault());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 内存级索引目录
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Directory openNIOFSDirectory() {
		try {
			/**
			 * 创建磁盘级索引
			 * 
			 * @param path        数据落地磁盘的路劲或文件
			 * @param lockFactory 锁工厂
			 *            
			 */
			return NIOFSDirectory.open(path, FSLockFactory.getDefault());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 关闭索引目录
	 * @param directory
	 */
	public static void close(Directory directory) {
		if(null != directory){
			try {
				directory.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
