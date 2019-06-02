#### 一、[单节点搭建][1]
#### 二、[测试搜集Centos日志][2]
#### 三、[测试搜集Nginx日志][3]
#### 生成SSH证书和公私密钥对（主要用于filebeat组件对ELK server的身份验证）
```bash
$ cd /etc/pki/tls
# 生成公私密钥对，私钥所在目录：/etc/pki/tls/private/logstash-forwarder.key；公钥所在目录：/etc/pki/tls/certs/logstash-forwarder.crt
$ sudo openssl req -subj '/CN=ELK_server_fqdn/' -x509 -days 3650 -batch -nodes -newkey rsa:2048 -keyout private/logstash-forwarder.key -out certs/logstash-forwarder.crt
```

[1]: https://github.com/firechiang/elk-test/tree/master/logstash/docs/single-node-construction.md
[2]: https://github.com/firechiang/elk-test/tree/master/logstash/docs/collect-centos-log.md
[3]: https://github.com/firechiang/elk-test/tree/master/logstash/docs/collect-nginx-log.md