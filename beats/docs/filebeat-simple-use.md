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
  
setup.template.settings:
  # 索引切片数
  index.number_of_shards: 2
  # 说应副本数（这个配置没有要手动加）
  index.number_of_replicas: 2

# 以下为自动创建索引生命周期管理配置，具体管理可到：kibana > 管理 > 索引生命周期策略。可管理索引数据自动删除等等
# 官方详解：https://www.elastic.co/guide/en/beats/filebeat/current/ilm.html#setup-ilm-policy_name-option
setup.ilm:
  # 开启日志索引生命周期管理
  enabled: true
  # 索引生命周期别名（会当成索引的名称显示）。默认是 filebeat-{agent.version}，设置此选项只会更改别名中的前缀，agent.version默认还是会在最后
  rollover_alias: "server002-elastic"
  # 用于生命周期策略的名称，这个名称可以在 kibana > 管理 > 索引生命周期策略，界面处查看到 。默认是 filebeat-{agent.version}
  policy_name: "server002-elastic"
```

#### 四、Filebeat 简单使用
```bash
$ ./filebeat --help                              # 查看 Filebeat 命令使用帮助
# -e                                   前台启动，日志打印在控制台，不会打印到日志文件
# -c filebeat.yml                      指定配置文件启动，默认指定配置文件目录下的 filebeat.yml 文件
# -d "publish"                         启用某些调试选择器
# -E output.logstash.enabled=false     启动时指定配置项的值，可以写多个 -E，每一个指定一个配置项 
$ ./filebeat -e -c filebeat.yml -d "publish"     # 启动   Filebeat（建议测试使用）  
$ ./filebeat -c filebeat.yml &                   # 启动   Filebeat（建议生产使用）

# 启动时指定配置信息
$ ./filebeat setup --template -E output.logstash.enabled=false -E 'output.elasticsearch.hosts=["localhost:9200"]'

```