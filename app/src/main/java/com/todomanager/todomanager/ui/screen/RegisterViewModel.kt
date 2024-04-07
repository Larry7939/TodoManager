package com.todomanager.todomanager.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todomanager.todomanager.constant.IOState
import com.todomanager.todomanager.dto.Profile
import com.todomanager.todomanager.repository.local.LocalRepository
import com.todomanager.todomanager.util.devErrorLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val localRepositoryImpl: LocalRepository) :
    ViewModel() {

    private var _profileState = MutableStateFlow<Profile>(Profile())
    val profileState: StateFlow<Profile>
        get() = _profileState

    private var _getProfileState = MutableStateFlow<IOState>(IOState.IDLE)
    val getProfileState: StateFlow<IOState>
        get() = _getProfileState

    private var _setProfileState = MutableStateFlow<IOState>(IOState.IDLE)
    val setProfileState: StateFlow<IOState>
        get() = _setProfileState

    private var _getRegisteredState = MutableStateFlow<IOState>(IOState.IDLE)
    val getRegisteredState: StateFlow<IOState>
        get() = _getRegisteredState

    private var _isRegisteredState = MutableStateFlow<Boolean>(false)
    val isRegisteredState: StateFlow<Boolean>
        get() = _isRegisteredState

    fun setIsRegistered(isRegistered: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.setIsRegistered(isRegistered)
            }.onSuccess {}.onFailure {}
        }
    }

    fun getIsRegistered() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                localRepositoryImpl.getIsRegistered()
            }.onSuccess { isRegistered ->
                _isRegisteredState.update { isRegistered }
                _getRegisteredState.update { IOState.SUCCESS }
            }.onFailure {
                devErrorLog("")
                _getRegisteredState.update { IOState.FAILURE }
            }
        }
    }

    fun setProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                updateSetProfileState(IOState.LOADING)
                localRepositoryImpl.setProfile(profile)
            }.onSuccess {
                updateSetProfileState(IOState.SUCCESS)
            }.onFailure {
                devErrorLog("")
                updateSetProfileState(IOState.FAILURE)
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                updateGetProfileState(IOState.LOADING)
                localRepositoryImpl.getProfile()
            }.onSuccess { profile ->
                _profileState.update { profile }
                updateGetProfileState(IOState.SUCCESS)
            }.onFailure {
                devErrorLog("")
                updateGetProfileState(IOState.FAILURE)
            }
        }
    }

    fun updateSetProfileState(state: IOState) {
        _setProfileState.update { state }
    }

    private fun updateGetProfileState(state: IOState) {
        _getProfileState.update { state }
    }
}