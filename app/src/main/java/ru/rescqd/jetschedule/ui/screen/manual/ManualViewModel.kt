package ru.rescqd.jetschedule.ui.screen.manual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.data.local.repository.UserPreferencesRepository
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
@HiltViewModel
class ManualViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    fun setIsFirstLaunch(value: Boolean){
        viewModelScope.launch {
            userPreferencesRepository.setIsFirstLaunch(value)
        }
    }
}