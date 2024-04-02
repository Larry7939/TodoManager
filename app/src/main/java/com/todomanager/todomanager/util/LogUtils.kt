package com.todomanager.todomanager.util

import com.todomanager.todomanager.BuildConfig
import timber.log.Timber

inline fun devLog(block: () -> Unit) {
    if (BuildConfig.DEBUG.not()) {
        return
    }
    block.invoke()
}

inline fun devTimberLog(crossinline block: () -> String) {
    devLog {
        Timber.d(block())
    }
}