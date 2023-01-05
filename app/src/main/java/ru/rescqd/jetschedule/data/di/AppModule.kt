package ru.rescqd.jetschedule.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.OkHttpClient
import ru.rescqd.jetschedule.data.Contains
import ru.rescqd.jetschedule.data.local.database.JetscheduleDatabase
import ru.rescqd.jetschedule.data.local.database.dao.CustomSelectDao
import ru.rescqd.jetschedule.data.local.database.dao.ScheduleDropDao
import ru.rescqd.jetschedule.data.local.database.dao.ScheduleSaveDao
import ru.rescqd.jetschedule.data.local.repository.JetscheduleRepository
import ru.rescqd.jetschedule.data.local.source.TeacherFioDataSource
import ru.rescqd.jetschedule.data.local.repository.UserPreferencesRepository
import ru.rescqd.jetschedule.data.local.source.ScheduleRemoteDataSource
import ru.rescqd.jetschedule.data.remote.network.base.CollegeApi
import ru.rescqd.jetschedule.data.remote.source.OkHttpScheduleSource
import ru.rescqd.jetschedule.data.remote.okhttp.OkHttpConfig
import ru.rescqd.jetschedule.data.wrapper.ScheduleManageWrapper
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Contains.USER_PREFERENCES_NAME)

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideUserPreferenceRepository(
        @ApplicationContext ctx: Context,
        workManager: WorkManager,
        scheduleManageWrapper: ScheduleManageWrapper
    ): UserPreferencesRepository =
        UserPreferencesRepository(ctx.dataStore, workManager, scheduleManageWrapper)

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext ctx: Context): WorkManager =
        WorkManager.getInstance(ctx)

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext ctx: Context): JetscheduleDatabase =
        Room.databaseBuilder(ctx, JetscheduleDatabase::class.java, Contains.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideScheduleSelectDao(db: JetscheduleDatabase): CustomSelectDao = db.getCustomSelectDao()


    @Provides
    @Singleton
    fun provideDropDao(db: JetscheduleDatabase): ScheduleDropDao = db.getScheduleDropDao()


        @Provides
    @Singleton
    fun provideScheduleSaveDao(db: JetscheduleDatabase): ScheduleSaveDao = db.getScheduleSaveDao()

    @Provides
    @Singleton
    fun provideCollegeApi(config: OkHttpConfig): CollegeApi = OkHttpScheduleSource(config)

    @Provides
    @Singleton
    fun provideOkHttpConfig(okHttpClient: OkHttpClient): OkHttpConfig =
        OkHttpConfig(Contains.API_BASE_URL, okHttpClient)

    @Provides
    @Singleton
    fun provideSaveUseCase(
        saveDao: ScheduleSaveDao,
        dropDao: ScheduleDropDao,
        teacherFIODataSource: TeacherFioDataSource
    ): ScheduleManageWrapper = ScheduleManageWrapper(saveDao, teacherFIODataSource, dropDao)



    @Provides
    @Singleton
    fun provideTeacherFioDataSource(): TeacherFioDataSource = TeacherFioDataSource()

    @Provides
    @Singleton
    fun provideJetscheduleRepository(
        dropDao: ScheduleDropDao,
        selectDao: CustomSelectDao,
        scheduleRemoteDataSource: ScheduleRemoteDataSource
    ): JetscheduleRepository = JetscheduleRepository(
        dropDao, selectDao, scheduleRemoteDataSource
    )

    @Provides
    @Singleton
    fun provideScheduleRemoteDataSource(
        workManager: WorkManager,
        userPreferencesRepository: UserPreferencesRepository
    ): ScheduleRemoteDataSource =
        ScheduleRemoteDataSource(workManager, userPreferencesRepository)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .followRedirects(false)
            .build()

}
