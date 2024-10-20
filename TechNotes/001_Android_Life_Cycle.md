# Android : Activity & Fragment Life Cycle 정리

### 1. Activity 생명주기

<div align="center">
  <img src="./000_images/001_1.png" alt="Android Life Cycle" width="258" height="331">
</div>

액티비티는 Android 앱의 화면을 구성하는 핵심 컴포넌트다. 사용자와 상호작용할 수 있는 상태로 이동하기까지 여러 단계가 존재하며, 시스템에 의해 다양한 상황에서 상태가 관리된다.

#### 주요 생명주기 메서드

- **onCreate()**:  
  액티비티가 생성될 때 호출된다. UI 초기화 및 리소스 로드, `setContentView()`로 레이아웃을 설정한다.
  
- **onStart()**:  
  액티비티가 사용자에게 보이기 직전 호출된다. UI 준비 등 화면 표시 전 단계에서 필요한 작업을 처리한다.
  
- **onResume()**:  
  <span style="color:aqua">**액티비티가 포그라운드에 올라오고, 사용자와 상호작용할 수 있을 때</span> 호출된다.**  
  이 시점부터 사용자 입력을 받기 시작하며, 상호작용이 가능해진다.
  
- **onPause()**:  
  액티비티가 포그라운드에서 벗어나기 직전에 호출된다. 데이터 저장, 애니메이션 일시 정지 등 자원 해제 작업을 처리한다.
  
- **onStop()**:  
  <span style="color:aqua">**액티비티가 사용자에게 더 이상 보이지 않게 될 때 호출</span> 된다. 시스템이 액티비티를 백그라운드로 보낸다.** 
  자원이 소모되는 작업들을 중단하는 시점이다.
  
- **onRestart()**:  
  `onStop()` 상태에서 다시 포그라운드로 전환될 때 호출된다. 액티비티가 재시작될 준비를 한다.
  
- **onDestroy()**:  
  액티비티가 종료될 때 호출된다. 모든 자원을 해제하고 메모리를 정리한다.

#### 생명주기 흐름 요약

```
onCreate() -> onStart() -> onResume() -> [실행 중] -> onPause() -> onStop() -> onDestroy()
```

#### 중간 흐름
- **onPause() -> onResume()**: 액티비티가 잠시 보이지 않다가 다시 나타날 때 발생한다.
- **onStop() -> onRestart() -> onStart()**: 백그라운드에 있던 액티비티가 다시 포그라운드로 올라올 때 발생한다.

---

### 2. Fragment 생명주기

<div align="center">
  <img src="./000_images/001_2.png" alt="Android Life Cycle" width="258" height="331">
</div>


프래그먼트는 액티비티 내의 UI 구성 요소로, 독립적인 생명주기를 가진다. 액티비티의 생명주기에 따라 프래그먼트도 상태가 변하며 추가적인 메서드들이 있다.

#### 주요 생명주기 메서드

- **onAttach()**:  
  프래그먼트가 액티비티에 처음 연결될 때 호출된다.
  
- **onCreate()**:  
  프래그먼트가 생성될 때 호출된다. 프래그먼트의 초기화 작업을 처리하지만, UI는 아직 생성되지 않는다.
  
- **onCreateView()**:  
  프래그먼트의 UI를 생성할 때 호출된다. 레이아웃을 inflate하여 화면을 구성한다.
  
- **onActivityCreated()**:  
  액티비티의 `onCreate()`가 완료된 후 호출된다.
  
- **onStart()**:  
  프래그먼트가 사용자에게 보이기 시작할 때 호출된다.
  
- **onResume()**:  
  프래그먼트가 포그라운드로 올라와 상호작용할 수 있을 때 호출된다.
  
- **onPause()**:  
  프래그먼트가 포그라운드에서 벗어날 때 호출된다.
  
- **onStop()**:  
  <span style="color:red">**프래그먼트가 더 이상 사용자에게 보이지 않을 때 호출된다.**</span> 자원 관리를 위해 중요한 메서드다.
  
- **onDestroyView()**:  
  프래그먼트의 뷰가 제거될 때 호출된다.
  
- **onDestroy()**:  
  프래그먼트가 파괴될 때 호출된다.
  
- **onDetach()**:  
  프래그먼트가 액티비티에서 분리될 때 호출된다.

#### 생명주기 흐름 요약

```
onAttach() -> onCreate() -> onCreateView() -> onActivityCreated() -> onStart() -> onResume() -> [실행 중] -> onPause() -> onStop() -> onDestroyView() -> onDestroy() -> onDetach()
```

---

### 3. 상태 저장 및 복구

- **onSaveInstanceState()**:  
  화면 회전 등으로 인해 액티비티가 일시적으로 종료될 때 상태를 저장하는 메서드다. `Bundle` 객체를 사용해 UI 상태를 저장한다.
  
- **onRestoreInstanceState()**:  
  `onCreate()` 또는 `onStart()` 단계에서 `onSaveInstanceState()`에 저장된 데이터를 복구한다. 일관된 사용자 경험을 제공하는 데 도움을 준다.

---

### 생명주기 관리의 중요성

적절한 생명주기 관리는 **효율적인 자원 관리**와 **일관된 사용자 경험**을 제공하는 데 필수적이다.  
액티비티나 프래그먼트가 화면에서 사라지면 불필요한 자원을 해제하고, 시스템 상태 변화에 따른 데이터 손실을 방지하는 방식으로 최적화할 수 있다.

---

### 참고
[Android 공식 문서 - Activity Lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle?hl=ko)

[Android 공식 문서 - Fragment Lifecycle](https://developer.android.com/guide/fragments/lifecycle?hl=ko)

---

<sub>작성일: 2024년 10월 21일</sub>