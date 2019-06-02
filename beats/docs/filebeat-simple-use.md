#### 一、创建部署用户，集群的每台机器都要创建(Elasticsearch不建议使用root账户部署)
```bash
$ useradd elk-admin                                                    # 创建 elk-admin 用户
$ echo "jiang" | passwd --stdin elk-admin                              # 为elk-admin 用户创建密码，密码是：jiang
# 为elk-admin 用户授权，并生成授权文件
$ echo "elk-admin ALL = (root) NOPASSWD:ALL" | sudo tee /etc/sudoers.d/elk-admin  
$ cat /etc/sudoers.d/elk-admin                                         # 查看授权文件
$ chmod 0440 /etc/sudoers.d/elk-admin                                  # 修改授权文件权限
$ chown elk-admin:elk-admin /home /home/tools                          # 将/home和/home/tools两个目录的权限授给elk-admin用户
$ su elk-admin                                                         # 切换到elk-admin
```

#### 二、下载安装
```bash
$ cd /home/tools
$ wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-7.1.0-linux-x86_64.tar.gz
$ tar -zxvf filebeat-7.1.0-linux-x86_64.tar.gz -C ../                  # 解压到上层目录
```
/home/elasticsearch-7.1.0/logs/myElasticsearch.log

#### 三修改[vi /home/filebeat-7.1.0-linux-x86_64/filebeat.yml]
```bash
filebeat.inputs:

# Each - is an input. Most options can be set at the input level, so
# you can use different inputs for various configurations.
# Below are the input specific configurations.

- type: log
  # 开启配置
  enabled: true
  # 日志所在目录
  paths:
    - /home/elasticsearch-7.1.0/logs/myElasticsearch.log
    
output.elasticsearch:
  # Elasticsearch集群地址
  hosts: ["server001:9200","server002:9200","server003:9200"] 
  # 连接协议  
  protocol: "http"
  username: "elastic"
  password: "jiang" 
  # 索引名称（这个配置没有要手动加）
  index: "elastixnode02-%{[agent.version]}-%{+yyyy.MM.dd}"
  
# 注意下面的配置格式经过了调整，原来是: setup.template.settings
setup.template:
  settings:
  # 索引切片数
  index.number_of_shards: 2
  # 说应副本数（这个配置没有要手动加）
  index.number_of_replicas: 2
  # 以下两个配置没有要手动加（注意：和索引的名称前缀对应）
  name: "elastixnode02"
  pattern: "elastixnode02-*"
```

#### 四、启动 Filebeat
```bash
$ ./filebeat -e -c filebeat.yml -d "publish"
```