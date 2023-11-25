# Algorithm_Team4
> 알고리즘 TEAM 4 Term Project

<br>

**목차**
  - [프로젝트 개요](#프로젝트-개요)
  - 적용 알고리즘
  - [참여 인원](#참여-인원)
  - [Git 컨벤션](#git-컨벤션)
    - [commit message 형식](#commit-message-형식)
    - [Type 및 Emoji 정리](#type-및-emoji-정리)

<br>

## 프로젝트 개요

<b><주제></b>   
사용자가 선호하는 음식의 재료를 입력하고 그 안에서 재료의 양과 유통기한을 고려하여 가중치를 다르게 부여하고 그에 따라 가장 높은 값, 점수를 갖게 되는 음식을 최종으로 추천하는 프로그램 개발
(만약에 선호하는 음식을 입력하지 않고 enter 을 누를 시 없다고 판단하고 모든 재료를 대상으로 양과 유통기한을 고려)



<br><br>

## 적용 알고리즘
<b> <프로그램 구조> </b>   
  +java 로 작성
  1) 사용자에게 선호하는 재료를 입력 받기
  2) 각 레시피에 대해 사용자의 선호 재료에 대한 점수를 계산하고 최종 점수를 기반으로 음식을 추천

<b> <가중치 부여 조건>  </b>   
  - 음식 추천은 사용자가 선호하는 재료가 사용된 레시피에만 가중치를 부여하며, 사용자가 아무런 재료를 입력하지 않을 경우 모든 레시피를 대상으로 추천
  - 양과 유통기한을 고려하여 각 재료에 대한 가중치를 계산

<br>

<b><사용한 알고리즘></b>  
<b><i>Greedy </b></i>     
각 단계에서 현재 상황에서 가장 최적인 선택을 하는 방식으로 문제를 해결하는 알고리즘입니다. 다른 말로 하면, 각 단계에서 지금 당장 가장 좋아 보이는 선택을 하는 것   
- 간단하고 직관적인 구현이 가능하며, 특정 문제에 대해 전역 최적해를 보장하지는 않지만, 많은 경우에 꽤 효과적으로 동작   

<b> <적용> </b>    
가능한 모든 음식 레시피를 확인하면서 각 음식에 대한 점수를 계산하고 계산된 점수를 비교하여 현재까지 가장 높은 점수를 가진 음식을 최적의 메뉴로 선택

<br>

<b><실행 결과></b>   

<table>
 <tr>
    <td align="center"> <image src="https://github.com/nahy-512/Algorithm_Team4/assets/112637518/d4bbbbdd-0c6d-4605-a5f0-e6780f9b4169"> </td>
  </tr>
  <tr>
    <td align="center"><b>설정한 재료와 해당하는 레시피(음식)</b></a></td>
      </tr>
</table>



<table>
 <tr>
    <td align="center"><img src="https://github.com/nahy-512/Algorithm_Team4/assets/112637518/6b799043-86d2-4fc3-8761-874a3806bafd"></td>
    <td align="center"><img src="https://github.com/nahy-512/Algorithm_Team4/assets/112637518/b9c220b7-fc83-4a6a-91f8-5cd8d019721a"></td>
  </tr>
  <tr>
    <td align="center"><b>선호하는 재료를 입력했을 때</b></a></td>
    <td align="center"><b>선호하는 재료를 입력하지 않았을 때</b></a></td>
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

| Type | Emoji | Description |
|:----:|:-----:|:-----------:|
| Feat | ✨(sparkles) | 새로운 기능 추가 |
| Fix | 🐛 (bug) | 버그 수정 |
| Docs | 📝 (memo) | 문서 수정 |
| Style | 🎨 (art) | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |
| Refactor | ♻️ (recycle) | 코드 리팩토링 |
| Test | ✅ (white_check_mark) | 테스트 코드, 리팩토링 테스트 코드 추가 |
| Chore | 🔧 (wrench) | 빌드 업무 수정, 패키지 매니저 수정 |
