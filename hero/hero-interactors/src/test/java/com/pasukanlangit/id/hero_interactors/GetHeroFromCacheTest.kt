package com.pasukanlangit.id.hero_interactors

import com.google.common.truth.Truth.assertThat
import com.pasukanlangit.id.core.DataState
import com.pasukanlangit.id.core.ProgressBarState
import com.pasukanlangit.id.core.UIComponent
import com.pasukanlangit.id.hero_datasource_test.cache.HeroCacheFake
import com.pasukanlangit.id.hero_datasource_test.cache.HeroDatabaseFake
import com.pasukanlangit.id.hero_datasource_test.network.data.HeroDataValid
import com.pasukanlangit.id.hero_datasource_test.network.serializeHeroData
import com.pasukanlangit.id.hero_domain.Hero
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class GetHeroFromCacheTest{
    private lateinit var heroRandomSelected: Hero
    private lateinit var heroCacheFake: HeroCacheFake
    private lateinit var getHeroFromCache: GetHeroFromCache

    @Before
    fun setUp() = runBlocking {
        heroCacheFake = HeroCacheFake(db = HeroDatabaseFake())
        getHeroFromCache = GetHeroFromCache(
            cache = heroCacheFake
        )

        val heroData = serializeHeroData(HeroDataValid.data)
        heroCacheFake.insert(heroData)

        heroRandomSelected = heroData[Random.nextInt(0, heroData.size - 1)]
    }
    
    @Test
    fun `get hero from cache should return success`() = runBlocking {
        val emission = getHeroFromCache(heroRandomSelected.id).toList()

        assertThat(emission[0]).isInstanceOf(DataState.Loading::class.java)

        assertThat(emission[1]).isInstanceOf(DataState.Data::class.java)
        assertThat((emission[1] as DataState.Data).data?.id).isEqualTo(heroRandomSelected.id)

        assertThat(emission[2]).isInstanceOf(DataState.Loading::class.java)
        assertThat((emission[2] as DataState.Loading).progressBarState).isEqualTo(ProgressBarState.Idle)
    }

    @Test
    fun `get hero from cache should return error when item not found`() = runBlocking {
        //remove item first
        heroCacheFake.removeHero(heroRandomSelected.id)

        val emissions = getHeroFromCache(heroRandomSelected.id).toList()
        assertThat(emissions[0]).isInstanceOf(DataState.Loading::class.java)

        assertThat(emissions[1]).isInstanceOf(DataState.Response::class.java)
        assertThat(((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).title).isEqualTo("Error")
        assertThat(((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).description).isEqualTo("Hero with id ${heroRandomSelected.id} not exist in the cache")

        assertThat((emissions[2] as DataState.Loading).progressBarState).isEqualTo(ProgressBarState.Idle)
    }
}