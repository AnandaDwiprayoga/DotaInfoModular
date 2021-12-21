package com.pasukanlangit.id.hero_datasource.network

import com.pasukanlangit.id.hero_datasource.network.model.HeroDto
import com.pasukanlangit.id.hero_datasource.network.model.toHero
import com.pasukanlangit.id.hero_domain.Hero
import io.ktor.client.*
import io.ktor.client.request.*

class HeroServiceImpl(
    private val httpClient: HttpClient
): HeroService {
    override suspend fun getHeroStats(): List<Hero> =
        httpClient.get<List<HeroDto>> {
            url(Endpoints.HERO_STATS)
        }.map { it.toHero() }
}