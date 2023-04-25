[AWS MSK 문서](https://docs.aws.amazon.com/ko_kr/msk/latest/developerguide/create-serverless-cluster-client.html)를 참고하여 진행하였다.


ec2 인스턴스에 kafka 클라이언트 환경을 구성하고 이를 통해서 MSK에 접근하는 방식이다.  
해당 인스턴스는 role이 부여되어 있어야 한다.

# 환경 구성

1. kafka 설치를 위해 java 11 버전을 먼저 세팅한다.
    ```shell
    sudo yum -y install java-11
    ```

1. kafka의 tarball을 다운로드한다.  2.8.1 버전을 사용했다.
    ```shell
    wget https://archive.apache.org/dist/kafka/2.8.1/kafka_2.12-2.8.1.tgz
    ```

1. tarball의 압축을 풀어준다.
    ```shell
    tar -xzf kafka_2.12-2.8.1.tgz
    ```

1. 설치된 kafka dir의 libs 디렉토리에 아래 명령어를 실행하여 IAM 관련 jar 파일을 다운로드한다.
    ```shell
    wget https://github.com/aws/aws-msk-iam-auth/releases/download/v1.1.1/aws-msk-iam-auth-1.1.1-all.jar
    ```

1. 설치된 kafka dir의 bin 디렉토리에서 client.properties 파일을 생성하고, 해당 파일에 아래 내용을 추가한다.
    ```shell
    # client.properties 파일 생성
    touch client.properties

    # client.properties 파일의 내용
    security.protocol=SASL_SSL
    sasl.mechanism=AWS_MSK_IAM
    sasl.jaas.config=software.amazon.msk.auth.iam.IAMLoginModule required;
    sasl.client.callback.handler.class=software.amazon.msk.auth.iam.IAMClientCallbackHandler
    ```


# topic 및 message 관리

## topic 생성
아래 명령어를 통해 topic을 생성 및 상태를 조회한다.
```shell
# 토픽 생성
./bin/kafka-topics.sh --bootstrap-server {MSK의 bootstrap 주소} \
--command-config client.properties \
--replication-factor 2 --partitions 1 \
--create --topic test-topic

# output
Created topic test-topic.

----------

# 토픽 상태 조회
./bin/kafka-topics.sh --bootstrap-server {MSK의 bootstrap 주소} \
--command-config client.properties \
--describe --topic test-topic
```

* MSK의 bootstrap 주소는 굉장히 긴 문자열이다. 명령어 실행 시마다 복붙하기에는 너무 수고롭다.  
 변수로 export 해주자.  
    ```shell
    export MSK_BOOTSTRAP={MSK의 bootstrap 주소}
    ```
    이제 아래와 같이 사용 가능하다.
    ```shell
    ./bin/kafka-topics.sh --bootstrap-server $MSK_BOOTSTRAP \
    --command-config client.properties \
    --create --topic test-topic
    ```

## 메시지 생성 및 소비

### 생성
아래 명령어를 통해 topic을 소비한다.
```shell
./bin/kafka-console-producer.sh --broker-list $MSK_BOOTSTRAP \
--producer.config client.properties \
--topic test-topic
```

### 소비
아래 명령어를 통해 message를 소비한다.
* 실행 시점부터 소비
    ```shell
    ./bin/kafka-console-consumer.sh --bootstrap-server $MSK_BOOTSTRAP \
        --consumer.config client.properties \
        --topic test-topic
    ```
* 처음부터 소비
    ```shell
    ./bin/kafka-console-consumer.sh --bootstrap-server $MSK_BOOTSTRAP \
        --consumer.config client.properties \
        --topic test-topic --from-beginning
    ```
---

### 참고 자료 링크
* https://docs.aws.amazon.com/ko_kr/msk/latest/developerguide/create-serverless-cluster-client.html
