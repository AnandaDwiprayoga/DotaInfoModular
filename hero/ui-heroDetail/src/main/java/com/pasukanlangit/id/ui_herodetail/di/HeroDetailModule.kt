package com.pasukanlangit.id.ui_herodetail.di

import com.pasukanlangit.id.core.Logger
import com.pasukanlangit.id.hero_interactors.GetHeroFromCache
import com.pasukanlangit.id.hero_interactors.HeroInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroDetailModule {
    @Provides
    @Singleton
    fun provideGetHeroFromCache(heroInteractors: HeroInteractors): GetHeroFromCache =
        heroInteractors.getHeroFromCache

//    @Provides
//    @Singleton
//    @Named(HERO_LIST_LOGGER)
//    fun provideLogger(): Logger =
//        Logger(tag = HERO_LIST_LOGGER)
}