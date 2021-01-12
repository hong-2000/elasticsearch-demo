# elasticsearch-demo

## Elasticsearch
基于Lucene的搜索服务器，java语言开发，其他产品有：Apache Solr。
|关系型数据库 |Elasticsearch|
|----|----|
|database|index|
|table|type|
|row|document|
|column|field|
type目前已作废
### 单节点配置
- mkdir -p /usr/local/elasticsearch
- tar zxvf elasticsearch-7.4.2.1-linux-x86_64.tar.gz -C /usr/local/elasticsearch/
- pwd -- 打印当前目录路径
- vim ../bin/elasticsearch-env JAVA_HOME="/usr/local/elasticsearch/elasticsearch-7.4.2/jdk" -- 用自带的jdk11
- groupadd es -- 添加用户组    useradd es -g es -- 添加用户到用户组
- su es -- 切换用户
- chown -Rf es /usr/local/elasticsearch/ -- 修改文件夹所属用户组
- chown -Rf es:es /usr/local/elasticsearch/ -- 修改文件夹所属用户组和用户
- /config/elasticsearch.yml  network.host:0.0.0.0 -- 远程访问，默认9200
- vim /etc/security/limits.conf 在最下面添加
    es soft nofile 65535
    es hard nofile 65535
    es soft nproc 65535
    es hard nproc 65535
- vim /etc/sysctl.conf 在最下面添加
    vm.max_map_count = 262144
- sysctl -p -- 刷新
- vim /config/elasticsearch.yml 在最下面添加
    discovery.seed_hosts: ["192.168.10.100"]
    cluster.initial_master_nodes: ["192.168.10.100"]
- ./elasticsearch -d -- 后台启动
- ps -ef|grep elasticsearch -- 查看启动情况
### 集群配置
- 指定jdk
- vim /config/elasticsearch.yml
- cluster.name: es  
- node.name: node-1
- network.host: 0.0.0.0
- http.port: 9200
- 添加跨域请求配置(为了让类似head的第三方插件可以请求es)
http.cors.enabled: true
http.cors.allow-origin: "*"
- 添加集群发现配置
discovery.seed_hosts: ["192.168.12.160","192.168.12.160","192.168.12.160"]
cluster.initial_master_nodes: ["node-1","node-2","node-3"]
discovery.zen.ping_timeout: 60s
### 插件安装
#### 可视化工具
1. head 
2. cerebrp 
3. elasticHD
#### Kibana
- 数据分析和可视化平台，elasticsearch成员。
- vim /config/kibana.yml 修改 1.server.port 2.server.host 3.elasticsearch.hosts
- cd /bin/ 启动 ./kibana --allow-root
#### IK Analysis中文分词器
- 将Lucene IK分析器集成到elasticsearch中，支持自定义词典
- 每个节点都需要安装
- unzip xx.zip -d es1/plugins/ik/
### Logstash
- 开源的服务器端数据处理管道，可以同时从多个来源采集数据。
- 启动测试 bin/logstash -e 'input { stdin {} } output { stdout {} }'
### 安装与启动环境所用到的命令
pwd<br>
vim elasticsearch-env<br>
groupadd es<br>
useradd es -g es<br>
chown -Rf es:es /usr/local/elasticsearch/<br>
vim /etc/security/limits.conf<br>
vim /etc/sysctl.conf<br>
sysctl -p<br>
cp -r elasticsearch-7.10.1/. /usr/local/elasticsearch/es1<br>
cp -r elasticsearch-7.10.1/. /usr/local/elasticsearch/es2<br>
cp -r elasticsearch-7.10.1/. /usr/local/elasticsearch/es3<br>
chown -Rf es:es /usr/local/elasticsearch/es1<br>
chown -Rf es:es /usr/local/elasticsearch/es2<br>
chown -Rf es:es /usr/local/elasticsearch/es3<br>
vim config/elasticsearch.yml<br>
es1/bin/elasticsearch -d<br>
curl http://127.0.0.1:9200<br>
http://192.168.12.160:9200/_cluster/health?pretty<br>
<br>
mkdir -p /usr/local/kibana<br>
tar zxvf kibana-7.10.1-linux-x86_64.tar.gz -C /usr/local/kibana<br>
vim kibana.yml<br>
/usr/local/kibana/kibana-7.10.1-linux-x86_64/bin/kibana --allow-root<br>
http://192.168.12.160:5601<br>
<br>
mkdir -p es1/plugins/ik<br>
mkdir -p es2/plugins/ik<br>
mkdir -p es3/plugins/ik<br>
unzip elasticsearch-analysis-ik-7.10.1.zip -d es1/plugins/ik/<br>
unzip elasticsearch-analysis-ik-7.10.1.zip -d es2/plugins/ik/<br>
unzip elasticsearch-analysis-ik-7.10.1.zip -d es3/plugins/ik/<br>
chown -Rf es:es /usr/local/elasticsearch/<br>
ps -ef |grep elastic<br>
kill -9 2145 2336 2766<br>
/usr/local/elasticsearch/es1/bin/elasticsearch -d<br>
/usr/local/elasticsearch/es2/bin/elasticsearch -d<br>
/usr/local/elasticsearch/es3/bin/elasticsearch -d<br>
<br>
mkdir -p /usr/local/logstash<br>
tar zxvf logstash-7.10.1-linux-x86_64.tar.gz -C /usr/local/logstash/<br>
bin/logstash -e 'input { stdin {} } output { stdout {} }'<br>
### 关于自定义配置
1. kibana的kibana.yml<br>
        · server.port: 5601<br>
        · server.host: "0.0.0.0"<br>
        · elasticsearch: ["http://192.168.12.160:9200","http://192.168.12.160:9201","http://192.168.12.160:9202"]<br>
        · i18n.locale: "zh-CN"<br>
2. elasticsearch的elasticsearch.yml<br>
        · cluster.name: es<br>
        · node.name: node-1<br>
        · network.host: 0.0.0.0<br>
        · http.port: 9200<br>
        · 添加跨域请求配置(为了让类似head的第三方插件可以请求es)<br>
        · http.cors.enabled: true<br>
        · http.cors.allow-origin: "*"<br>
        · 添加集群发现配置<br>
        · discovery.seed_hosts: ["192.168.12.160","192.168.12.160","192.168.12.160"]<br>
        · cluster.initial_master_nodes: ["node-1","node-2","node-3"]<br>
        · discovery.zen.ping_timeout: 60s<br>
3. elasticsearch的elasticsearch-env<br>
        · JAVA_HOME="/usr/local/elasticsearch/elasticsearch-7.4.2/jdk"<br>
4. IK的IKAnalyzer.cfg.xml<br>
        · entry key="ext_dict" zhaoqinghong.dic /entry<br>


