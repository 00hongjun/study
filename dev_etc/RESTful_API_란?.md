# Restful API 란?


## API 란?
먼저 API가 무엇인지 정리해 보자.  
API는 `Application programming interface`에서 각 앞 글자를 줄인 말이다. 여기서 Application은 어떤 기능과 데이터를 제공하는 하나의 서비스를 말하며, 이러한 서비스 간의 통신을 할 수 있게 해주는 일종의 interface 규약을 API라 말한다.  
각각의 서비스들은 자신의 API를 제공하고 다른 서비스의 API를 통해 필요한 정보들을 요청하고 응답받는다.  


## REST API
`Representational State Transfer API`의 약자이다.  
[REST API 논문](https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm)에서 처음 제안되었으며, 해당 논문에서 Roy Fielding은 REST의 개념, 설계 원칙, 장점 등을 설명하고 있다.  

위 논문의 내용을 resource에 대한 부분만 아주 간단하게 요약해 보았다.(많이 추상적이라 참고만 하는 용도가 적당한 것 같다.)  
논문에서 말하는 REST의 핵심 추상화 요소는 resource이다. 이름을 지정할 수 있는 모든 정보는 resource가 될 수 있으며 이미지, 동영상, 텍스트, 숫자 또는 모든 유형의 데이터 일수 있다.  아래의 특징을 가진다.  
- resource는 식별 가능하다.  
- resource는 상태를 가진다.  
- resource는 조작 가능하다.  
- resource는 다른 resource와 연결 가능하다. -> resource간의 관계를 정의할 수 있다.   
- resource는 식별자를 이용하여 구문하며, URI로 표현한다.  

해당 논문에는 아래와 같은 REST의 요소들에 대해서도 정리하고 있다.  
![REST_Data_Elements](/picture/etc/REST_Data_Elements.png)


REST는 전체적으로 적용될 때 구성 요소 상호 작용의 확장성, 인터페이스의 일반성, 구성 요소의 독립적인 배포 및 중간 구성 요소를 강조하여 상호 작용 대기 시간을 이고 보안을 강화하며 레거시 시스템을 캡슐화하는 일련의 아키텍처 제약 조건을 제공합니다.



---

### 참고 자료
* https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm#sec_5_1