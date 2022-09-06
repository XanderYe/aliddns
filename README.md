# aliddns

## 用途
阿里DDNS的docker镜像，每5分钟更新一次解析记录，支持多主机记录解析同一地址

## 获取镜像

### 1.自己编译
执行 `docker build -t xanderye/aliddns:1.3 .`

#### 1.1 自己根据本地代码文件编译
执行 `docker build -f Dockerfile-local -t xanderye/aliddns:1.3 .`

### 2.从dockerhub pull
2.执行 `docker pull xanderye/aliddns:1.3`

## 创建容器
执行
```
docker run -itd \
-e PERIOD=5 \
-e REGIN_ID=cn-hangzhou \
-e ACCESS_KEY_ID= \
-e ACCESS_SECRET= \
-e RR=test \
-e DOMAIN_NAME=xanderye.cn \
-e TYPE=ipv6 \
--restart=always \
--net=host \
--name aliddns \
xanderye/aliddns:1.3
```
### docker-compose
```yaml
version: "3.9"
services:
  aliddns:
    image: xanderye/aliddns:1.3
    container_name: aliddns
    network_mode: host
    environment:
      - PERIOD=5
      - REGIN_ID=cn-hangzhou
      - ACCESS_KEY_ID=
      - ACCESS_SECRET=
      - RR=
      - DOMAIN_NAME=
      - TYPE=ipv6 #可选ipv4或ipv6,不添加则默认为ipv6
    restart: unless-stopped
```

### 注意事项
1. 参数需要自己填好、修改好, [如何获取AccessKey](https://help.aliyun.com/knowledge_detail/48699.html)
2. RR支持多主机记录解析，逗号分隔
