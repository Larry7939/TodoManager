package com.todomanager.todomanager.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.todomanager.todomanager.dto.Profile
import com.todomanager.todomanager.util.devErrorLog
import com.todomanager.todomanager.util.devTimberLog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
    private val json: Json
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(LOCAL_PREFS_NAME, Context.MODE_PRIVATE)
    }

//    val profile: Profile
//        set(value) = prefs.edit {
//        }

    @OptIn(ExperimentalSerializationApi::class)
    fun setProfile(profile: Profile) {
        return prefs.edit {
            putString(KEY_PROFILE, json.encodeToString(profile))
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun getProfile(): Profile {
        val jsonProfile = prefs.getString(KEY_PROFILE, null)?: return Profile()
        return try {
            json.decodeFromString<Profile>(jsonProfile)
        } catch (e: SerializationException) {
            e.message?.let {
                devErrorLog(it)
            }
            Profile()
        } catch (e: IllegalArgumentException) {
            e.message?.let {
                devErrorLog(it)
            }
            Profile()
        }
    }

    fun setIsRegistered(isRegistered: Boolean) {
        prefs.edit { putBoolean(KEY_IS_REGISTERED, isRegistered) }
    }

    fun getIsRegistered(): Boolean {
        return prefs.getBoolean(KEY_IS_REGISTERED, false)
    }

    companion object {
        private const val LOCAL_PREFS_NAME = "local_prefs"
        private const val KEY_PROFILE = "profile"
        private const val KEY_IS_REGISTERED = "is_registered"
        @Volatile
        private var instance: LocalDataSource? = null
        fun getInstance(context: Context, json: Json): LocalDataSource {
            return instance ?: synchronized(this) {
                LocalDataSource(context, json).also { instance = it }
            }
        }
    }
}