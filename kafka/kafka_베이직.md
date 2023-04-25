

kafka를 공부하기 전에 간단하게 훑어보자.  

# event? event streaming?
![event_stream](/picture/kafka/event_stream.png)

## 이벤트(event)?
프로그램에 의해 감지되고 처리될 수 있는 동작이나 사건을 말한다.  
예를 들면 아래의 경우가 있다.  
- 브라우저에서의 사용자가 클릭을 했을 때, 스크롤을 했을 때, 필드의 내용을 바꾸었을 때
- 고객이 상품을 장바구니에 담았을 때, 상품을 구매했을 때
- 키보드를 눌렀을 때
- 예금이 입금됐을 때
- DB에 값이 insert 됐을 때

이벤트는 사용자의 어떤 동작에 의해 발생할 수 있으며 또 다른 이벤트에 의해 발생할 수도 있다.

## event streaming?
여러 종류의 클라이언트에서 이벤트는 실시간으로 발생하며, 이런 이벤트를 실시간으로 stream 형태로 처리하는것을 말한다.    
기업은 이벤트 스트리밍을 통해 이벤트와 관련된 데이터를 분석하고 해당 이벤트를 실시간으로 처리한다.  
이벤트 스트림 처리를 통해 데이터를 정규화, 보강, 필터링할수 있으며, 이벤트의 상관 관계에 따라 또 다른 이벤트를 발생시키고 비즈니스를 처리할 수도 있다.   

사용자가 주문 → 주문을 생성(주문시스템) → 이벤트 → 결제 처리(결제 시스템) → 이벤트 → 주문 완료 처리(주문 시스템)

## event streaming에서 할 수 있는것들
- 증권거래소, 은행, 보험 등 결제와 금융거래를 실시간으로 처리.
- 자동차, 트럭, 비행대, 그리고 운송을 물류, 자동차 산업과 같이 실시간으로 추적하고 감시.
- IoT 기기 또는 공장이나 윈드파크와 같은 기타 장비에서 센서 데이터를 지속적으로 캡처하고 분석.
- 소매업, 호텔 및 여행업, 모바일 애플리케이션과 같은 고객 상호작용 및 주문에 즉시 대응.
- 병원 진료 시 환자를 모니터링하고 응급 상황 시 적시에 치료할 수 있도록 상태 변화를 예측.
- 회사의 여러 부서에서 생산한 데이터를 연결, 저장 및 이용 가능하도록 하기 위해.
- 데이터 플랫폼, 이벤트 중심 아키텍처 및 마이크로 서비스를 위한 기반으로서 기능한다.

---

# Apache Kafka

![kafka_logo](/picture/kafka/kafka_logo.png)

**LinkedIn의 고객 데이터 분석**  
LinkedIn에서 웹사이트 활동을 추적하고 분석하는것을 목적으로 개발 되었다.
개발 당시 **'높은 처리량'**, **'임의의 타이밍에 데이터 호출'**, **'다양한 시스템과 연동'**, **'메시지를 잃지 않는다.'** 라는 4가지 목표를 가지고 개발을 시작하였다.
   
**요구사항**  
- 높은 처리량으로 실시간 처리  
- 임의의 타이밍에 데이터 처리  
- 다양한 서드 파티와의 연동  
- 메시지를 잃지 않는다  

위의 4가지 요구사항을 만족하기 위해 아래의 실현 수단을 이용하였다.  

**실현 수단**  
- 메시징 모델, 스케일 아웃형 아키텍처  
- 디스크에 저장
- 다양한 API 제공(Connector, Kafka Streams)
- 전달 보증

## KAFKA의 특징
여러 대의 분산 서버에서 대량의 데이터를 처리하는 **분산 메시징** 시스템.  
대량의 데이터를 **높은 처리량**과 **실시간**으로 처리하도록 아래의 특징을 가지고 있다.  

- 확장성: 서버를 얼마든지 확장 가능 → 데이터 양에 따라 시스템 확장.
- 영속성: 데이터를 디스크에 유지 가능 → 언제든지 데이터를 다시 읽을 수 있다.
- 유연성: 연계 가능한 다양한 방법을 제공 → 다양한 시스템을 연결하는 허브 역할.
- 신뢰성: 메시지 전달 보증 → 데이터 분실이 일어나지 않도록 보장한다.

## 구성요소
- **Broker**: 메시지를 수신/전달 전달한다. 전달받은 데이터는 디스크에 영속화한다.  
- **Message**: Key-Value 형태이며, 카프카에서 다루는 최소단위의 데이터.  
- **Producer**: 송신 역할. 데이터의 생산자. 메시지를 보내는 역할을 한다.  
- **Consumer**: 수신 역할. Broker에서 메시지를 취득한다.   
- **Topic**: 메시지를 종류별로 관리하는 스토리지. 메시지를 중계한다.  

