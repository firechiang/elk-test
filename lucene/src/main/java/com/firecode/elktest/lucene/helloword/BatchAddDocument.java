package com.firecode.elktest.lucene.helloword;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;

import com.firecode.elktest.lucene.IndexWriters;

/**
 * 批量插入文档
 * @author JAING
 *
 */
public class BatchAddDocument {
	
	private String[] ids = {"1","2","3","4","5","6"};
	private String[] emails = {"aa@itat.org","bb@itat.org","cc@cc.org","dd@sina.org","ee@zttc.edu","ff@itat.org"};
	private String[] contents = {"welcome to visited the space,I like book",
			                     "hello boy, I like pingpeng ball",
			                     "my name is cc I like game",
			                     "I like football",
			                     "I like football and I like basketball too",
			                     "I like movie and swim"
	};
	
	@Test
	public void test() throws IOException {
		IndexWriter indexWriter = IndexWriters.newInstance();
		// 删除所有文档
		indexWriter.deleteAll();
		Document doc = null;
		for(int i=0;i<ids.length;i++) {
			doc = new Document();
			FieldType docType = new FieldType();
			// 持久化
			docType.setStored(true);
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
			docType.setIndexOptions(IndexOptions.DOCS);
			doc.add(new Field("id",ids[i],docType));
			doc.add(new Field("email",emails[i],docType));
			
			FieldType allType = new FieldType();
			// 持久化
			allType.setStored(true);
			docType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
			doc.add(new Field("content",contents[i],allType));
			indexWriter.addDocument(doc);
		}
		// 提交数据
		indexWriter.commit();
	}
}
