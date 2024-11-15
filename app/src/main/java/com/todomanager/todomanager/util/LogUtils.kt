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

fun devErrorLog(message: String = "") = Timber.tag(buildTag()).e(message)

private fun buildTag(): String =
    Thread.currentThread().stackTrace[4].let { ste ->
        "${ste.fileName}:${ste.lineNumber}#${ste.methodName}"
    }

