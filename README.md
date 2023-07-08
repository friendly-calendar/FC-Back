
## Kotlin Lint 사용 방법

- Intellij IDE 우측에  Tasks > formatting > ktlintFormat  : 린트 적용

- Intellij IDE 우측에  Tasks > verification > ktlintCheck  : 린트에 지정한 규칙에 위반한 코드가 있는지 검사
  ![img.png](lint_example.png)

또는 terminal 창을 열고 , 직접 gradle 커맨드를 통해 린트 검사를 진행하셔도 상관없습니다.

```bash
./gradlew ktlintCheck
```
- 예시 린트 검사 결과물
![img.png](lint_example_output.png)
## ERD

- https://app.diagrams.net/#G15SRu4vcVyurTIL-ECVzgrr-0e8MTbwnq

## Directory 구조

- entity      : RDBMS Entity
- repository
- service
- controller
