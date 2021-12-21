package com.pasukanlangit.id.ui_herolist.di

import com.pasukanlangit.id.core.Logger
import com.pasukanlangit.id.hero_interactors.FilterHeroes
import com.pasukanlangit.id.hero_interactors.GetHeroes
import com.pasukanlangit.id.hero_interactors.HeroInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroListModule {

    @Provides
    @Singleton
    fun provideGetHeroes(
        interactors: HeroInteractors
    ): GetHeroes = interactors.getHeroes

    @Provides
    @Singleton
    fun provideFilterHeroes(
        interactors: HeroInteractors
    ): FilterHeroes = interactors.filterHeroes
}