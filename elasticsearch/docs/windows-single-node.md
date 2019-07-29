#### 一、下载Elasticsearch https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.1.0-windows-x86_64.zip
#### 二、修改[vi elasticsearch-7.1.0/config/elasticsearch.yml]配置文件
```bash
http.port: 9200                                           # http 通信端口
transport.tcp.port: 9400                                  # 服务访问端口（注意：Spring Data连接的就是这个端口）
network.host: server001                                   # 服务绑定地址
path.data: D:\elasticsearch-7.1.0\data                    # 数据存储目录(注意：手动创建目录)
path.logs: D:\elasticsearch-7.1.0\data                    # 日志存储目录 (注意：手动创建目录)
#discovery.type: single-node                              # 单节点模式（不建议配置，因为以后可能要做集群）
xpack.security.enabled: true                              # 如果要配置密码的话，就开启安全验证(配置项里面没有，需手动添加)
```
#### 三、配置IK中文分词器（注意：IK分词器需对应Elasticsearch版本，否则Elasticsearch将无法启动，如果版本不对应可以修改IK分词器plugin-descriptor.properties配置文件里面的Elasticsearch版本号）
```bash
$ sudo yum install -y unzip zip                           # 安装zip文件解压工具                         
$ wget -P /home/tools/ik https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.1.0/elasticsearch-analysis-ik-7.1.0.zip
$ cd /home/tools/ik
$ unzip ./elasticsearch-analysis-ik-7.1.0.zip -d ./       # 解压到当前目录
$ scp -r /home/tools/ik /home/elasticsearch-7.1.0/plugins # 将IK中文分词器插件拷贝到Elasticsearch插件目录
```

#### 四、启动Elasticsearch（单个节点信息：http://192.168.229.133:9200）
```bash
$ elasticsearch.bat                                       # 启动 Elasticsearch
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
