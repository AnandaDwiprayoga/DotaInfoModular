package com.pasukanlangit.id.hero_domain

import com.codingwithmitch.hero_domain.HeroAttribute
import com.pasukanlangit.id.core.FilterOrder

sealed class HeroFilter(val uiValue: String){
    data class Hero(
        val order: FilterOrder = FilterOrder.Descending
    ): HeroFilter("Hero")

    data class ProWins(
        val order: FilterOrder = FilterOrder.Descending
    ): HeroFilter("Pro win-rate")
}
