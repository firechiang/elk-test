#### ES7规定每一个index只能有一个type，默认叫_doc，在8.x版本会彻底移除type
#### 一、Elasticsearch和关系型数据库的数据对比
```bash
-----------------|-----------------------
    MySQL        |     Elasticsearch    
-----------------|-----------------------
   database      |      index(索引库)    
-----------------|-----------------------
    table        |      type(类型)        
-----------------|-----------------------
    row          |     document(文档)    
-----------------|-----------------------
   column        |     field(字段)       
-----------------|-----------------------
```
#### 二、Rest接口操作说明（对象可以是：索引，类型）
```bash
GET                                                          # 获取对象的当前状态
PUT                                                          # 改变索引的状态或创建对象
POST                                                         # 创建对象
DELETE                                                       # 删除对象
HEAD                                                         # 获取头信息
```

#### 三、Elasticsearch内置Rest接口描述（注意：实际应用当中请将 index 替换成索引库名字）
```bash
/index/_search                                               # 搜索指定索引下的数据
/_aliases                                                    # 获取或操作索引库的别名(所有的)
/index/                                                      # 查看指定索引库的详细信息
/index/type                                                  # 创建或操作索引库表
/index/_mapping                                              # 创建或操作mapping
/index/_settings                                             # 创建索引库时设置 number_of_shards或number_of_replicas(是不可更改的)
/index/_open                                                 # 打开指定被关闭的索引库
/index/_close                                                # 关闭指定索引库
/index/_refresh                                              # 刷新索引库(使新加内容对搜索可见，不保证数据被写入磁盘)
/index/_flush                                                # 刷新索引库(会触发lucene提交)
```

#### 四、Elasticsearch内置Rest接口测试使用
```bash

# 创建索引库 user_test ，切片数 1，副本数 1
curl -H 'Content-Type:application/json' -XPUT 'http://192.168.229.133:9200/user_test' -d '{ 
    "settings" : {
        "number_of_shards":2,
        "number_of_replicas":2
    }                                                                          
}'

# 以POST方式往索引库 user_test 上的maomao表加了一条数据，ID自动创建(也可以自定义，在连接最后添加即可)（注意：库和表会自动创建）
curl -H 'Content-Type:application/json' -XPOST 'http://192.168.229.133:9200/user_test/maomao' -d '{ 
    "first_name" : "maomao",                                                                          
    "age" : 33,                                                                                       
    "about" : "I love to go rock climbing", 
    "create_date" : "2019-09-10",                                                        
    "interests" : ["sports","music"]                                                                  
}'

# 以PUT方式往索引库 user_test 上的maomao表加了一条数据（注意：需要在请求连接后面指定ID，而且索引库和表必须先存在，否则会报错）
curl -H 'Content-Type:application/json' -XPUT 'http://192.168.229.133:9200/user_test/maomao/1' -d '{ 
    "first_name" : "jiangjiang",                                                                          
    "age" : 30,                                                                                       
    "about" : "I love to go rock climbing",  
    "create_date" : "2019-10-10",                                                          
    "interests" : ["sports","music"]                                                                  
}'

# 根据ID(id=1)获取索引库 user_test 上的maomao表上的数据（格式化JSON）
curl -XGET 'http://192.168.229.133:9200/user_test/maomao/1?pretty'

# 根据ID(id=1)获取索引库 user_test 上的maomao表上的数据（不格式化JSON）
curl -XGET 'http://192.168.229.133:9200/user_test/maomao/1'

# 根据field(属性(first_name=maomao))获取索引库 user_test 上的maomao表上的数据
curl -XGET 'http://192.168.229.133:9200/user_test/maomao/_search?q=first_name=maomao'

# 以单条件查询匹配的方式获取索引库 user_test 上的maomao表上的数据(query表示查询，match表示匹配)
curl -H 'Content-Type:application/json' -XGET 'http://192.168.229.133:9200/user_test/maomao/_search' -d '{
    "query":{
        "match":{
            "first_name":"jiangjiang"
        }
    }
}'

# multi_match查询允许你做match查询的基础上同时搜索多个字段
# 以多条件查询匹配的方式获取索引库 user_test 上的maomao表上的数据(query表示数据的值，fields表示多个字段，operator关联模式（and 是或者的意思）)
# 查询 first_name 或者 about 的属性值等于maomao的数据
curl -H 'Content-Type:application/json' -XGET 'http://192.168.229.133:9200/user_test/maomao/_search' -d '{
    "query":{
        "multi_match":{
            "query":"maomao",
            "fields":["first_name","about"],
            "operator":"and"
        }
    }
}'


# bool 查询
# bool 查询与 bool 过滤相似，用于合并多个查询子句。不同的是，bool 过滤可以直接给出是否匹配成功， 而bool 查询要计算每一个查询子句的 _score （相关性分值）。
# must: 查询指定文档一定要被包含。
# must_not: 查询指定文档一定不要被包含。
# should: 查询指定文档，有则可以为文档相关性加分。
# 以下查询将会找到 first_name 等于 maomao，并且 age 不等于22。 如果 about 等于starred或者 create_date 是2010-01-01之前的，那么这些匹配的文档将比同类的等级高
curl -H 'Content-Type:application/json' -XGET 'http://192.168.229.133:9200/user_test/maomao/_search' -d '{
    "query":{
        "bool":{
            "must":{
                "match":{
                    "first_name":"maomao"
                }
            },
            "must_not":{
                "match":{
                    "age":22
                }
            },
            "should": [ 
                { "match": { "about": "starred" }}, 
                { "range": { "create_date": { "gte": "2010-01-01" }}} 
            ]
        }
    }
}'

# 查询年龄在22到33之间的所有数据
curl -H 'Content-Type:application/json' -XGET 'http://192.168.229.133:9200/user_test/maomao/_search' -d '{
    "query":{
        "bool":{
            "must":{
                "range":{
                    "age":{"from":20,"to":33}
                }
            }
        }
    }
}'


# wildcards（标准的shell通配符）查询（? 匹配任意一个字符（不包括零个），* 匹配任意多个字符（包括零个或一个））
curl -H 'Content-Type:application/json' -XGET 'http://192.168.229.133:9200/user_test/maomao/_search' -d '{ 
    "query": { 
        "wildcard": { 
            "first_name": "m?om*o" 
        } 
    } 
}'


# regexp（正则表达式）查询
curl -H 'Content-Type:application/json' -XGET 'http://192.168.229.133:9200/user_test/maomao/_search' -d '{ 
    "query": { 
        "regexp": { 
            "first_name": "j^*.+" 
        } 
    } 
}'

# prefix（前缀）查询
curl -H 'Content-Type:application/json' -XGET 'http://192.168.229.133:9200/user_test/maomao/_search' -d '{ 
    "query": { 
        "prefix": { 
            "first_name": "m" 
        } 
    } 
}'

# Phrase Matching（短语匹配），当你需要寻找邻近的几个单词时，可以使用match_phrase查询
curl -H 'Content-Type:application/json' -XGET 'http://192.168.229.133:9200/user_test/maomao/_search' -d '{
    "query": {
        "match_phrase": {
            "first_name": "quick brown fox"
        }
    }
}'
```