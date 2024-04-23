package com.todomanager.todomanager.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.todomanager.todomanager.R
import com.todomanager.todomanager.TodoManagerApplication.Companion.TASK_CHANNEL_ID
import com.todomanager.todomanager.repository.local.LocalRepository
import com.todomanager.todomanager.ui.MainActivity
import com.todomanager.todomanager.util.Utils.convertMillisToDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class TaskNotificationService : Service() {
    @Inject
    lateinit var localRepository: LocalRepository

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intentToMain = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intentToMain, PendingIntent.FLAG_IMMUTABLE)
        var time = 0
        var notificationId = System.currentTimeMillis().toInt()

        val notification = createNotification(pendingIntent)
        startForeground(notificationId, createEmptyNotification())
        CoroutineScope(CoroutineName("NotificationCoroutine") + Dispatchers.IO).launch {
            val manager = getSystemService(NotificationManager::class.java)
            while (true) {
                delay(60000L)
                time += 1
                val pattern = "yy'' MM/dd h:mm a"
                val taskDate = convertMillisToDate(pattern, System.currentTimeMillis())
                val taskList = localRepository.findTaskByTaskDate(taskDate)
                for (task in taskList) {
                    notification.setContentText(task.name)
                    manager.notify(notificationId, notification.build())
                    notificationId = System.currentTimeMillis()
                        .toInt() // Notification ID를 매번 갱신해줌으로써 매번 다른 알림을 띄워준다.
                }
            }
        }
        // START_STICKY: 서비스가 비정상 종료(메모리 정리)된 경우 서비스를 다시 시작 할 수 있도록 한다.
        // START_NOT_STICKY: 서비스가 비정상 종료(메모리 정리)된 경우 서비스가 다시 시작되지 않는다.
        return START_STICKY
    }

    private fun createEmptyNotification(): Notification {
        return NotificationCompat.Builder(this, TASK_CHANNEL_ID).build()
    }

    private fun createNotification(pendingIntent: PendingIntent): NotificationCompat.Builder {
        // notiBuilder: TodoManagerApplication에서 만든 notification channel을 바탕으로 notification의 출력 형태 지정
        // 채널 ID를 전달해주지 않으면 Deprecated 되었음을 알려준다.
        return NotificationCompat.Builder(this, TASK_CHANNEL_ID)
            .setContentTitle(getString(R.string.task_notification_now))
            .setSmallIcon(R.drawable.todolist_logo)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // 클릭 후에 자동으로 Notification 취소
    }

    override fun onBind(p0: Intent?): IBinder? = null
}