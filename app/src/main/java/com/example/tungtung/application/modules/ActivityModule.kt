package com.example.tungtung.application.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by josephmagara on 9/10/20.
 */
@Module
@InstallIn(ApplicationComponent::class)
object ActivityModule {

    @Provides
    fun providesApplicationContext(application: Application): Context = application
}