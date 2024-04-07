package com.todomanager.todomanager.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    var id: String,
    var name: String,
    val taskDate: TaskDate
)