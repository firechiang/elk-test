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
$ wget https://artifacts.elastic.co/downloads/logstash/logstash-7.1.0.tar.gz
$ tar -zxvf logstash-7.1.0.tar.gz -C ../                    # 解压到上层目录
```

#### 三、修改[vi /home/logstash-7.1.0/config/startup.options]配置基础启动参数（这个文件好像不需要修改）
```bash
JAVACMD=/usr/lib/jvm/jdk1.8.0_171                           # JDK目录
LS_HOME=/home/logstash-7.1.0                                # logstash 安装目录
LS_SETTINGS_DIR=/home/logstash-7.1.0/config                 # 包含 logstash.yml 配置文件的目录
LS_JAVA_OPTS=""                                             # JVM 启动要传递的参数
LS_GC_LOG_FILE=/home/logstash-7.1.0/logs/gc.log             # logstash 的GC日志所在目录

LS_USER=logstash                                            # logstash 用户
LS_GROUP=logstash                                           # logstash 用户组
```