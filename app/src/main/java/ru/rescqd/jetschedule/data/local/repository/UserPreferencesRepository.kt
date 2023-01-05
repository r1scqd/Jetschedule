package ru.rescqd.jetschedule.data.local.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.rescqd.jetschedule.data.wrapper.ScheduleManageWrapper
import ru.rescqd.jetschedule.ui.home.HomeSections
import ru.rescqd.jetschedule.ui.screen.schedule.schedule.model.LeftNavigationDrawerItem
import ru.rescqd.jetschedule.ui.theme.*
import java.time.Duration

@ExperimentalSerializationApi
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
    private val workManager: WorkManager,
    private val scheduleManageWrapper: ScheduleManageWrapper
) {
    companion object {
        private val IS_FIRST_LAUNCH_KEY = booleanPreferencesKey("is_first_launch")
        private val SCHEDULE_PROVIDER_KEY = stringPreferencesKey("schedule_provider")
        private val TYPOGRAPHY_KEY = stringPreferencesKey("typography")
        private val COLOR_SCHEME_KEY = stringPreferencesKey("color_scheme")
        private val AUDIENCE_COLOR_SCHEME_KEY = stringPreferencesKey("audience_color_scheme")
        private val SCHEDULE_CORPUS = intPreferencesKey("schedule_corpus")
        private val IS_PERIODIC_WORK_KEY = booleanPreferencesKey("is_periodic_work")
        private val PERIODIC_WORK_REPEAT_TIME_KEY = stringPreferencesKey("work_repeat_time")
        private val PERIODIC_WORK_FLEX_TIME_KEY = stringPreferencesKey("work_flex_time")
        private val PERIODIC_WORK_THRESHOLD_KEY = longPreferencesKey("work_threshold")
        private val PERIODIC_WORK_DATE_OFFSET_KEY = longPreferencesKey("work_date_offset")
        private val START_DESTINATION_KEY = stringPreferencesKey("start_destination")
        private val APP_THEME_KEY = stringPreferencesKey("app_theme")
        private val SCHEDULE_LEFT_NAVIGATION_DRAWER_ITEMS_KEY =
            stringPreferencesKey("left_navigation_drawer_items")
        private val SCHEDULE_CURRENT_LEFT_NAVIGATION_DRAWER_ITEM_KEY =
            stringPreferencesKey("current_left_navigation_drawer_item")
    }

    private suspend inline fun <reified T> DataStore<Preferences>.setPreferenceEncode(
        key: Preferences.Key<String>,
        value: T,
    ) = edit { it[key] = Json.encodeToString(value) }

    private suspend inline fun <T> DataStore<Preferences>.setPreference(
        key: Preferences.Key<T>,
        value: T,
    ) = edit { it[key] = value }

    private fun <T> DataStore<Preferences>.getPreference(
        key: Preferences.Key<T>,
        defaultValue: T,
    ): Flow<T> = data.map { it[key] }.map { it ?: defaultValue }

    private inline fun <reified T> DataStore<Preferences>.getPreferenceDecode(
        key: Preferences.Key<String>,
        defaultValue: T,
    ): Flow<T> = data.map { it[key] }
        .map { if (it.isNullOrEmpty()) defaultValue else Json.decodeFromString(it) }


    fun getAppTheme(defaultValue: AppTheme = AppTheme.SYSTEM): Flow<AppTheme> =
        dataStore.getPreferenceDecode(APP_THEME_KEY, defaultValue)

    suspend fun setAppTheme(theme: AppTheme) = dataStore.setPreferenceEncode(APP_THEME_KEY, theme)

    fun getStartDestination(defaultValue: HomeSections): Flow<HomeSections> =
        dataStore.getPreferenceDecode(START_DESTINATION_KEY, defaultValue)

    suspend fun setStartDestination(startDestination: HomeSections) =
        dataStore.setPreferenceEncode(START_DESTINATION_KEY, startDestination)

    fun getScheduleBackendProvider(defaultValue: ScheduleBackendProvider = ScheduleBackendProvider.GROUP): Flow<ScheduleBackendProvider> =
        dataStore.getPreferenceDecode(SCHEDULE_PROVIDER_KEY, defaultValue)

    suspend fun setScheduleBackendProvider(provider: ScheduleBackendProvider) {
        dataStore.setPreferenceEncode(SCHEDULE_PROVIDER_KEY, provider)
        workManager.cancelAllWork()
        scheduleManageWrapper.changeBackendSideEffect()
    }


    suspend fun setThemeTypography(jetscheduleFontFamily: JetscheduleFontFamily) =
        dataStore.setPreferenceEncode(TYPOGRAPHY_KEY, jetscheduleFontFamily)

    fun getThemeTypography(defaultValue: JetscheduleFontFamily): Flow<JetscheduleFontFamily> =
        dataStore.getPreferenceDecode(TYPOGRAPHY_KEY, defaultValue)

    suspend fun setThemeColorScheme(jetscheduleColorScheme: JetscheduleColorScheme) =
        dataStore.setPreferenceEncode(COLOR_SCHEME_KEY, jetscheduleColorScheme)

    fun getThemeColorScheme(defaultValue: JetscheduleColorScheme): Flow<JetscheduleColorScheme> =
        dataStore.getPreferenceDecode(COLOR_SCHEME_KEY, defaultValue)

    suspend fun setThemeAudienceColorScheme(jetscheduleAudienceColorScheme: JetscheduleAudienceColorScheme) =
        dataStore.setPreferenceEncode(AUDIENCE_COLOR_SCHEME_KEY, jetscheduleAudienceColorScheme)

    fun getThemeAudienceColorScheme(defaultValue: JetscheduleAudienceColorScheme): Flow<JetscheduleAudienceColorScheme> =
        dataStore.getPreferenceDecode(AUDIENCE_COLOR_SCHEME_KEY, defaultValue)

    fun getIsFirstLaunch(defaultValue: Boolean = true): Flow<Boolean> =
        dataStore.getPreference(IS_FIRST_LAUNCH_KEY, defaultValue)

    suspend fun setIsFirstLaunch(value: Boolean) =
        dataStore.setPreference(IS_FIRST_LAUNCH_KEY, value)

    fun getScheduleCorpus(value: Int): Flow<Int> = dataStore.getPreference(SCHEDULE_CORPUS, value)

    suspend fun setScheduleCorpus(value: Int) = dataStore.setPreference(SCHEDULE_CORPUS, value)

    fun getIsPeriodicWorkEnabled(value: Boolean = false): Flow<Boolean> =
        dataStore.getPreference(IS_PERIODIC_WORK_KEY, value)

    suspend fun setIsPeriodicWorkEnabled(value: Boolean) =
        dataStore.setPreference(IS_PERIODIC_WORK_KEY, value)

    suspend fun setPeriodicWorkRepeatInterval(repeat: Duration) =
        dataStore.setPreferenceEncode(PERIODIC_WORK_REPEAT_TIME_KEY, repeat)

    fun getPeriodicWorkRepeatInterval(value: Duration): Flow<Duration> =
        dataStore.getPreferenceDecode(PERIODIC_WORK_REPEAT_TIME_KEY, value)

    fun getPeriodicWorkFlexInterval(value: Duration): Flow<Duration> =
        dataStore.getPreferenceDecode(PERIODIC_WORK_FLEX_TIME_KEY, value)

    suspend fun setPeriodicWorkFlexInterval(flex: Duration) =
        dataStore.setPreferenceEncode(PERIODIC_WORK_FLEX_TIME_KEY, flex)

    fun getPeriodicWorkThreshold(defaultValue: Long): Flow<Long> =
        dataStore.getPreference(PERIODIC_WORK_THRESHOLD_KEY, defaultValue)

    suspend fun setPeriodicWorkThreshold(threshold: Long) =
        dataStore.setPreference(PERIODIC_WORK_THRESHOLD_KEY, threshold)

    fun getPeriodicWorkDateOffset(value: Long): Flow<Long> =
        dataStore.getPreference(PERIODIC_WORK_DATE_OFFSET_KEY, value)

    suspend fun setPeriodicWorkDateOffset(offset: Long) =
        dataStore.setPreference(PERIODIC_WORK_DATE_OFFSET_KEY, offset)

    fun getLeftNavDrawerItems(defaultValue: List<LeftNavigationDrawerItem> = emptyList()): Flow<List<LeftNavigationDrawerItem>> =
        dataStore.getPreferenceDecode(SCHEDULE_LEFT_NAVIGATION_DRAWER_ITEMS_KEY, defaultValue)

    suspend fun setLeftNavDrawerItems(items: List<LeftNavigationDrawerItem>) =
        dataStore.setPreferenceEncode(SCHEDULE_LEFT_NAVIGATION_DRAWER_ITEMS_KEY, items)

    fun getCurrentLeftNavDrawerItem(defaultValue: LeftNavigationDrawerItem): Flow<LeftNavigationDrawerItem> =
        dataStore.getPreferenceDecode(
            SCHEDULE_CURRENT_LEFT_NAVIGATION_DRAWER_ITEM_KEY, defaultValue
        )

    suspend fun setCurrentLeftNavDrawerItem(item: LeftNavigationDrawerItem) =
        dataStore.setPreferenceEncode(SCHEDULE_CURRENT_LEFT_NAVIGATION_DRAWER_ITEM_KEY, item)
}