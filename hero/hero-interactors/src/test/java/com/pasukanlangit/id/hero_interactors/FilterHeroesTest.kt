package com.pasukanlangit.id.hero_interactors

import com.codingwithmitch.hero_domain.HeroAttribute
import com.google.common.truth.Truth.assertThat
import com.pasukanlangit.id.core.FilterOrder
import com.pasukanlangit.id.hero_datasource_test.network.data.HeroDataValid
import com.pasukanlangit.id.hero_datasource_test.network.serializeHeroData
import com.pasukanlangit.id.hero_domain.HeroFilter
import org.junit.Before
import org.junit.Test
import java.lang.Math.round
import kotlin.math.roundToInt

/**
 * 1. Success (Search for specific hero by 'localizedName' param)
 * 2. Success (Order by 'localizedName' param DESC)
 * 3. Success (Order by 'localizedName' param ASC)
 * 4. Success (Order by 'proWins' % ASC)
 * 5. Success (Order by 'proWins' % DESC)
 * 6. Success (Filter by 'HeroAttribute' "Strength")
 * 7. Success (Filter by 'HeroAttribute' "Agility")
 * 8. Success (Filter by 'HeroAttribute' "Intelligence")
 */
class FilterHeroesTest {
    private lateinit var filterHeroes: FilterHeroes

    private val heroList = serializeHeroData(HeroDataValid.data)

    @Before
    fun setUp(){
        filterHeroes = FilterHeroes()
    }

    @Test
    fun searchHeroByName(){
        val emissions = filterHeroes(
            currentHeroes = heroList,
            searchKeyword = "Slark",
            heroFilter = HeroFilter.Hero(),
            heroAttribute = HeroAttribute.Unknown
        )

        assertThat(emissions[0].localizedName == "Slark")
    }

    @Test
    fun orderByNameDesc(){
        val emissions = filterHeroes(
            currentHeroes = heroList,
            searchKeyword = "",
            heroFilter = HeroFilter.Hero(order = FilterOrder.Descending),
            heroAttribute = HeroAttribute.Unknown
        )

        for(index in 1 until emissions.size - 1) {
            assertThat(emissions[index-1].localizedName.toCharArray()[0]).isAtLeast(emissions[index].localizedName.toCharArray()[0])
        }
    }

    @Test
    fun orderByNameAsc(){
        val emissions = filterHeroes(
            currentHeroes = heroList,
            searchKeyword = "",
            heroFilter = HeroFilter.Hero(order = FilterOrder.Ascending),
            heroAttribute = HeroAttribute.Unknown
        )

        for(index in 1 until emissions.size - 1) {
            assertThat(emissions[index-1].localizedName.toCharArray()[0]).isAtMost(emissions[index].localizedName.toCharArray()[0])
        }
    }

    @Test
    fun orderByProWinsAsc(){
        val emissions = filterHeroes(
            currentHeroes = heroList,
            searchKeyword = "",
            heroFilter = HeroFilter.ProWins(order = FilterOrder.Ascending),
            heroAttribute = HeroAttribute.Unknown
        )

        for(index in 1 until emissions.size - 1) {
            val prevWinPercentage = (emissions[index - 1].proWins.toDouble() / emissions[index - 1].proPick.toDouble() * 100).roundToInt()
            val currWinPercentage = (emissions[index].proWins.toDouble() / emissions[index].proPick.toDouble() * 100).roundToInt()

            assertThat(prevWinPercentage).isAtMost(currWinPercentage)
        }
    }

    @Test
    fun orderByProWinsDesc(){
        val emissions = filterHeroes(
            currentHeroes = heroList,
            searchKeyword = "",
            heroFilter = HeroFilter.ProWins(order = FilterOrder.Descending),
            heroAttribute = HeroAttribute.Unknown
        )

        for(index in 1 until emissions.size - 1) {
            val prevWinPercentage = (emissions[index - 1].proWins.toDouble() / emissions[index - 1].proPick.toDouble() * 100).roundToInt()
            val currWinPercentage = (emissions[index].proWins.toDouble() / emissions[index].proPick.toDouble() * 100).roundToInt()

            assertThat(prevWinPercentage).isAtLeast(currWinPercentage)
        }
    }

    @Test
    fun filterHeroesByStrength(){
        val emissions = filterHeroes(
            currentHeroes = heroList,
            searchKeyword = "",
            heroFilter = HeroFilter.Hero(),
            heroAttribute = HeroAttribute.Strength
        )

        for(hero in emissions){
            assertThat(hero.primaryAttribute is HeroAttribute.Strength)
        }
    }


    @Test
    fun filterHeroesByAgility(){
        val emissions = filterHeroes(
            currentHeroes = heroList,
            searchKeyword = "",
            heroFilter = HeroFilter.Hero(),
            heroAttribute = HeroAttribute.Agility
        )

        for(hero in emissions){
            assertThat(hero.primaryAttribute is HeroAttribute.Agility)
        }
    }

    @Test
    fun filterHeroesByIntelligence(){
        val emissions = filterHeroes(
            currentHeroes = heroList,
            searchKeyword = "",
            heroFilter = HeroFilter.Hero(),
            heroAttribute = HeroAttribute.Intelligence
        )

        for(hero in emissions){
            assertThat(hero.primaryAttribute is HeroAttribute.Intelligence)
        }
    }
}