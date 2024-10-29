# BoundService 개념 정리


### 1. BoundService 개요
`BoundService`는 Android에서 컴포넌트(주로 Activity나 Fragment)가 서비스와 결합(bind)하여 상호작용할 수 있도록 하는 서비스 유형입니다. 이를 통해 클라이언트는 서비스에 대한 메서드를 호출하여 데이터를 요청하거나 특정 작업을 수행할 수 있습니다. 일반적으로 음악 재생, GPS 정보, 원격 데이터베이스 액세스와 같은 지속적인 작업을 수행할 때 사용됩니다.

`BoundService`는 클라이언트와 연결된 동안에만 실행됩니다. 클라이언트가 서비스에 바인딩을 해제하면 서비스도 종료될 수 있습니다. 이 방식은 클라이언트와 서비스 간에 강력한 상호작용이 필요한 경우에 유용합니다.

---

### 2. BoundService 구현 단계

#### Step 1: 서비스 클래스 작성

`Service` 클래스를 상속받아 `onBind` 메서드를 구현해야 합니다. `onBind`는 서비스가 바인딩될 때 호출되며, 클라이언트가 서비스와 상호작용할 수 있도록 `IBinder` 객체를 반환합니다.

```kotlin
class MyBoundService : Service() {

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MyBoundService = this@MyBoundService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun getRandomNumber(): Int {
        return (0..100).random()
    }
}
```

위 예제에서 `LocalBinder`는 클라이언트가 `getService()` 메서드를 통해 `MyBoundService`의 인스턴스에 접근할 수 있도록 합니다.

#### Step 2: 클라이언트에서 서비스 바인딩

`bindService` 메서드를 사용하여 클라이언트(Activity 또는 Fragment)가 서비스에 바인딩하고, 바인딩이 완료되면 `ServiceConnection` 인터페이스의 `onServiceConnected` 메서드가 호출됩니다.

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var myService: MyBoundService
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MyBoundService.LocalBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MyBoundService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }
}
```

여기서 `bindService` 메서드는 `onStart()`에서 호출되고, `unbindService`는 `onStop()`에서 호출되어 바인딩을 해제합니다.

---

### 3. BoundService와 클라이언트 간 데이터 교환

위 예제에서 `getRandomNumber()` 메서드를 통해 `MainActivity`에서 `MyBoundService`의 메서드를 호출할 수 있습니다. 이는 클라이언트가 서비스와 상호작용할 수 있는 일반적인 방법입니다.

```kotlin
if (isBound) {
    val number = myService.getRandomNumber()
    Log.d("BoundService", "Random Number: $number")
}
```

---

### 4. BoundService 사용 시 고려 사항

- **생명주기 관리**: 클라이언트의 생명주기에 따라 바인딩을 적절하게 설정/해제해야 메모리 누수를 방지할 수 있습니다.
- **여러 클라이언트 지원**: 여러 개의 클라이언트가 서비스와 바인딩될 수 있으며, 각 클라이언트가 `unbindService`를 호출하기 전까지 서비스는 종료되지 않습니다.
- **멀티스레딩**: `BoundService`는 기본적으로 메인 스레드에서 동작하므로, 긴 작업이 필요한 경우 `HandlerThread`나 `AsyncTask`, `Coroutine` 등을 활용해 백그라운드 작업을 수행해야 합니다.

---

### BoundService 활용 예제

BoundService는 음악 플레이어처럼 상태가 자주 변경되고, 사용자가 컨트롤해야 하는 기능을 가진 앱에서 자주 활용됩니다.

---

### 5. BoundService의 다양한 바인딩 옵션

- **Local Bound Service**: 같은 애플리케이션 내의 Activity나 Fragment가 바인딩할 때 사용합니다. 로컬 바인딩은 위에서 설명한 방식으로 `Binder`를 활용하여 직접 구현합니다.
  
- **Remote Bound Service (AIDL)**: 다른 애플리케이션이 서비스에 바인딩할 수 있도록 할 때 사용하며, Android Interface Definition Language(AIDL)를 사용해 원격 프로세스 간 통신을 설정해야 합니다. 이 경우 IPC(Inter-Process Communication)를 통해 다른 애플리케이션과 데이터를 주고받게 됩니다.

---

### 6. BoundService의 다양한 메서드

- **onRebind(Intent)**: 클라이언트가 `unbindService()`를 호출한 후 다시 연결될 때 호출됩니다. 일반적으로 재바인딩이 필요한 상황에서 활용합니다.
  
- **onUnbind(Intent)**: 모든 클라이언트가 `unbindService()`를 호출할 때 호출됩니다. 서비스가 더 이상 바인딩되지 않으면 자원을 해제하거나 필요한 정리 작업을 수행할 수 있습니다.

---

### 7. BoundService와 LifecycleService

**LifecycleService**를 사용하면 서비스에서 `Lifecycle`을 직접 관리할 수 있어, `Observer`를 통해 서비스의 상태 변화를 쉽게 감지할 수 있습니다. 예를 들어 `LiveData`를 사용하여 서비스 상태를 Activity나 Fragment에 전달할 수 있습니다. 이 방법을 통해 수명 주기를 더 효율적으로 관리하고, 클라이언트가 상태 변화에 쉽게 반응할 수 있습니다.

```kotlin
class MyLifecycleService : LifecycleService() {
    init {
        lifecycle.addObserver(MyServiceObserver())
    }
}

class MyServiceObserver : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartService() {
        // 서비스 시작 시 작업
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopService() {
        // 서비스 중지 시 작업
    }
}
```

이렇게 `LifecycleService`를 통해 서비스와 수명 주기를 연결하면, 더 안정적으로 리소스를 관리할 수 있습니다.

---


### 참고 문서
- [Android Developers 공식 문서 - Bound Services](https://developer.android.com/guide/components/bound-services?hl=ko)
- [Android Developers 공식 문서 - Services](https://developer.android.com/guide/components/services?hl=ko)
- [Android Developers 공식 문서 - LifecycleService](https://developer.android.com/reference/androidx/lifecycle/LifecycleService?hl=ko)
---

<sub>작성일: 2024년 10월 29일</sub>