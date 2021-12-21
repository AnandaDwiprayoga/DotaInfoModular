package com.pasukanlangit.id.hero_interactors

import com.codingwithmitch.hero_domain.HeroAttribute
import com.pasukanlangit.id.core.FilterOrder
import com.pasukanlangit.id.hero_domain.Hero
import com.pasukanlangit.id.hero_domain.HeroFilter
import kotlin.math.round

class FilterHeroes {
    operator fun invoke(
        currentHeroes: List<Hero>,
        searchKeyword: String,
        heroFilter: HeroFilter,
        heroAttribute: HeroAttribute
    ): List<Hero> {
        var filteredHeroes = currentHeroes.filter { hero ->
            hero.localizedName.lowercase().contains(searchKeyword.lowercase())
        }.toMutableList() // <- convert to mutable so can access build in function sorting

        when(heroFilter){
            is HeroFilter.Hero -> {
                when(heroFilter.order){
                    is FilterOrder.Ascending -> {
                        filteredHeroes.sortBy { it.localizedName }
                    }
                    is FilterOrder.Descending -> {
                        filteredHeroes.sortByDescending { it.localizedName }
                    }
                }
            }
            is HeroFilter.ProWins -> {
                when(heroFilter.order){
                    is FilterOrder.Ascending -> {
                        filteredHeroes.sortBy {
                            getPercentageProWins(it.proPick.toDouble(), it.proWins.toDouble())
                        }
                    }
                    is FilterOrder.Descending -> {
                        filteredHeroes.sortByDescending {
                            getPercentageProWins(it.proPick.toDouble(), it.proWins.toDouble())
                        }
                    }
                }
            }
        }

        when(heroAttribute){
            HeroAttribute.Agility -> {
                filteredHeroes = filteredHeroes.filter { it.primaryAttribute is HeroAttribute.Agility }.toMutableList()
            }
            HeroAttribute.Intelligence -> {
                filteredHeroes = filteredHeroes.filter { it.primaryAttribute is HeroAttribute.Intelligence }.toMutableList()
            }
            HeroAttribute.Strength -> {
                filteredHeroes = filteredHeroes.filter { it.primaryAttribute is HeroAttribute.Strength }.toMutableList()
            }
            HeroAttribute.Unknown -> {
                //Do nothing
            }
        }

        return filteredHeroes
    }

    private fun getPercentageProWins(proPick: Double, proWins: Double): Int{
        return if(proPick <= 0) {
            0
        }else{
            val winRate: Int = round( proWins / proPick * 100).toInt()
            winRate
        }
    }
}