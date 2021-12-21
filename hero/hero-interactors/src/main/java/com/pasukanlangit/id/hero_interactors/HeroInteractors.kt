package com.pasukanlangit.id.hero_interactors

import com.pasukanlangit.id.hero_datasource.cache.HeroCache
import com.pasukanlangit.id.hero_datasource.network.HeroService
import com.squareup.sqldelight.db.SqlDriver

data class HeroInteractors(
    val getHeroes: GetHeroes,
    val getHeroFromCache: GetHeroFromCache,
    val filterHeroes: FilterHeroes
){
    companion object Factory {
        fun build(sqlDriver: SqlDriver): HeroInteractors {
            val cache = HeroCache.build(sqlDriver)
            val api = HeroService.build()
            return HeroInteractors(
                getHeroes = GetHeroes(
                    api = api,
                    cache = cache
                ),
                getHeroFromCache = GetHeroFromCache(
                    cache = cache
                ),
                filterHeroes = FilterHeroes()
            )
        }

        val scheme: SqlDriver.Schema = HeroCache.scheme
        val dbName: String = HeroCache.dbName
    }
}

