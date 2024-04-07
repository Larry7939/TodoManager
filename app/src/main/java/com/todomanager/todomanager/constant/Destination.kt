package com.todomanager.todomanager.constant

object Destination {
    const val SPLASH = "splash_screen"
    const val REGISTER = "register_screen"
    const val REGISTER_WITH_ARG = "$REGISTER?${NavArgKey.PROFILE_IMAGE_KEY}={${NavArgKey.PROFILE_IMAGE_KEY}}"
    const val CAMERA = "camera_screen"
    const val REGISTER_COMPLETE = "register_complete_screen"
    const val TASK_MAIN = "task_main_screen"
    const val TASK_ADD = "task_add_screen"
    const val TASK_EDIT = "task_edit_screen"
}