package com.example.dankbrowser.di

import android.app.Application
import android.content.Context
import com.example.dankbrowser.MyApplication
import com.example.dankbrowser.task_view.TaskList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.mozilla.geckoview.GeckoRuntime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BrowserModule {

    @Provides
    @Singleton
    fun geckoRuntime(@ApplicationContext applicationContext: Context): GeckoRuntime {
        return GeckoRuntime.create(applicationContext)
    }

    @Provides
    @Singleton
    fun getTaskList(): TaskList {
        return TaskList()
    }
}