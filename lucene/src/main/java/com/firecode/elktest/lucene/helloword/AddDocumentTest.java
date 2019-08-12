package com.firecode.elktest.lucene.helloword;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSLockFactory;
import org.apache.lucene.store.NIOFSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 查询文档相关操作简单使用
 * 
 * @author JIANG
 */
public class AddDocumentTest {

	/**
	 * 索引写入对象
	 */
	private IndexWriter indexWriter;
	/**
	 * 索引目录
	 */
	private Directory directory;
	
	private boolean create = false;

	@Before
	public void init() throws IOException {
		// 创建索引目录
		getDirectory();
		/**
		 * 索引写入相关配置
		 * @param analyzer 分词器
		 *            
		 */
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		if (create) {
			// 在目录中创建新索引，删除以前的索引
			config.setOpenMode(OpenMode.CREATE);
		} else {
			// 将新文档添加到现有索引:
			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		this.indexWriter = new IndexWriter(directory, config);
	}

	/**
	 * 获取索引目录
	 * 
	 * @return
	 * @throws IOException
	 */
	public void getDirectory() throws IOException {
		Path path = Paths.get("d:\\lucene\\data", "project");
		// 如果数据目录已存在，直接返回索引目录
		if (Files.exists(path)) {
            this.create = true;
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
	}

	/**
	 * 添加文档
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void addDocument() throws IOException, ParseException {
		String filePath = Thread.currentThread().getContextClassLoader().getResource("helloword_document.txt").getPath();
		byte[] readAllBytes = Files.readAllBytes(new File(filePath).toPath());
		String content = new String(readAllBytes, StandardCharsets.UTF_8);
		// 文档对象
		Document document = new Document();
		FieldType fieldType = new FieldType();
		// 数据存储到文件当中（注意：这个一定要是true否则报错）
		fieldType.setStored(true);
		/**
		 * DOCS                                      只有文档会被索引，词频和位置都会被省略
         * DOCS_AND_FREQS                            文档和词频被索引，位置省略
         * DOCS_AND_FREQS_AND_POSITIONS              文档 词频 位置都被索引
         * DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS  除了文档和词频位置还有偏移量也会被索引
         * NONE                                      不索引
         *
         * 有些字段我们需要在查询的时候返回，但不希望它进入索引影响查询效率，
         * 可以用setIndexOptions设为False，同时有些词我们只需要对其进行过滤即可，
         * 比如权限和时间过滤，这种值我们不太需要记录其出现的频率（词频）和位置（偏移量），所以只需要DOCS级别即可，
         * 通常对于要索引的字段我们都设置为DOCS_AND_FREQS_AND_POSITIONS
		 */
		//fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		/**
		 * @param name  属性名称
		 * @param value 属性值
		 * @param type  属性类型（Store.YES=数据被索引，并且存储到文件当中；Store.NO=数据被索引，但不存储到文件当中）
		 */
		Field field1 = new StringField("content", content, Store.YES);
		Field field2 = new StringField("name", "maomao", Store.YES);
		Field field3 = new StringField("age", "30", Store.YES);
		document.add(field1);
		document.add(field2);
		document.add(field3);
		// 建立索引
		indexWriter.addDocument(document);
	}

	@After
	public void close() throws IOException {
		this.indexWriter.close();
	}

}
