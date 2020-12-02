# aliddns

## 用途
阿里DDNS的docker镜像，每5分钟更新一次解析记录，支持多主机记录解析同一地址

## 获取镜像

### 1.自己编译
执行 `docker build -t xanderye/aliddns:1.1 .`

### 2.从dockerhub pull
2.执行 `docker pull xanderye/aliddns:1.1`

## 创建容器
执行
`docker run -itd \
-e PERIOD=5 \
-e REGIN_ID=cn-hangzhou \
-e ACCESS_KEY_ID= \
-e ACCESS_SECRET= \
-e RR=test \
-e DOMAIN_NAME=xanderye.cn \
--restart=always \
--name aliddns \
xanderye/aliddns:1.1`

### 注意事项
1. 参数需要自己填好、修改好, [如何获取AccessKey](https://help.aliyun.com/knowledge_detail/48699.html)
2. RR支持多主机记录解析，逗号分隔
