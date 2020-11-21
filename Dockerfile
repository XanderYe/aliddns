# build
FROM maven:3.6.3-adoptopenjdk-8 as BUILDER
LABEL maintainer="XanderYe <XanderYe@outlook.com>"

RUN apt-get update && \
	apt-get install -y git && \
	rm -rf /var/lib/apt/lists/* && \
	apt-get clean

WORKDIR /app

RUN git clone https://github.com/XanderYe/aliddns.git

WORKDIR /app/aliddns

RUN mvn -Dmaven.test.skip clean install assembly:single
# run
FROM fabletang/jre8-alpine:181

WORKDIR /app
COPY --from=BUILDER /app/aliddns/target/aliddns-jar-with-dependencies.jar ./aliddns.jar

ENTRYPOINT ["java", "-jar", "/app/aliddns.jar"]
