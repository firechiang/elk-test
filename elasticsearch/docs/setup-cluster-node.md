#### 一、准备环境，修改如下配置，请使用root账号（注意：集群的每个节点都要修改）
```bash
# 修改 sysctl.conf 配置如下
$ vi /etc/sysctl.conf
vm.max_map_count=655360

# 修改 limits.conf 配置如下
$ vi /etc/security/limits.conf
* soft nofile 65535
* hard nofile 65535

# 加载配置，使上面的配置生效
$ sysctl -p          
```
#### 二、创建部署用户，集群的每台机器都要创建(Elasticsearch不建议使用root账户部署)
```bash
$ useradd elk-admin                                                               # 创建 elk-admin 用户
$ echo "jiang" | passwd --stdin elk-admin                                         # 为elk-admin 用户创建密码，密码是：jiang
$ echo "elk-admin ALL = (root) NOPASSWD:ALL" | sudo tee /etc/sudoers.d/elk-admin  # 为elk-admin 用户授权，并生成授权文件
$ cat /etc/sudoers.d/elk-admin                                                    # 查看授权文件
$ chmod 0440 /etc/sudoers.d/elk-admin                                             # 修改授权文件权限
$ chown elk-admin:elk-admin /home /home/tools                                     # 将/home和/home/tools两个目录的权限授给elk-admin用户
$ su elk-admin                                                                    # 切换到elk-admin
```


#### 三、下载安装
```bash
$ cd /home/tools
$ wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.1.0-linux-x86_64.tar.gz
$ tar -zxvf elasticsearch-7.1.0-linux-x86_64.tar.gz -C ../                        # 解压到上层目录
```

#### 四、修改[vi /home/elasticsearch-7.1.0/config/elasticsearch.yml]配置文件
```bash
cluster.name: myElasticsearch                                # 集群名称
http.port: 9200                                              # http 通信端口
transport.tcp.port: 9400                                     # 集群内部通信端口
#指定该节点是否有资格被选举成为master节点，默认是true，es是默认集群中的第一台机器为master，如果这台机挂了就会重新选举master
node.master: true
node.data: true                                              # 允许该节点存储数据(默认开启)
path.data: /home/elasticsearch-7.1.0/data                    # 数据存储目录(注意：手动创建目录)
path.logs: /home/elasticsearch-7.1.0/logs                    # 日志存储目录 (注意：手动创建目录)
discovery.seed_hosts: ["server001", "server002","server003"] # 种子节点列表（注意：填写IP或主机名）
cluster.initial_master_nodes: ["node01", "node02","node03"]  # 初始化集群参选主节点名称列表，如果是新加节点到旧的集群好像不能写自己（注意：这里填的是名称，就是配置项 node.name 的值）
xpack.security.enabled: true                                 # 是否开启安全验证(配置项里面没有，需手动添加)
```

#### 五、分发安装文件到集群各个节点
```bash
$ scp -r /home/elasticsearch-7.1.0 elk-admin@server002:/home
$ scp -r /home/elasticsearch-7.1.0 elk-admin@server003:/home
```

#### 六、修改[vi /home/elasticsearch-7.1.0/config/elasticsearch.yml]集群各个节点的名称和服务绑定地址
```bash
node.name: node01                                            # 节点名称（集群唯一）
network.host: server001                                      # 服务绑定地址（修改为各个节点的主机名或IP）
```

#### 七、配置集群各个节点上的环境变量[vi ~/.bashrc]在末尾添加如下内容
```bash
export ELASTICSEARCH_HOME=/home/elasticsearch-7.1.0
PATH=$PATH:$ELASTICSEARCH_HOME/bin                           # linux以 : 号隔开，windows以 ; 号隔开

$ source ~/.bashrc                                           # （系统重读配置）在各个机器上执行使配置文件生效（实验：敲个elastic然后按Tab键，如果补全了说明配置成功了）
$ echo $ELASTICSEARCH_HOME
```

#### 八、启动集群各个节点上的Elasticsearch（单个节点信息：http://192.168.229.133:9200）
```bash
$ elasticsearch                                              # 前台启动 Elasticsearch 节点（建议测试使用，因为前台显示日志）
$ elasticsearch -d                                           # 后台启动 Elasticsearch 节点（建议生产使用）

$ kill -SIGTERM 15455                                        # 正常停止 Elasticsearch 节点
```

#### 九、配置集群访问密码，Elasticsearch默认已经把账号设置好了，我们只需要配置密码即可，密码最低长度6位数（到集群任意节点执行命令）
```bash
$ elasticsearch-setup-passwords interactive

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
