#### [一、Windows 开发搭建][1]
#### [二、Centos 单机搭建][2]
#### [三、Centos 集群搭建][3]
#### [四、Rest 接口使用][4]
#### Elasticsearch核心基于Lucene的倒排索引（注意：倒排索引只在field上创建）
```bash
# 数据示例(注：后面跟的是行号)
我是中国人(1)                                                                           
中国人是全球人口最多的国家，中国人也很多(2)

# 倒排索引示例(注：括号里面的是：数据所出现的行和次数，中括号里面的是：数据所在文件的偏移量)
我(1:1){0}                    # (1:1)表示：数据在第1行，出现1次，{0}表示：数据所在文件的偏移量
中国(1:1){2},(2:2){0,15}     
```

#### Elasticsearch和关系型数据库的数据对比
```bash
-----------------|-----------------------
    MySQL        |     Elasticsearch    
-----------------|-----------------------
   database      |      index(索引库)    
-----------------|-----------------------
    table        |     type(类型)        
-----------------|-----------------------
    row          |     document(文档)    
-----------------|-----------------------
   column        |     field(字段)       
-----------------|-----------------------
```

[1]: https://github.com/firechiang/elk-test/tree/master/elasticsearch/docs/windows-single-node.md
[2]: https://github.com/firechiang/elk-test/tree/master/elasticsearch/docs/setup-single-node.md
[3]: https://github.com/firechiang/elk-test/tree/master/elasticsearch/docs/setup-cluster-node.md
[4]: https://github.com/firechiang/elk-test/tree/master/elasticsearch/docs/setup-rest-use.md