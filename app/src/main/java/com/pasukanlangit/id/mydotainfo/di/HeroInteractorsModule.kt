package com.pasukanlangit.id.mydotainfo.di

import android.app.Application
import com.pasukanlangit.id.hero_interactors.HeroInteractors
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroInteractorsModule {

    const val DRIVER_DB_HERO_NAME = "heroAndroidSqlDriver"

    @Provides
    @Singleton
    @Named(DRIVER_DB_HERO_NAME) //in case you had another SQL Delight db
    fun provideAndroidDriver(app: Application): SqlDriver =
        AndroidSqliteDriver(
            schema = HeroInteractors.scheme,
            context = app,
            name = HeroInteractors.dbName
        )

    @Provides
    @Singleton
    fun provideHeroInteractors(
        @Named(DRIVER_DB_HERO_NAME) sqlDriver: SqlDriver
    ): HeroInteractors = HeroInteractors.build(sqlDriver = sqlDriver)

}