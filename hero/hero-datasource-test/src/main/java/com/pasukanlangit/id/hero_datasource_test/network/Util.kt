package com.pasukanlangit.id.hero_datasource_test.network

import com.pasukanlangit.id.hero_datasource.network.model.HeroDto
import com.pasukanlangit.id.hero_datasource.network.model.toHero
import com.pasukanlangit.id.hero_domain.Hero
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
}

fun serializeHeroData(jsonData: String): List<Hero> =
    json.decodeFromString<List<HeroDto>>(jsonData).map { it.toHero() }