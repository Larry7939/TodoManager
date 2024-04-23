package com.todomanager.todomanager.util

import android.app.ActivityManager
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object Utils {
    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun requestPermission(context: Context, permission: String, logic: () -> Unit) {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    logic()
                }

                override fun onPermissionDenied(deniedPermission: List<String>) {
                    context.showToast("권한을 허가해주세요.")
                }
            })
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(permission)
            .check()
    }

    fun Context.isServiceRunning(serviceName: String): Boolean {
        val activityManager =
            getSystemService(ComponentActivity.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceName == service.service.className) {
                return true
            }
        }
        return false
    }

    fun createTaskId(): String {
        return UUID.randomUUID().toString()
    }

    fun convertMillisToDate(pattern: String, millis: Long): String {
        val formatter = SimpleDateFormat(pattern, Locale.US).apply {
            timeZone = TimeZone.getTimeZone("GMT+09:00")
        }
        return formatter.format(Date(millis))
    }
}