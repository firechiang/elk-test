#### 一、创建配置文件 [vi /home/logstash-7.1.0/config/simple-nginx-test.conf] 
```bash
# 输入源配置
input {
  file {
    path => "/tmp/elk_access.log"  # 指定文件的路径
    start_position => "beginning"  # 指定何时开始收集
    type => "nginx"                # 定义日志类型，可自定义
  }
}
# 过滤器配置
filter {
    grok {
        match => { "message" => "%{IPORHOST:http_host} %{IPORHOST:clientip} - %{USERNAME:remote_user} \[%{HTTPDATE:timestamp}\] \"(?:%{WORD:http_verb} %{NOTSPACE:http_request}(?: HTTP/%{NUMBER:http_version})?|%{DATA:raw_http_request})\" %{NUMBER:response} (?:%{NUMBER:bytes_read}|-) %{QS:referrer} %{QS:agent} %{QS:xforwardedfor} %{NUMBER:request_time:float}"}  # 定义日志的输出格式
    }
    geoip {
        source => "clientip"
    }
}
# 输出目的地配置（输出到Elasticsearch）
output {
    stdout { codec => rubydebug }
    elasticsearch {
        hosts => ["server001:9200","server002:9200","server003:9200"]
        index => "nginx-test-%{+YYYY.MM.dd}"
  }
}

# 输出目的地配置（输出到控制台）
#output {
#  stdout {
#    codec => rubydebug  # 将日志输出到当前的终端上显示
#  }
#}
```

#### 四、启动 Logstash 日志搜集（注意：如果一台logstash需要启动多个进程，--path.data参数，需要指定不同的目录，不然会报错。基本和 Flume 的使用差不多）
#####  --path.settings         用于指定logstash的配置文件所在的目录
##### -f                       指定需要被检测的配置文件的路径
##### --config.test_and_exit   指定检测完之后就退出，不然就会直接启动了
```bash
$ cd /home/logstash-7.1.0/bin

# 检查我们的测试配置文件是否有错误，执行过程有点慢（如果显示了 Configuration OK 说明没有问题）
$ ./logstash --path.settings /home/logstash-7.1.0/config -f /home/logstash-7.1.0/config/simple-nginx-test.conf --config.test_and_exit

# 指定配置文件的方式启动 Logstash，执行过程有点慢
$ ./logstash --path.settings /home/logstash-7.1.0/config -f /home/logstash-7.1.0/config/simple-nginx-test.conf --path.data=/home/logstash-7.1.0/data-nginx &
```