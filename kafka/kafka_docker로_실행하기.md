
테스트용으로 간단한 kafka 환경을 구성하기 위해 docker를 이용하여 kafka를 single node로 간단하게 구성한다.  
작업은 aws의 ec2 환경에서 진행하였으며, image는 [wurstmeister/kafka-docker](https://github.com/wurstmeister/kafka-docker)를 이용한다.



# compose 파일 생성
간단한 docker compose 파일을 만들어보자.  
파일의 이름은 kafka-single-node.yml로 생성하였다.
```yaml
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: {ec2 인스턴스의 IP}  # ip 입력
      KAFKA_CREATE_TOPICS: "test:1:1"
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```

> [README에 작성되어 있는 내용](https://github.com/wurstmeister/kafka-docker#pre-requisites)에 의하면 KAFKA_ADVERTISED_HOST_NAME 값을 host의 IP로 지정할 것을 추천하고 있다.


# 실행 및 관리

## 컨데이너 관리 명령어
* 실행
    ```bash
    docker-compose -f kafka-single-node.yml up -d
    ```
* 컨테이너 확인
    ```bash
    docker ps
    docker ps -a
    ```
* 모든 컨테이너 삭제
    ```bash
    docker stop $(docker ps -a -q)
    docker rm  $(docker ps -a -q)
    ```
* 컨테이너 접근
    ```bash
    docker exec -it kafka1 /bin/bash
    ```
    > kafka1은 컨테이너의 이름이다.

## kafka 관리 명령어

* 모든 토픽 리스트 조회
    * 컨테이너에 접근하여 실행
        ```bash
        kafka-topics.sh --bootstrap-server localhost:9092 \
        --list 
        ```
    * 호스트 환경에서 실행
        ```bash
        docker exec -it kafka1 kafka-topics.sh \
        --bootstrap-server localhost:9092 \
        --list 
        ```
* 토픽 생성
    ```bash
    docker exec -it kafka1 kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --create --topic test-events 
    ```
    > test-events라는 이름의 topic을 생성한다.

* 토픽 상태 확인
    ```bash
    docker exec -it kafka1 kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --describe --topic test-events
    ```
* 토픽 삭제
    ```bash
    docker exec -it kafka1 kafka-topics.sh \
    --bootstrap-server localhost:9092 \
    --delete --topic test-events
    ```
* producer 실행
    ```bash
    docker exec -it kafka1 kafka-console-producer.sh \
    --bootstrap-server localhost:9092 \
    --topic test-events
    ```
* consumer 실행
    * 실행 시점의 message 부터 읽기
        ```bash
        docker exec -it kafka1 kafka-console-consumer.sh \
        --bootstrap-server localhost:9092 \
        --topic test-events
        ```
    * 첫 message 부터 읽기
        ```bash
        docker exec -it kafka1 kafka-console-consumer.sh \
        --bootstrap-server localhost:9092 \
        --topic test-events --from-beginning
        ```