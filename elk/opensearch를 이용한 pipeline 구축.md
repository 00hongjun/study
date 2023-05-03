# opensearch를 이용한 pipeline 구축

`Amazon Linux 2023` 기준으로 정리하였다.  
버전 호환은 Opensearch 2.7.x의 [문서](https://opensearch.org/docs/latest/tools/index/#downloads)를 확인하였다.

## filebeat

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
설치 경로의 `./bin` 경로로 이동하면 `filebeat.yml` [파일의 원본](https://github.com/hjhello423/dev-study/blob/main/elk/filebeat.yml)
과 `filebeat.reference.yml`이 있으므로 참고로 사용하면 좋다.

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

### 실행 및 로그

`{설치 경로}` 부분의 경로를 입력하여 아래 명령어를 실행한다.

```
# 데몬으로 실행
$ nohup {설치 경로}/filebeat -c {설치 경로}/filebeat.yml > /dev/null &

# 직접 실행
$ {설치 경로}/filebeat -c {설치 경로}/filebeat.yml
```

filebeat의 로그는 filebeat.yml의 `logging.files:path`에 설정해둔 경로에 기록된다.  
위의 예시에서는 아래 명령어를 이용하여 확인 가능하다.

```shell
$ tail -f ~/logs/filebeat.log
```

## logstash

### 다운로드

먼저 JDK를 설치한다.  
[opensearch 권장 사항](https://opensearch.org/docs/latest/tools/logstash/index/#install-logstash)을 확인하여 JDK 8 버전을 사용하였다.

```shell
# Amazon Linux 2
$ sudo yum list java*
$ sudo yum -y install java-1.8.0-amazon-corretto-devel.x86_64
```

7.16.3버전의 logstash-oss를 다운 받고 압축을 푼다.

```
# 파일 다운로드
$ wget https://artifacts.opensearch.org/logstash/logstash-oss-with-opensearch-output-plugin-7.16.3-linux-x64.tar.gz

# 압축 풀기
$ tar xzf logstash-oss-with-opensearch-output-plugin-7.16.3-linux-x64.tar.gz
```

압축을 해제한 폴더로 이동 후 opensearch plugin을 install 한다.

```bash
# 이동
$ cd /home/ec2-user/logstash-7.16.3

# 명령어 실행
$ ./bin/logstash-plugin install --preserve logstash-input-opensearch

Using bundled JDK: /home/ec2-user/logstash-7.16.3/jdk
OpenJDK 64-Bit Server VM warning: Option UseConcMarkSweepGC was deprecated in version 9.0 and will likely be removed in a future release.
Validating logstash-input-opensearch
Installing logstash-input-opensearch
Installation successful
```

* issue   
  아래와 같이 Killed 메시지가 출력되면서 플러그인 설치가 안되는 경우가 있었다.
  ```bash
  $ ./bin/logstash-plugin install --preserve logstash-input-opensearch

  Using bundled JDK: /home/ec2-user/logstash-7.16.3/jdk
  OpenJDK 64-Bit Server VM warning: Option UseConcMarkSweepGC was deprecated in version 9.0 and will likely be removed in a future release.
  Validating logstash-input-opensearch
  Installing logstash-input-opensearch
  Killed
  ```
    * 해결  
      java의 JDK 문제일 것으로 추측됐는데. OpenJDK17, amazon corretto8에서 동일한 에러가 발생했다.
      OpenJDK8을 설치 후 시도해 보면 해결된다.

### 설정

먼저 logstash 설치 경로 하위의 config 디렉토리에서 conf.d 이름의 디렉토리를 생성한다.

```bash
$ cd {설치 경로}/config
$ mkdir conf.d
```

logstash의 일부 설정 파일을 conf.d로 복사해 사용한다.

```bash
$ cd {설치 경로}/config
$ cp logstash-sample.conf ./conf.d/logstash.conf
```

#### logstash.yml 설정

`{설치경로}/config/logstash.yml` 파일의 설정을 변경한다.

- logstash.yml
    ```bash
    pipeline.ordered: auto
    config.reload.automatic: true
    
    # ------------ Debugging Settings --------------
    log.level: debug
    path.logs: /home/ec2-user/logs/logstash
    ```

### 실행 및 로그

---

### 참고 자료 링크

* https://opensearch.org/docs/latest/tools/index/#downloads
* https://opensearch.org/blog/introducing-logstash-input-opensearch-plugin-for-opensearch/
* https://opensearch.org/docs/latest/tools/logstash/index/#install-logstash
