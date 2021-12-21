package com.pasukanlangit.id.mydotainfo.di

import com.pasukanlangit.id.constant.Constants.HERO_LIST_LOGGER
import com.pasukanlangit.id.core.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named(HERO_LIST_LOGGER)
    fun provideLogger(): Logger =
        Logger(tag = HERO_LIST_LOGGER)
}