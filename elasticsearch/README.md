#### [一、Windows 开发搭建][1]
#### [二、Centos 单机搭建][2]
#### [三、Centos 集群搭建][3]
#### [四、Rest 接口使用][4]
#### [五、索引生命周期使用，可到：kibana > 管理 > 索引生命周期策略，界面创建](https://www.elastic.co/guide/en/elasticsearch/reference/7.1/getting-started-index-lifecycle-management.html)
#### Elasticsearch核心基于Lucene的倒排索引（注意：倒排索引只在field上创建）
```bash
# 数据示例(注：后面跟的是行号)
我是中国人(1)                                                                           
中国人是全球人口最多的国家，中国人也很多(2)

# 倒排索引示例(注：括号里面的是：数据所出现的行和次数，中括号里面的是：数据所在文件的偏移量)
我(1:1){0}                    # (1:1)表示：数据在第1行，出现1次，{0}表示：数据所在文件的偏移量
中国(1:1){2},(2:2){0,15}     
```

#### Elasticsearch和关系型数据库的数据对比（注意：ES7规定每一个index只能有一个type，默认叫_doc，在8.x版本会彻底移除type）
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

#### wget爬取数据
```bash
# -o                 指定日志文件目录
# -P                 爬取数据存放目录
# --no-parent        不向上搜索
# --no-verbose       只查找比本地文件新的数据
# -m                 做一个镜像即使拷贝
# -D                 要爬取的域名列表  
# -N --convert-links 将爬取的地址转换成存储目录  
# --random-wait      随机等待  
# -A                 要爬取的文件       
wget -o /home/wget-log/wget.log -P /home/elk-data --no-parent --no-verbose -m -D news.cctv.com -N --convert-links --random-wait -A html,HTML,shtml,SHTML http://news.cctv.com
```

[1]: https://github.com/firechiang/elk-test/tree/master/elasticsearch/docs/windows-single-node.md
[2]: https://github.com/firechiang/elk-test/tree/master/elasticsearch/docs/setup-single-node.md
[3]: https://github.com/firechiang/elk-test/tree/master/elasticsearch/docs/setup-cluster-node.md
[4]: https://github.com/firechiang/elk-test/tree/master/elasticsearch/docs/setup-rest-use.md
