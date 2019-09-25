package com.firecode.elktest.lucene.helloword;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.After;
import org.junit.Test;

import com.firecode.elktest.lucene.BaseDirectory;

/**
 * 修改文档相关操作
 * 
 * @author JIANG
 */
public class UpdateDocumentTest extends BaseDirectory {

	/**
	 * 索引写入对象
	 */
	private IndexWriter indexWriter;
	
	public void before() throws IOException {
		/**
		 * 索引写入相关配置
		 * @param analyzer 分词器
		 *            
		 */
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		if (exists) {
			// 在目录中创建新索引，删除以前的索引
			config.setOpenMode(OpenMode.CREATE);
		} else {
			// 将新文档添加到现有索引:
			config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		this.indexWriter = new IndexWriter(directory, config);
	}
	
	/**
	 * 添加文档
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@Test
	public void updateDocument() throws IOException, ParseException {
		
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
		Field field1 = new StringField("content", "aaaaaa", Store.YES);
		Field field2 = new StringField("name", "maomao", Store.YES);
		Field field3 = new StringField("age", "32", Store.YES);
		document.add(field1);
		document.add(field2);
		document.add(field3);
		/**
		 * 修改name=maomao的文档
		 * 注意：Lucene其实是先将文档删除，再添加
		 */
		indexWriter.updateDocument(new Term("name","maomao"), document);
	}

	@After
	public void close() throws IOException {
		this.indexWriter.close();
		super.close();
	}

}
