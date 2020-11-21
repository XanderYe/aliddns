# aliddns

## 用途
阿里DDNS的docker镜像，每5分钟更新一次解析记录

## 获取镜像

### 1.自己编译
执行 `docker build -t xanderye/aliddns:1.0 .`

### 2.从dockerhub pull
2.执行 `docker pull xanderye/aliddns:1.0`

## 创建容器
执行
`docker run -itd \
--env REGIN_ID=cn-hangzhou \
--env ACCESS_KEY_ID= \
--env ACCESS_SECRET= \
--env RR=test \
--env DOMAIN_NAME=xanderye.cn \
--restart=always \
--name aliddns \
xanderye/aliddns:1.0`

参数需要自己填好、修改好, [如何获取AccessKey](https://help.aliyun.com/knowledge_detail/48699.html)
