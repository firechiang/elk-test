#### 一、系统调优配置，修改如下配置，请使用root账号（注意：集群的每个节点都要修改）
```bash
# 修改 sysctl.conf 配置如下
$ vi /etc/sysctl.conf
vm.swappiness=10                                       # 定义内核交换内存页面的积极程度。较高的值会增加攻击性，较低的值会减少交换量。建议使用10来保证交换延迟
vm.max_map_count=655360                                # 限制进程最大内存映射区域数，单个jvm能开启的最大线程数为其一半（控制进程能够打开文件句柄的数量。提供对shell及其启动的进程的可用文件句柄的控制。这是进程级别的）

# 修改 limits.conf 配置如下
$ vi /etc/security/limits.conf
* soft nofile 655350                                   # 打开文件和网络连接文件描述符。建议将655350设置为文件描述符（注意：不能超过hard nofile）
* hard nofile 655350                                   # 打开文件和网络连接文件描述符。建议将655350设置为文件描述符（注意：不能超过 fs.nr_open 的值，可使用  sysctl  fs.nr_open 命令查看 fs.nr_open 的值）

# 加载配置，使上面的配置生效
$ sysctl -p   
$ ulimit -n                                            # 查看进程能够打开文件句柄的数量  
$ shutdown -r now                                      # 重启机器使配置生效
```
#### 二、创建部署用户，集群的每台机器都要创建(Elasticsearch不建议使用root账户部署)
```bash
$ useradd elk-admin                                    # 创建 elk-admin 用户
$ echo "jiang" | passwd --stdin elk-admin              # 为elk-admin 用户创建密码，密码是：jiang
# 为elk-admin 用户授权，并生成授权文件
$ echo "elk-admin ALL = (root) NOPASSWD:ALL" | sudo tee /etc/sudoers.d/elk-admin  
$ cat /etc/sudoers.d/elk-admin                         # 查看授权文件
$ chmod 0440 /etc/sudoers.d/elk-admin                  # 修改授权文件权限
$ chown elk-admin:elk-admin /home /home/tools          # 将/home和/home/tools两个目录的权限授给elk-admin用户
$ su elk-admin                                         # 切换到elk-admin
```


#### 三、下载安装
```bash
$ cd /home/tools
$ wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.1.0-linux-x86_64.tar.gz
$ tar -zxvf elasticsearch-7.1.0-linux-x86_64.tar.gz -C ../           
```
#### 四、配置IK中文分词器（注意：IK分词器需对应Elasticsearch版本，否则Elasticsearch将无法启动，如果版本不对应可以修改IK分词器plugin-descriptor.properties配置文件里面的Elasticsearch版本号）
```bash
$ sudo yum install -y unzip zip                              # 安装zip文件解压工具                         
$ wget -P /home/tools/ik https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.1.0/elasticsearch-analysis-ik-7.1.0.zip
$ cd /home/tools/ik
$ unzip ./elasticsearch-analysis-ik-7.1.0.zip -d ./          # 解压到当前目录
$ scp -r /home/tools/ik /home/elasticsearch-7.1.0/plugins    # 将IK中文分词器插件拷贝到Elasticsearch插件目录
```

#### 五、修改[vi /home/elasticsearch-7.1.0/config/elasticsearch.yml]配置文件
```bash
http.port: 9200                                        # http 通信端口
transport.tcp.port: 9400                               # 服务访问端口（注意：Spring Data连接的就是这个端口）
network.host: server001                                # 服务绑定地址
path.data: /home/elasticsearch-7.1.0/data              # 数据存储目录(注意：手动创建目录)
path.logs: /home/elasticsearch-7.1.0/logs              # 日志存储目录 (注意：手动创建目录)
xpack.security.enabled: true                           # 如果要配置密码的话，就开启安全验证(配置项里面没有，需手动添加)
```

#### 六、配置环境变量[vi ~/.bashrc]在末尾添加如下内容
```bash
export ELASTICSEARCH_HOME=/home/elasticsearch-7.1.0
PATH=$PATH:$ELASTICSEARCH_HOME/bin                     # linux以 : 号隔开，windows以 ; 号隔开

$ source ~/.bashrc                                     # （系统重读配置）在各个机器上执行使配置文件生效（实验：敲个elastic然后按Tab键，如果补全了说明配置成功了）
$ echo $ELASTICSEARCH_HOME
```

#### 七、启动Elasticsearch（单个节点信息：http://192.168.229.133:9200）
```bash
$ elasticsearch                                        # 前台启动 Elasticsearch 节点（建议测试使用，因为前台显示日志）
$ elasticsearch -d                                     # 后台启动 Elasticsearch 节点（建议生产使用）

$ kill -SIGTERM 15455                                  # 正常停止 Elasticsearch 节点
```

#### 八、配置访问密码，Elasticsearch默认已经把账号设置好了，我们只需要配置密码即可，密码最低长度6位数
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
