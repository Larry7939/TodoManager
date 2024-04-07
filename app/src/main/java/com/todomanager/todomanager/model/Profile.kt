package com.todomanager.todomanager.model

import kotlinx.serialization.Serializable


@Serializable
data class Profile(
    val uri: String = "",
    val name: String = "",
    val birthday: String = ""
)
