ec2 인스턴스 환경에서 ㅓㅇ를 버전별로 설치해 보자.

 
# corretto 설치, 삭제
## corretto17
```shell
$ sudo yum -y install java-17-amazon-corretto-devel
$ sudo yum -y remove java-17-amazon-corretto-devel
```
> /usr/lib/jvm/java-17-amazon-corretto.<cpu_arch>. 경로에 설치된다.

## corretto11
```shell
$ sudo yum -y install java-11-amazon-corretto-devel
$ sudo yum -y remove java-11-amazon-corretto-devel
```
> /usr/lib/jvm/java-11-amazon-corretto.<cpu_arch>. 경로에 설치된다.

## corretto8
```shell
$ sudo yum -y install java-1.8.0-amazon-corretto-devel
$ sudo yum -y remove java-1.8.0-amazon-corretto-devel
```
> /usr/lib/jvm/java-1.8.0-amazon-corretto.<cpu_arch>. 경로에 설치된다.
  
  
# OpenJDK 설치
## OpenJDK8
설치 가능한 jdk 리스트를 먼저 확인해보자.

```shell
$ sudo yum list java*jdk-devel

Loaded plugins: extras_suggestions, langpacks, priorities, update-motd
Available Packages
java-1.7.0-openjdk-devel.x86_64                               1:1.7.0.321-2.6.28.2.amzn2.0.1                               amzn2-core
java-1.8.0-openjdk-devel.x86_64                               1:1.8.0.362.b08-1.amzn2.0.1                                  amzn2-core
``` 

리스트에서 확인한 버전을 설치하자.

```shell
$ sudo yum -y install java-1.8.0-openjdk-devel.x86_64
```


# 원하는 JDK 버전으로 세팅되지 않았을 때
java -version 명령어를 입력하여 만약 원하는 jdk 버전이 출력되지 않는다면 아래 명령어를 통해 설정해 보면 된다.

```shell
$ sudo alternatives --config java
$ sudo alternatives --config javac
``` 
 

# 설치 경로 찾기
which java 명령어를 입력하면 출력되는 결과를 readlink 명령어에 사용해서 찾는다.

```shell
$ which java
/usr/bin/java


$ readlink -f /usr/bin/java
/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.362.b08-1.amzn2.0.1.x86_64/jre/bin/java
```


---

### 참고 자료 링크
* https://docs.aws.amazon.com/ko_kr/corretto/latest/corretto-17-ug/amazon-linux-install.html
* https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/amazon-linux-install.html
* https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/amazon-linux-install.html
