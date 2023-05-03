# EC2 환경에서 JDK 설치

ec2 인스턴스 환경에서 JDK를 버전별로 설치해 보자.

## corretto

* corretto17
    ```shell
    $ sudo yum -y install java-17-amazon-corretto-devel
    $ sudo yum -y remove java-17-amazon-corretto-devel
    ```
  > /usr/lib/jvm/java-17-amazon-corretto.<cpu_arch>. 경로에 설치된다.

* corretto11
    ```shell
    $ sudo yum -y install java-11-amazon-corretto-devel
    $ sudo yum -y remove java-11-amazon-corretto-devel
    ```
  > /usr/lib/jvm/java-11-amazon-corretto.<cpu_arch>. 경로에 설치된다.

* corretto8
    ```shell
    # Amazon Linux 2
    $ sudo yum -y install java-1.8.0-amazon-corretto-devel
    $ sudo yum -y remove java-1.8.0-amazon-corretto-devel
  
    # Amazon Linux 2023
    $ sudo yum list java*
    $ sudo yum -y install java-1.8.0-amazon-corretto-devel.x86_64
    ```
  > /usr/lib/jvm/java-1.8.0-amazon-corretto.<cpu_arch>. 경로에 설치된다.

## OpenJDK 설치

* OpenJDK8
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

## 원하는 JDK 버전으로 세팅되지 않았을 때

* java -version 명령어를 입력하여 만약 원하는 jdk 버전이 출력되지 않는다면 아래 명령어를 통해 설정해 보면 된다.
  ```shell
  $ sudo alternatives --config java
  $ sudo alternatives --config javac
  ``` 

* Amazon Linux 2023에서는 패키지 관리자가 변경되었다.(yum의 후속 버전인 Fedora’s dnf가 함께 제공)    
  패키지 관리자에서 corretto 버전의 JDK만 기본으로 제공된다.
  참고 [링크1](https://aws.amazon.com/ko/blogs/korea/amazon-linux-2023-a-cloud-optimized-linux-distribution-with-long-term-support/)
  , [링크2](https://docs.aws.amazon.com/linux/al2023/ug/compare-with-al2.html)
  ```shell
  # dnf list java* 명령어도 같은 결과를 출력한다.
  $ yum list java*
  
  Last metadata expiration check: 0:16:59 ago on Wed May  3 02:08:45 2023.
  Available Packages
  java-1.8.0-amazon-corretto.x86_64                           1:1.8.0_362.b08-1.amzn2023                                    amazonlinux
  java-1.8.0-amazon-corretto-devel.x86_64                     1:1.8.0_362.b08-1.amzn2023                                    amazonlinux
  java-11-amazon-corretto.x86_64                              1:11.0.18+10-1.amzn2023                                       amazonlinux
  java-11-amazon-corretto-devel.x86_64                        1:11.0.18+10-1.amzn2023                                       amazonlinux
  java-11-amazon-corretto-headless.x86_64                     1:11.0.18+10-1.amzn2023                                       amazonlinux
  java-11-amazon-corretto-javadoc.x86_64                      1:11.0.18+10-1.amzn2023                                       amazonlinux
  java-11-amazon-corretto-jmods.x86_64                        1:11.0.18+10-1.amzn2023                                       amazonlinux
  java-17-amazon-corretto.x86_64                              1:17.0.6+10-1.amzn2023.1                                      amazonlinux
  java-17-amazon-corretto-devel.x86_64                        1:17.0.6+10-1.amzn2023.1                                      amazonlinux
  java-17-amazon-corretto-headless.x86_64                     1:17.0.6+10-1.amzn2023.1                                      amazonlinux
  java-17-amazon-corretto-javadoc.x86_64                      1:17.0.6+10-1.amzn2023.1                                      amazonlinux
  java-17-amazon-corretto-jmods.x86_64                        1:17.0.6+10-1.amzn2023.1                                      amazonlinux
  java_cup.noarch                                             1:0.11b-21.amzn2023.0.3                                       amazonlinux
  java_cup-javadoc.noarch                                     1:0.11b-21.amzn2023.0.3                                       amazonlinux
  java_cup-manual.noarch                                      1:0.11b-21.amzn2023.0.3                                       amazonlinux
  javacc.noarch                                               7.0.4-11.amzn2023.0.1                                         amazonlinux
  javacc-demo.noarch                                          7.0.4-11.amzn2023.0.1                                         amazonlinux
  javacc-javadoc.noarch                                       7.0.4-11.amzn2023.0.1                                         amazonlinux
  javacc-manual.noarch                                        7.0.4-11.amzn2023.0.1                                         amazonlinux
  javacc-maven-plugin.noarch                                  2.6-35.amzn2023.0.1                                           amazonlinux
  javacc-maven-plugin-javadoc.noarch                          2.6-35.amzn2023.0.1                                           amazonlinux
  javapackages-bootstrap.noarch                               1.5.0^20220105.git9f283b7-3.amzn2023.0.2                      amazonlinux
  javapackages-filesystem.noarch                              6.0.0-7.amzn2023.0.5                                          amazonlinux
  javapackages-generators.noarch                              6.0.0-7.amzn2023.0.5                                          amazonlinux
  javapackages-local.noarch                                   6.0.0-7.amzn2023.0.5                                          amazonlinux
  javapackages-tools.noarch                                   6.0.0-7.amzn2023.0.5                                          amazonlinux
  javaparser.noarch                                           3.22.0-3.amzn2023.0.1                                         amazonlinux
  javaparser-javadoc.noarch                                   3.22.0-3.amzn2023.0.1                                         amazonlinux
  javassist.noarch                                            3.28.0-4.amzn2023.0.1                                         amazonlinux
  javassist-javadoc.noarch                                    3.28.0-4.amzn2023.0.1                                         amazonlinux
  ```

## 설치 경로 찾기

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
