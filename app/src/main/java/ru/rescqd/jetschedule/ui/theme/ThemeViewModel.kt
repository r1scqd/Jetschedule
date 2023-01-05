package ru.rescqd.jetschedule.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.data.local.repository.UserPreferencesRepository
import ru.rescqd.jetschedule.ui.home.HomeSections
import javax.inject.Inject


@HiltViewModel
@OptIn(ExperimentalSerializationApi::class)
class ThemeViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    fun isFirstLaunchFlow() = userPreferencesRepository.getIsFirstLaunch()
    fun getStartDestinationValue() = runBlocking { userPreferencesRepository.getStartDestination(HomeSections.SETTINGS).first() }

    fun isFirstLaunchValue() = runBlocking { isFirstLaunchFlow().first() }
    fun getAppThemeFlow(): Flow<AppTheme> = userPreferencesRepository.getAppTheme()
    fun getAppThemeValue() = runBlocking { getAppThemeFlow().first() }
    fun getColorSchemeFlow() = userPreferencesRepository.getThemeColorScheme(JetscheduleColorScheme.AndroidTheme)
    fun getColorSchemeValue() = runBlocking { getColorSchemeFlow().first() }
    fun getThemeTypographyFlow() = userPreferencesRepository.getThemeTypography(
        JetscheduleFontFamily.SystemDefault)
    fun getThemeTypographyValue() = runBlocking { getThemeTypographyFlow().first() }
    fun getThemeAudienceColorSchemeFlow() = userPreferencesRepository.getThemeAudienceColorScheme(
        JetscheduleAudienceColorScheme.Android)
    fun getThemeAudienceColorSchemeValue() = runBlocking {getThemeAudienceColorSchemeFlow().first() }

    fun setIsFirstLaunch(value: Boolean){
        viewModelScope.launch { userPreferencesRepository.setIsFirstLaunch(value) }
    }
    fun setAppTheme(value: AppTheme){
        viewModelScope.launch { userPreferencesRepository.setAppTheme(value) }
    }

    fun setColorScheme(value: JetscheduleColorScheme){
        viewModelScope.launch { userPreferencesRepository.setThemeColorScheme(value) }
    }

    fun setTypography(value: JetscheduleFontFamily){
        viewModelScope.launch { userPreferencesRepository.setThemeTypography(value) }
    }

    fun setAudienceColorScheme(value: JetscheduleAudienceColorScheme){
        viewModelScope.launch { userPreferencesRepository.setThemeAudienceColorScheme(value) }
    }
}