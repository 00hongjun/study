# 간단한 git 사용법 정리

* local로 clone
```text
git clone -b {브랜치} --single-branch {repository url}
```

* 브랜치 만들고 이동
```text
git checkout -b {브랜치}
```


* 수정사항 커밋
```text
git status //변경된 파일 확인
git add -A  (or) git add . //변경된 전체 파일 반영
git commit -m "메시지" //작업 내용에 대한 메시지 기록
```

* 로컬 브랜치를 원격 저장소에 올리기
```text
git push origin {브랜치}
```

* 브랜치 삭제
```text
git checkout {브랜치} //target이 아닌 브랜치로 이동
git branch -D {target브랜치} //target 브랜치 삭제
```

* third-party 저장소 추가
```text
git remote add {별칭} {remote repository url} //외부 저장소를 별칭으로 지정하여 등록
git remote -v
```
    * 특정 브랜치만 가져오기
    ```text
    git remote add -t {remote-branch} {별칭} {remote url}
    ```

* 원격 저장소와 동기화 하기
```text
git fetch upstream {브랜치}
```

* rebase
```text
git rebase upstream/{브랜치}
git rebase master HEAD~10 //head를 기준으로 10개의 커밋을 master에 반영
```