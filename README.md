# CareFridge

> 알고리즘 TEAM 4 Term Project

<b>[목차]</b>

- [프로젝트 개요](#프로젝트-개요)
  - [프로젝트 설명](#프로젝트-설명)
- [알고리즘 소개](#알고리즘-소개)
  - [사용 알고리즘](#사용-알고리즘)
  - [초기 알고리즘 (Java) vs 프로젝트에 적용한 알고리즘 (Kotlin)](#초기-알고리즘-java-vs-프로젝트에-적용한-알고리즘-kotlin)
  - [실행 결과](#실행-결과)
- [참여 인원](#참여-인원)
- [Git 컨벤션](#git-컨벤션)
  - [commit message 형식](#commit-message-형식)
  - [Type 및 Emoji 정리](#type-및-emoji-정리)

<br>

## 프로젝트 개요

![header](https://capsule-render.vercel.app/api?type=waving&color=8AC9FB&height=250&section=header&text=CareFridge&fontSize=90)

### 프로젝트 설명

> 냉장고에 있는 재료를 바탕으로 메뉴를 추천해 주는 어플

<table>
  <tr>
    <td align="center"><b>CareFridge</b></td>
    <td align="center"><b>🛠️ 핵심 기능</b></td>
  </tr>
 <tr>
    <td align="left"><image width="250" alt="Logo" src="https://github.com/nahy-512/Algorithm_Team4/assets/101113025/5a2a147c-924f-46ca-986d-2870f1a8d5be"> </td>
    <td align="left">
      1. <b>재료 추가</b><br>
        - 재료 이름과 유통기한, 양, 선호도를 설정할 수 있습니다.<br>
        - 재료는 수정과 삭제도 가능합니다.<br><br>
      2. <b>저장한 재료 보기</b><br>
        - 현재 냉장고에 있는 재료를 리스트 형태로 시각화해서 보여줍니다.<br>
        - 유통기한까지 남은 재료의 디데이도 확인할 수 있습니다.<br><br>
      3. <b>메뉴 추천</b><br><t>
        - 냉장고에 있는 재료의 양, 유통기한, 선호도를 고려하여 재료마다 가중치를 계산하고, 가장 높은 점수를 가진 메뉴를 추천해 줍니다.<br>
        - 메뉴가 마음에 들지 않는다면 재추천을 받을 수도 있습니다.<br>
    </td>
  </tr>
</table>

<br><br>

## 알고리즘 소개

### 사용 알고리즘

<b><i>`Greedy`</b></i>

> 각 단계에서 현재 상황에서 가장 최적인 선택을 하는 방식으로 문제를 해결하는 알고리즘
> 각 단계에서 지금 당장 가장 좋아 보이는 선택을 하는 것

- 간단하고 직관적인 구현이 가능하며, 특정 문제에 대해 전역 최적해를 보장하지는 않지만, 많은 경우에 꽤 효과적으로 동작
  </br

<b>`그리디 적용`</b>  
가능한 모든 음식 레시피를 확인하면서 각 음식에 대한 점수를 계산하고, 계산된 점수를 비교하여 현재까지 가장 높은 점수를 가진 음식을 최적의 메뉴로 선택

<br>

### 초기 알고리즘 (Java) vs 프로젝트에 적용한 알고리즘 (Kotlin)

<table>
  <tr>
    <td align="center"><b>유형</b></td>
    <td align="center" onClick="location.href='https://github.com/nahy-512/Algorithm_Team4/blob/main/MenuRecommendation.java'" style="cursor:pointer;"><b>초기 알고리즘</b></td> 
    <td align="center" onClick="location.href='https://github.com/nahy-512/Algorithm_Team4/blob/main/CareFridge/app/src/main/java/com/example/carefridge/algorithm/MenuRecommendAlgorithm.kt'" style="cursor:pointer;"><b>프로젝트에 적용한 알고리즘</b></td> 
  </tr>
 <tr>
    <td align="left"><b>프로젝트 동작 구조</b></td>
    <td align="left">
      1) 사용자에게 선호하는 재료를 입력 받기<br><br>
      2) 각 레시피에 대해 사용자의 선호 재료에 대한 점수를 계산하고 최종 점수를 기반으로 음식을 추천
    </td>
    <td align="left">
      1) 기본적으로 제공하는 레시피와 사용자가 미리 저장한 재료 리스트를 추천 알고리즘의 인자에 전달<br><br>
      2) 재료의 유통기한, 양, 선호도를 바탕으로 점수를 계산하고 최종 점수를 기반으로 음식을 추천
    </td>
  </tr>
  <tr>
    <td align="left"><b>가중치 부여 조건</b></td>
    <td align="left">
      - 음식 추천은 사용자가 선호하는 재료가 사용된 레시피에만 가중치를 부여하며, 사용자가 아무런 재료를 입력하지 않을 경우 모든 레시피를 대상으로 추천<br><br>
      - 양과 유통기한을 고려하여 각 재료에 대한 가중치를 계산
    </td>
    <td align="left">
      - 기본적으로는 양과 유통기한을 고려하여 각 재료에 대한 가중치를 계산<br><br>
      - 추가 가중치는 사용자가 선호하는 재료가 사용된 레시피에만 부여, 사용자가 아무런 재료를 입력하지 않을 경우 모든 레시피를 대상으로 추천
    </td>
  </tr>
</table>

### 실행 결과

#### [초기 알고리즘 코드 (Java)](https://github.com/nahy-512/Algorithm_Team4/blob/main/MenuRecommendation.java)</b>

<br>
<b>[초기화 부분]</b>
<table>
 <tr>
    <td align="center"> <image src="https://github.com/nahy-512/Algorithm_Team4/assets/112637518/d4bbbbdd-0c6d-4605-a5f0-e6780f9b4169"> </td>
  </tr>
  <tr>
    <td align="center"><b>설정한 재료와 해당하는 레시피(음식)</b></a></td>
      </tr>
</table>

<br>
<b>[코드 출력 결과]</b>
<table>
 <tr>
    <td align="center"><img src="https://github.com/nahy-512/Algorithm_Team4/assets/112637518/b9c220b7-fc83-4a6a-91f8-5cd8d019721a"></td>
    <td align="center"><img src="https://github.com/nahy-512/Algorithm_Team4/assets/112637518/6b799043-86d2-4fc3-8761-874a3806bafd"></td>
  </tr>
  <tr>
    <td align="center"><b>선호하는 재료를 입력하지 않았을 때</b></a></td>
    <td align="center"><b>선호하는 재료를 입력했을 때</b></a></td>
  </tr>
</table>

<br><br>

#### [프로젝트에 적용한 모습 (Kotlin)](https://github.com/nahy-512/Algorithm_Team4/blob/main/CareFridge/app/src/main/java/com/example/carefridge/algorithm/MenuRecommendAlgorithm.kt)</b>

<br>
<b>[코드 출력 결과]</b>
<table>
 <tr>
    <td align="center"><img width="167" alt="image" src="https://github.com/nahy-512/Markdown/assets/101113025/1cedb820-8bcf-437e-95e7-08f252a586a2"></td>
    <td align="center"><img width="185" alt="image" src="https://github.com/nahy-512/Markdown/assets/101113025/61eb1331-fd3b-4386-bc21-9e4e71c4bfc9"></td>
  </tr>
  <tr>
    <td align="center"><b>선호 재료가 없을 때</b></a></td>
    <td align="center"><b>선호 재료가 있을 때</b></a></td>
  </tr>
</table>

<br>
<b>[화면]</b>
<table>
 <tr>
    <td align="center"> <image width="250" src="https://media.discordapp.net/attachments/1124558739211558993/1181313300861042688/Screenshot_20231205-041223_CareFridge.jpg?ex=65809abe&is=656e25be&hm=23e867ee2f4309d701d545988150f41c5909848bfd644d62b733bceaab97a2de&=&format=webp&width=506&height=1124">
    </td>
    <td align="center"> <image width="250" src="https://media.discordapp.net/attachments/1124558739211558993/1181313301121073282/Screenshot_20231205-041538_CareFridge.jpg?ex=65809abe&is=656e25be&hm=65b33db3f5b3d4e1e5c2c71affe2ca9164fa70cba7f1b013352e2886f080ba4f&=&format=webp&width=506&height=1124">
    </td>
    <td align="center"><img width="250" src="https://media.discordapp.net/attachments/1124558739211558993/1181313301460815893/Screenshot_20231205-041607_CareFridge.jpg?ex=65809abe&is=656e25be&hm=03b4ff2f1952e7a263a0ff1c66395cc718a40642836a8831ce2606d102674d09&=&format=webp&width=506&height=1124">
    </td>
    <td align="center">
     <img width="250" src="https://media.discordapp.net/attachments/1124558739211558993/1181313301741846670/Screenshot_20231205-041622_CareFridge.jpg?ex=65809abe&is=656e25be&hm=3e4904d5169cdbc8f8822211b6c38fa894195637ea08dc2d58e280e70c880efd&=&format=webp&width=506&height=1124"></br>
    </td>
  </tr>
  <tr>
    <td align="center"><b>사용자가 미리 설정한 재료</b></a></td>
    <td align="center"><b>선호 여부 설정</b></a></td>
    <td align="center"><b>선호 재료가 없을 때</b></a></td>
    <td align="center"><b>선호 재료가 있을 때</b></a></td>
  </tr>
</table>

<br><br>

## 참여 인원

<table>
 <tr>
    <td align="center"><a href="https://github.com/nahy-512"><img src="https://avatars.githubusercontent.com/nahy-512" width="140px;" alt=""></a></td>
    <td align="center"><a href="https://github.com/yellowgree"><img src="https://avatars.githubusercontent.com/yellowgree" width="140px;" alt=""></a></td>
    <td align="center"><a href="https://github.com/jooo0198"><img src="https://avatars.githubusercontent.com/jooo0198" width="140px;" alt=""></a></td>
    <td align="center"><a href="https://github.com/lghyeon"><img src="https://avatars.githubusercontent.com/lghyeon" width="140px;" alt=""></a></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/nahy-512"><b>김나현</b></a></td>
    <td align="center"><a href="https://github.com/yellowgree"><b>박하은</b></a></td>
    <td align="center"><a href="https://github.com/jooo0198"><b>신주환</b></a></td>
    <td align="center"><a href="https://github.com/lghyeon"><b>이강현</b></a></td>
  </tr>
</table>

<br><br>

## Git 컨벤션

- [자세한 commit 규칙](https://github.com/GC-Project-Space/Convention/blob/main/forGithub/commit.md)

### Commit message 형식

> 커밋 메세지의 기본 포멧은 아래의 명세를 따릅니다.

```git
:Emoji: <type>: <subject>

<body>

<footer>
```

### Type 및 Emoji 정리

> 해당 커밋은 무엇에 대한 작업인지 키워드를 통해 표시합니다.

|   Type   |         Emoji         |                    Description                    |
| :------: | :-------------------: | :-----------------------------------------------: |
|   Feat   |     ✨(sparkles)      |                 새로운 기능 추가                  |
|   Fix    |       🐛 (bug)        |                     버그 수정                     |
|   Docs   |       📝 (memo)       |                     문서 수정                     |
|  Style   |       🎨 (art)        | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |
| Refactor |     ♻️ (recycle)      |                   코드 리팩토링                   |
|   Test   | ✅ (white_check_mark) |      테스트 코드, 리팩토링 테스트 코드 추가       |
|  Chore   |      🔧 (wrench)      |        빌드 업무 수정, 패키지 매니저 수정         |
