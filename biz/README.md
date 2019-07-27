#### 一、技术栈
springboot，lombok，mysql，redis，kafka，elasticsearch，thymeleaf，springsecurity，jpa阿里云，七牛云，百度地图
#### 二、使用说明
```bash
用户入口 http://localhost:8080/user/center 帐号密码都是admin
管理员入口 http://localhost:8080/admin/center 帐号密码都是admin
```
#### 三、[下载Windows版Elacticsearch-6.8.0](https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.8.0.zip)和修改[vi elasticsearch-6.8.0/config/elasticsearch.yml]配置文件
```bash
network.host: 0.0.0.0                                      # 服务绑定地址
http.port: 9200                                            # http 通信端口
transport.tcp.port: 9400                                   # 服务访问端口（注意：Spring Data连接的就是这个端口）
#discovery.type: single-node                   # 单节点模式（不建议配置，因为以后可能要做集群）
path.data: D:\elasticsearch-6.8.0\data                     # 数据存储目录(注意：手动创建目录)
path.logs: D:\elasticsearch-6.8.0\logs                     # 日志存储目录 (注意：手动创建目录)
```
#### 四、[下载](https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.8.0/elasticsearch-analysis-ik-6.8.0.zip)和配置IK中文分词器（注意：IK分词器需对应Elasticsearch版本，否则Elasticsearch将无法启动，如果版本不对应可以修改IK分词器plugin-descriptor.properties配置文件里面的Elasticsearch版本号）
```bash
$ sudo yum install -y unzip zip                            # 安装zip文件解压工具                         
$ wget -P /home/tools/ik https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.8.0/elasticsearch-analysis-ik-6.8.0.zip
$ cd /home/tools/ik
$ unzip ./elasticsearch-analysis-ik-6.8.0.zip -d ./        # 解压到当前目录
$ scp -r /home/tools/ik /home/elasticsearch-6.8.0/plugins  # 将IK中文分词器插件拷贝到Elasticsearch插件目录
```

#### 五、启动Elasticsearch（单个节点信息：http://127.0.0.1:9200）
```bash
elasticsearch.bat                                          # 启动 Elasticsearch
```

#### 六、创建需要的索引
```bash
$ curl -H 'Content-Type:application/json' -XPUT 'http://192.168.229.1:9200/xunwu' -d '{
  "settings": {
    "number_of_replicas": 0,
    "index.store.type": "niofs"
  },
  "mappings": {
    "house": {
      "dynamic": false,
      "properties": {
        "houseId": {
          "type": "long"
        },
        "title": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "price": {
          "type": "integer"
        },
        "area": {
          "type": "integer"
        },
        "createTime": {
          "type": "date"

        },
        "lastUpdateTime": {
          "type": "date"

        },
        "cityEnName": {
          "type": "keyword"
        },
        "regionEnName": {
          "type": "keyword"
        },
        "direction": {
          "type": "integer"
        },
        "distanceToSubway": {
          "type": "integer"
        },
        "subwayLineName": {
          "type": "keyword"
        },
        "subwayStationName": {
          "type": "keyword"
        },
        "tags": {
          "type": "text"
        },
        "street": {
          "type": "keyword"
        },
        "district": {
          "type": "keyword"
        },
        "description": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "layoutDesc" : {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "traffic": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "roundService": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "rentWay": {
          "type": "integer"
        },
        "suggest": {
          "type": "completion"
        },
        "location": {
          "type": "geo_point"
        }
      }
    }
  }
}'
```
