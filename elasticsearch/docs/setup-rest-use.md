#### 一、Rest接口操作说明
```bash
GET                                                          # 获取对象的当前状态
PUT                                                          # 改变对象的状态或创建对象
POST                                                         # 创建对象
DELETE                                                       # 删除对象
HEAD                                                         # 获取头信息
```

#### 二、Elasticsearch内置Rest接口描述（注意：实际应用当中请将 index 替换成对象名称）
```bash
/index/_search                                               # 搜索指定索引下的数据
/_aliases                                                    # 获取或操作索引的别名(所有的)
/index/                                                      # 查看指定索引的详细信息
/index/type                                                  # 创建或操作类型
/index/_mapping                                              # 创建或操作mapping
/index/_settings                                             # 创建或操作设置 number_of_shards(是不可更改的)
/index/_open                                                 # 打开指定被关闭的索引
/index/_close                                                # 关闭指定索引
/index/_refresh                                              # 刷新索引(使新加内容对搜索可见，不保证数据被写入磁盘)
/index/_flush                                                # 刷新索引(会触发lucene提交)
```

#### 三、Elasticsearch内置Rest接口测试使用
```bash
curl -XPUT http://192.168.229.133:9200/test_test             # 创建对象名字叫test_test(相当于Mysql创建了一个库，库名test_test)
```