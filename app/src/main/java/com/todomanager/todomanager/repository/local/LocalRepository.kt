package com.todomanager.todomanager.repository.local

import android.content.SharedPreferences
import com.todomanager.todomanager.dto.Profile

interface LocalRepository {
    fun setProfile(profile: Profile)
    fun getProfile(): Profile
}