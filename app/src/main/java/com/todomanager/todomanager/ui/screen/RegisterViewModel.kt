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
class RegisterViewModel @Inject constructor(private val localRepositoryImpl: LocalRepository): ViewModel() {

    private var _setProfileState = MutableStateFlow<IOState>(IOState.IDLE)
    val setProfileState:StateFlow<IOState>
        get() = _setProfileState

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
    fun updateSetProfileState(state: IOState) {
        _setProfileState.update { state }
    }
}