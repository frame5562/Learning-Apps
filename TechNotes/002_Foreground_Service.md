# Foreground Service 개념 정리 (기본)

### 1. **포그라운드 서비스란?**
포그라운드 서비스는 사용자가 명확하게 인식해야 하는 작업을 실행할 때 사용하는 서비스다. 예를 들어, **음악 재생**, **파일 다운로드**, **위치 추적**과 같은 중요한 작업을 지속적으로 실행해야 할 때 사용된다.

#### 주요 특징
- **시스템에 의해 종료되지 않음**: 포그라운드 서비스는 메모리 부족 등으로 시스템에서 임의로 종료되지 않는다.
- **필수 알림**: 포그라운드 서비스는 항상 알림을 통해 사용자에게 표시되어야 한다.
- **장시간 실행**: 장시간 실행이 필요한 작업에 적합하며, 배터리 최적화 정책에 영향을 덜 받음.

### 2. **포그라운드 서비스 시작**
포그라운드 서비스는 `startForegroundService()` 메서드를 통해 시작된다. 이 메서드는 서비스가 시작된 후 반드시 **`startForeground()`** 메서드를 호출하여 알림을 제공해야 한다. 그렇지 않으면 시스템에서 서비스를 자동으로 종료할 수 있다.

#### 필수 단계 요약
1. **서비스 시작**: `startForegroundService()`로 서비스 시작.
2. **알림 표시**: `startForeground()` 메서드를 통해 알림을 제공.

### 3. **필수 구성요소**
포그라운드 서비스를 구성하기 위해 필요한 주요 구성요소는 아래와 같다.

#### 3.1. **Service 클래스**
서비스는 앱에서 백그라운드 작업을 처리할 수 있도록 해주는 컴포넌트다. 포그라운드 서비스를 사용하려면 `Service` 클래스를 상속받아야 한다.

```kotlin
class MyForegroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 서비스가 시작되면 실행할 작업
        startForegroundService()
        return START_NOT_STICKY
    }

    private fun startForegroundService() {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Service is running")
            .setSmallIcon(R.drawable.ic_service)
            .setContentIntent(pendingIntent)
            .build()
    }
}
```

#### 3.2. **알림(Notification)**
포그라운드 서비스는 반드시 알림을 통해 사용자에게 서비스가 실행 중임을 알려야 한다. 알림은 `NotificationCompat.Builder`를 사용해 생성하며, `startForeground()` 메서드로 이를 서비스와 연결한다.

#### 3.3. **Permission**
포그라운드 서비스를 사용하려면 AndroidManifest에 **권한**을 추가해야 한다. Android 9 (API 28) 이상에서는 `FOREGROUND_SERVICE` 권한이 필요하다.

```xml
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

### 4. **필수 알림 채널(Notification Channel)**
Android 8.0 (API 26) 이상에서는 알림을 표시할 때 **알림 채널(Notification Channel)** 을 설정해야 한다. 알림 채널은 사용자에게 특정 그룹의 알림을 관리할 수 있게 해준다.

```kotlin
private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}
```

### 5. **서비스 종료**
포그라운드 서비스는 더 이상 실행할 필요가 없을 때 **`stopForeground()`** 메서드를 호출하여 알림을 제거하고, **`stopSelf()`**로 서비스를 종료할 수 있다.

```kotlin
stopForeground(true)  // 알림 제거
stopSelf()  // 서비스 종료
```

### 6. **포그라운드 서비스의 사용 사례**
- **음악 앱**: 백그라운드에서 음악을 재생할 때 사용.
- **파일 다운로드**: 큰 파일을 다운로드하는 동안 서비스를 유지.
- **위치 기반 앱**: 사용자의 위치를 지속적으로 추적하는 앱에서 사용.

### 7. **주의사항**
- **알림 제공 필수**: 알림이 제공되지 않으면 포그라운드 서비스는 시스템에 의해 종료될 수 있다.
- **권한 관리**: Android 9 이상에서 포그라운드 서비스 권한을 명시해야 한다.
- **배터리 최적화**: 포그라운드 서비스는 배터리 최적화 기능에 영향을 덜 받지만, 불필요한 사용은 배터리 수명을 단축시킬 수 있으므로 적절히 종료해야 한다.
---

### 참고 문서
[Android 공식 문서 - Foreground Services](https://developer.android.com/develop/background-work/services/foreground-services?hl=ko)

---

<sub>작성일: 2024년 10월 21일</sub>