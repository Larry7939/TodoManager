package com.todomanager.todomanager.repository.local

import android.content.SharedPreferences
import com.todomanager.todomanager.dto.Profile

interface LocalRepository {
    fun setIsRegistered(isRegistered: Boolean)
    fun getIsRegistered(): Boolean
    fun setProfile(profile: Profile)
    fun getProfile(): Profile
}