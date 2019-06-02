#### 一、创建配置文件 [vi /home/logstash-7.1.0/config/simple-centos-test.conf] 
```bash
# 输入源配置（系统日志推送组件好像只会将日志推送到 10514 这个端口）
input {
  #beats {
  #  port => 5044
  #  ssl => true
  #  ssl_certificate => "/etc/pki/tls/certs/logstash-forwarder.crt"
  #  ssl_key => "/etc/pki/tls/private/logstash-forwarder.key"
  #}
  syslog {
    type => "syslog" # 定义日志类型，可自定义
    port => 10514    # 定义监听端口
  }
}
# 过滤器配置
filter {
  if [type] == "syslog" {
    grok {
      match => { "message" => "%{SYSLOGTIMESTAMP:syslog_timestamp} %{SYSLOGHOST:syslog_hostname} %{DATA:syslog_program}(?:\[%{POSINT:syslog_pid}\])?: %{GREEDYDATA:syslog_message}" }
      add_field => [ "received_at", "%{@timestamp}" ]
      add_field => [ "received_from", "%{host}" ]
    }
    syslog_pri {
    #syslog_pri_field_name => "syslog_pri"
    }
    #geoip { source => "ip" }
    date {
      match => [ "syslog_timestamp", "MMM  d HH:mm:ss", "MMM dd HH:mm:ss" ]
    }
  }
}
# 输出目的地配置（输出到Elasticsearch）
output {
  elasticsearch {
    hosts => ["server001:9200","server002:9200","server003:9200"]
    sniffing => true
    manage_template => false
    index => "centos-server003-log-%{+YYYY.MM.DD}"
    #index => "%{[@metadata][beat]}-%{+YYYY.MM.DD}"
    #document_type => "%{[@metadata][type]}"
  }
}

# 输出目的地配置（输出到控制台）
#output {
#  stdout {
#    codec => rubydebug  # 将日志输出到当前的终端上显示
#  }
#}
```

#### 二、修改[sudo vi /etc/rsyslog.conf]系统日志推送组件的配置，将系统日志推送到我们的 Logstash（这个组件Centos7是默认自带的）
```bash
*.* @@server003:10514
```

#### 三、重启系统日志推送组件
```bash
$ sudo systemctl restart rsyslog
```

#### 四、启动 Logstash 日志搜集（注意：如果一台logstash需要启动多个进程，--path.data参数，需要指定不同的目录，不然会报错。基本和 Flume 的使用差不多）
#####  --path.settings         用于指定logstash的配置文件所在的目录
##### -f                       指定需要被检测的配置文件的路径
##### --config.test_and_exit   指定检测完之后就退出，不然就会直接启动了
```bash
$ cd /home/logstash-7.1.0/bin

# 检查我们的测试配置文件是否有错误，执行过程有点慢（如果显示了 Configuration OK 说明没有问题）
$ ./logstash --path.settings /home/logstash-7.1.0/config -f /home/logstash-7.1.0/config/simple-centos-test.conf --config.test_and_exit

# 指定配置文件的方式启动 Logstash，执行过程有点慢
$ ./logstash --path.settings /home/logstash-7.1.0/config -f /home/logstash-7.1.0/config/simple-centos-test.conf --path.data=/home/logstash-7.1.0/data-centos &
```
