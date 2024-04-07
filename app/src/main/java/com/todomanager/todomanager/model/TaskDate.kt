package com.todomanager.todomanager.model

import kotlinx.serialization.Serializable

@Serializable
data class TaskDate(
    var year: String = "",
    var monthDate: String = "",
    var hourMinute: String = ""
) {
    override fun toString(): String {
        return if (isEmpty().not()) {
            "${year.subSequence(2,4)}' $monthDate $hourMinute"
        } else {
            ""
        }
    }
    fun toStringForSorting(): String {
        return "$year $monthDate $hourMinute"
    }
    fun isEmpty(): Boolean {
        return year.isEmpty() && monthDate.isEmpty() && hourMinute.isEmpty()
    }
}