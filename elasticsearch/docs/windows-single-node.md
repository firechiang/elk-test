#### 一、下载Elasticsearch https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.1.0-windows-x86_64.zip
#### 二、修改[vi elasticsearch-7.1.0/config/elasticsearch.yml]配置文件
```bash
http.port: 9200                                              # http 通信端口
transport.tcp.port: 9400                                     # 集群内部通信端口
#指定该节点是否有资格被选举成为master节点，默认是true，es是默认集群中的第一台机器为master，如果这台机挂了就会重新选举master
node.master: true
node.data: true                                              # 允许该节点存储数据(默认开启)
node.name: node01                                            # 节点名称
network.host: 127.0.0.1                                      # 服务绑定地址
path.data: D:\elasticsearch-7.1.0\data                       # 数据存储目录(注意：手动创建目录)
path.logs: D:\elasticsearch-7.1.0\data                       # 日志存储目录 (注意：手动创建目录)
discovery.seed_hosts: ["localhost"]                          # 种子节点列表（注意：填写IP或主机名）
cluster.initial_master_nodes: ["node01"]                     # 初始化集群参选主节点名称列表，如果是新加节点到旧的集群好像不能写自己（注意：这里填的是名称，就是配置项 node.name 的值）
xpack.security.enabled: true                                 # 是否开启安全验证(配置项里面没有，需手动添加)
```
#### 三、配置IK中文分词器（注意：IK分词器需对应Elasticsearch版本，否则Elasticsearch将无法启动，如果版本不对应可以修改IK分词器plugin-descriptor.properties配置文件里面的Elasticsearch版本号）
```bash
$ sudo yum install -y unzip zip                              # 安装zip文件解压工具                         
$ wget -P /home/tools/ik https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.1.0/elasticsearch-analysis-ik-7.1.0.zip
$ cd /home/tools/ik
$ unzip ./elasticsearch-analysis-ik-7.1.0.zip -d ./          # 解压到当前目录
$ scp -r /home/tools/ik /home/elasticsearch-7.1.0/plugins    # 将IK中文分词器插件拷贝到Elasticsearch插件目录
```

#### 四、启动Elasticsearch（单个节点信息：http://192.168.229.133:9200）
```bash
$ elasticsearch.bat                                          # 启动 Elasticsearch
```

#### 五、配置访问密码，Elasticsearch默认已经把账号设置好了，我们只需要配置密码即可，密码最低长度6位数
```bash
$ elasticsearch-setup-passwords.bat interactive

Initiating the setup of passwords for reserved users elastic,apm_system,kibana,logstash_system,beats_system,remote_monitoring_user.
You will be prompted to enter passwords as the process progresses.
Please confirm that you would like to continue [y/N]y


Enter password for [elastic]: 
Reenter password for [elastic]: 
Enter password for [apm_system]: 
Reenter password for [apm_system]: 
Enter password for [kibana]: 
Reenter password for [kibana]: 
Enter password for [logstash_system]: 
Reenter password for [logstash_system]: 
Enter password for [beats_system]: 
Reenter password for [beats_system]: 
Enter password for [remote_monitoring_user]: 
Reenter password for [remote_monitoring_user]: 
Changed password for user [apm_system]
Changed password for user [kibana]
Changed password for user [logstash_system]
Changed password for user [beats_system]
Changed password for user [remote_monitoring_user]
Changed password for user [elastic]
```
