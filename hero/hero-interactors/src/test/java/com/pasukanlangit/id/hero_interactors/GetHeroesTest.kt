package com.pasukanlangit.id.hero_interactors

import com.google.common.truth.Truth.assertThat
import com.pasukanlangit.id.core.DataState
import com.pasukanlangit.id.core.ProgressBarState
import com.pasukanlangit.id.core.UIComponent
import com.pasukanlangit.id.hero_datasource_test.cache.HeroCacheFake
import com.pasukanlangit.id.hero_datasource_test.cache.HeroDatabaseFake
import com.pasukanlangit.id.hero_datasource_test.network.HeroServiceFake
import com.pasukanlangit.id.hero_datasource_test.network.HeroServiceResponseType
import com.pasukanlangit.id.hero_datasource_test.network.data.HeroDataValid
import com.pasukanlangit.id.hero_datasource_test.network.serializeHeroData
import com.pasukanlangit.id.hero_domain.Hero
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetHeroesTest {
    //system in test
    private lateinit var getHeroes: GetHeroes

    @Test
    fun `Get heroes with valid data should success`() = runBlocking {
        val api = HeroServiceFake.build(
            type = HeroServiceResponseType.ValidData
        )
        val heroesCache = HeroCacheFake(
            db = HeroDatabaseFake()
        )
        getHeroes = GetHeroes(
            cache = heroesCache,
            api = api
        )

        assertThat(heroesCache.selectAll()).isEmpty()

        val emissions = getHeroes().toList()
        checkIsStateLoading(emissions.first())

        assertThat((emissions[1] as DataState.Data).data?.size).isEqualTo(HeroDataValid.NUM_HEROES)
        assertThat(heroesCache.selectAll().size).isEqualTo(HeroDataValid.NUM_HEROES)

        assertThat(emissions.last() as DataState.Loading).isEqualTo(DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Idle))
    }

    @Test
    fun `Get heroes with malformed returned data should thrown error`() = runBlocking {
        val api = HeroServiceFake.build(
            type = HeroServiceResponseType.MalformedData
        )
        val heroesCache = HeroCacheFake(
            db = HeroDatabaseFake()
        )
        getHeroes = GetHeroes(
            cache = heroesCache,
            api = api
        )

        assertThat(heroesCache.selectAll()).isEmpty()
        val heroData = serializeHeroData(HeroDataValid.data)
        heroesCache.insert(heroData)

        assertThat(heroesCache.selectAll().size).isEqualTo(HeroDataValid.NUM_HEROES)

        val emissions = getHeroes().toList()

        checkIsStateLoading(emissions.first())

        assertThat(emissions[1]).isInstanceOf(DataState.Response::class.java)
        assertThat((((emissions[1] as DataState.Response).uiComponent) as UIComponent.Dialog).title).isEqualTo("Network Data Error")
        assertThat((((emissions[1] as DataState.Response).uiComponent) as UIComponent.Dialog).description).contains("Unexpected JSON token at offset")

        // Confirm third emission is data from the cache
        assertThat(emissions[2]).isInstanceOf(DataState.Data::class.java)
        assertThat((emissions[2] as DataState.Data).data?.size).isEqualTo(HeroDataValid.NUM_HEROES)

        assertThat(heroesCache.selectAll().size).isEqualTo(HeroDataValid.NUM_HEROES)

        assertThat((emissions.last() as DataState.Loading).progressBarState).isEqualTo(ProgressBarState.Idle)
    }

    @Test
    fun `Get heroes with empty data should success`() = runBlocking {
        val api = HeroServiceFake.build(
            type = HeroServiceResponseType.EmptyList
        )
        val heroesCache = HeroCacheFake(
            db = HeroDatabaseFake()
        )
        getHeroes = GetHeroes(
            cache = heroesCache,
            api = api
        )

        assertThat(heroesCache.selectAll()).isEmpty()

        val emissions = getHeroes().toList()
        checkIsStateLoading(emissions.first())

        assertThat((emissions[1] as DataState.Data).data?.size).isEqualTo(0)
        assertThat(heroesCache.selectAll().size).isEqualTo(0)

        assertThat(emissions.last() as DataState.Loading).isEqualTo(DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Idle))
    }


    private fun checkIsStateLoading(loading: DataState<List<Hero>>) {
        assertThat(loading).isInstanceOf(DataState.Loading::class.java)
        assertThat(loading as DataState.Loading).isEqualTo(DataState.Loading<List<Hero>>(progressBarState = ProgressBarState.Loading))
    }
}