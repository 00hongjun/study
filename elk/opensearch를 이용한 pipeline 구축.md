# opensearch를 이용한 pipeline 구축

`Amazon Linux 2023` 기준으로 정리하였다.  
버전 호환은 Opensearch 2.7.x의 [문서](https://opensearch.org/docs/latest/tools/index/#downloads)를 확인하였다.

## filebeat 환경 구성

먼저 로그를 수집하여 logstash로 전달할 filebeat를 구성한다.

### 다운로드

oss-7.16.2 버전을 설치 한다. → [다운로드 링크](https://opensearch.org/docs/latest/tools/index/#downloads)

```shell
 # 다운로드
 $ wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-oss-7.16.3-linux-x86_64.tar.gz
 
 # 압축 풀기
 $ tar xzf filebeat-oss-7.16.3-linux-x86_64.tar.gz
```

### 설정

먼저 filebeat.yml에 수집 대상 file과 logstash 정보 등을 지정해야 한다.
설치 경로의 `./bin` 경로로 이동하면 `filebeat.yml` 파일의 원본과 `filebeat.reference.yml`이 있으므로 참고로 사용하면 좋다.

```
# ============================== Filebeat inputs ===============================

filebeat.inputs:
- type: logm
  enabled: true
  paths:
    - /home/ec2-user/logs/payment-app.log
  multiline.pattern: ^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d{3}
  multiline.negate: true
  multiline.match: after


# ============================== Filebeat modules ==============================

filebeat.config.modules:
  path: ${path.config}/modules.d/*.yml
  reload.enabled: false


# ======================= Elasticsearch template setting =======================

setup.template.settings:
  index.number_of_shards: 1
  #index.codec: best_compression
  #_source.enabled: false


# ================================== General ===================================

name: payments
fields:
  service: payments
  module: payments-app


# ================================== Outputs ===================================

# ------------------------------ Logstash Output -------------------------------
output.logstash:
  hosts: ["localhost:5044"]


# ================================= Processors =================================
processors:
  - add_host_metadata:
      when.not.contains.tags: forwarded
  - add_cloud_metadata: ~
  - add_docker_metadata: ~
  - add_kubernetes_metadata: ~

# ================================== Logging ===================================
logging.level: info
logging.to_file: true
logging.files:
  path: /home/ec2-user/logs/filebeat
  name: filebeat.log
  keepfiles: 15
  permissions: 0644
```

filebeat.yml 파일에 대한 권한 에러가 발생할 경우 아래의 명령어를 입력한다.

```shell
$ chmod go-w filebeat.yml
```

---

### 참고 자료 링크

* https://opensearch.org/docs/latest/tools/index/#downloads
