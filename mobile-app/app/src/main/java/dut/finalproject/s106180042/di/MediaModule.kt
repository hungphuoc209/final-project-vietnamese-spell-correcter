package dut.finalproject.s106180042.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dut.finalproject.s106180042.ext.MediaManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvidesModule {
    @Singleton
    @Provides
    fun provideMediaManager(application: Application) = MediaManager(application)
}