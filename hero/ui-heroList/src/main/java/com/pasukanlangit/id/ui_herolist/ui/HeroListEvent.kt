package com.pasukanlangit.id.ui_herolist.ui

import com.codingwithmitch.hero_domain.HeroAttribute
import com.pasukanlangit.id.hero_domain.HeroFilter
import com.pasukanlangit.id.hero_domain.UIComponentState

sealed class HeroListEvent {
    object GetHeroes: HeroListEvent()

    object FilterHeroes: HeroListEvent()

    data class UpdateSearchKeyword(
        val heroName: String
    ): HeroListEvent()

    data class UpdateHeroFilter(
        val heroFilter: HeroFilter
    ): HeroListEvent()

    data class UpdateFilterDialogState(
        val uiComponentState: UIComponentState
    ): HeroListEvent()

    data class UpdateHeroAttribute(
        val heroAttribute: HeroAttribute
    ): HeroListEvent()

    object OnRemoveHeadFromQueue: HeroListEvent()
}