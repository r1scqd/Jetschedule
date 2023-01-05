package ru.rescqd.jetschedule.ui.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ru.rescqd.jetschedule.ui.components.SnackbarManager

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {
    @ActivityRetainedScoped
    @Provides
    fun provideSnackbarManager(): SnackbarManager = SnackbarManager
}