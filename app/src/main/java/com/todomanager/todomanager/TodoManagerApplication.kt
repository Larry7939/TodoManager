package com.todomanager.todomanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TodoManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        if (BuildConfig.DEBUG) {
            initTimber()
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun createNotificationChannel() {
        // API 26이상부터는 노티피케이션 채널을 만들어야한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).run{
                val foreChannel = NotificationChannel(
                    TASK_CHANNEL_ID, //아이디
                    "테스크 알림", //이름
                    NotificationManager.IMPORTANCE_LOW //중요도. 높을수록 사용자에게 알리는 강도가 높아짐
                )
                createNotificationChannel(foreChannel)
            }
        }
    }

    companion object {
        const val TASK_CHANNEL_ID = "task_channel_id"
    }
}