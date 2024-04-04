package com.todomanager.todomanager.util

import android.Manifest
import android.content.Context
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

object Utils {
    fun requestPermission(context: Context, permission: String, logic : () -> Unit) {
        TedPermission.create()
            .setPermissionListener(object  : PermissionListener {
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
}