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
$ wget https://artifacts.elastic.co/downloads/kibana/kibana-7.1.0-linux-x86_64.tar.gz
$ tar -zxvf kibana-7.1.0-linux-x86_64.tar.gz -C ../                    # 解压到上层目录
```

#### 三、修改[vi /home/kibana-7.1.0-linux-x86_64/config/kibana.yml]
```bash
server.port: 5601
server.host: "server004"                                          
server.name: "kibana01"                                                # 服务名称
elasticsearch.hosts: ["http://s01:9200","http://s02:9200"]             # Elasticsearch集群地址
elasticsearch.username: "kibana"                                       # Elasticsearch用户名
elasticsearch.password: "jiang"                                        # Elasticsearch密码
pid.file: /home/kibana-7.1.0-linux-x86_64/kibana.pid                   # 运行Kibana时进程号文件所在地址
logging.dest: /home/kibana-7.1.0-linux-x86_64/logs/kibana.log          # Kibana日子文件所在地址
i18n.locale: "zh-CN"                                                   # 配置中文显示
```

#### 启动 Kibana
```bash
$ /home/kibana-7.1.0-linux-x86_64/bin
$ ./kibana &                                                           # 后台启动 kibana
$ kill -SIGTERM `cat /home/kibana-7.1.0-linux-x86_64/kibana.pid` # 停止 kibana
```