## Kafka의 모델
kafka는 큐잉 모델과 Pub/Sub 모델을 가져와 **Producer**, **Broker**, **Consumer**를 구성하였다.  
PUSH / PULL 형태. → Consumer가 원하는 시점에 데이터 처리가 가능하다.  
![queue_model](/picture/kafka/queue_model.png)

[큐잉 모델]  

브로커가 큐를 가지고 있는 형태이며,  컨슈머가 큐에서 메시지를 읽는다.  
컨슈머를 확장시켜 처리량을 늘릴 수 있으나 동일 메시지를 여러 컨슈머가 읽을 수 있도록 처리할 수는 없다.  
대표적인 예로 AWS SQS가 있다. SQS는 standard, FIFO 등으로 처리 방식을 선택할 수 있다.  
standard의 경우 scale out이 가능하나 전달 보증이 at least once까지 보장 가능하다.  


![kafka_message_model](/picture/kafka/kafka_message_model.png)

[kafka message 모델]

Publisher(Producer)와 Subscriber(Consumer)가 Broker를 중간에 두고 메시지를 전달하는 형태이다.  
Publisher와 Consumer는 서로 메시지의 생성자와 수신자가 누구인지 알 수 없는 구조를 가지고 있으며 이를 통해 뛰어난 확장성을 가지고 있다.  
반대로 Producer가 생산한 메시지를 Subscriber가 반드시 처리하도록 보장하지 못하는 단점도 가지고 있다.  

### Topic
메시지를 **종류별로 구분하는 단위**이다.  
디스크에 **영속화** 한다.  
Broker는 여러 개의 Topic을 가질 수 있다.  
Producer, Consumer는 Topic을 지정하고, 해당 Topic에 메시지를 송신/수신한다.  
Partition 단위로 나누고 데이터를 분산 시킬 수 있다.  
![broker](/picture/kafka/broker1.png)
![broker](/picture/kafka/broker2.png)
![broker](/picture/kafka/broker3.png)
![broker](/picture/kafka/broker4.png)

### Consumer Group
큐잉모델과 Pub/Sub 모델의 장점을 가져오기 위해 만든 개념이다.  
여러 컨슈머가 **동일 Topic**을 분산하여 메시지를 처리하도록 한다. → 메시지 처리의 확장성을 담보한다.  
Topic의 메시지는 다른 그룹에 속한 Consumer는 같은 메시지를 읽을 수 있다.   
하나의 Partition에는 Consumer Group 내의 1개의 Consumer만 접근 가능하다.  

![consumer_group](/picture/kafka/consumer_group1.png)
![consumer_group](/picture/kafka/consumer_group2.png)
![consumer_group](/picture/kafka/consumer_group3.png)
![consumer_group](/picture/kafka/consumer_group4.png)
![consumer_group](/picture/kafka/consumer_group5.png)


## 전달 보증
메시지 전달 과정에서 3가지 방식을 이용하여 메시지 전달 보증을 제공한다.

| 종류 | 설명 | 재전송 | 중복 삭제 | 비고 |
| --- | --- | --- | --- | --- |
| At Most Once | 1회는 전달을 시도 | X | X | 메시지는 중복되지 않지만 상실 가능 |
| At Least Once | 적어도 1회는 전달 | O | X | 메시지가 중복될 수 있지만 상실 안됨 |
| Exactly Once | 1회만 전달 | O | O | 중복, 상실이 되지 않고 확실하게 전달
성능상 불리 |

Kafka에서는 Exactly Once의 트랜잭션 메커니즘 제공을 위해  Ack와 Offset Commit을 이용한다.

- **Ack** : Broker가 메시지를 수신했을 때 Producer에게 수신 완료 응답을 보내는것  
- **Offset** : Consumer가 어디까지 메시지를 받았는지 관리  
- **Offset Commit** :  Offset의 전달 보증을 위해 처리한 Offset을 업데이트 하는데 이를 Offset Commit이라 부른다.  

![kafka_ack](/picture/kafka/kafka_ack.png)
![offset_commit](/picture/kafka/offset_commit.png)

### At Least Once 방식

Ack와 Offset commit의 개념을 이용한다.
Broker는 메시지를 수신했을 때 Ack 응답을 보낸다. Broker가 Ack 응답을 받지 못한 경우 재전송 처리를 한다.
Consumer가 메시지를 수신 후, 어디까지 메시지를 받았는지 알기 위해 offset을 관리하는데 이를 Offset Commit이라 한다.
![at_least_once](/picture/kafka/at_least_once.png)

### Exactly One 방식
transaction 개념을 도입하여 전달을 보증한다.   
sequence 번호를 이용해 중복 실행을 관리한다.  
![exactly_one](/picture/kafka/exactly_one1.png)
![exactly_one](/picture/kafka/exactly_one2.png)


## Cluster, Replication
- **Leader** : 메인 파티션
- **Follower** : Leader의 메시지를 취득하여 복제
- **ISR(In Sync Replica)** : 복제 상태를 유지하는 replica
- **URP(Under Replicated Partitions)** : 모든 replica가 ISR이 아닌 파티션

![cluster](/picture/kafka/cluster.png)