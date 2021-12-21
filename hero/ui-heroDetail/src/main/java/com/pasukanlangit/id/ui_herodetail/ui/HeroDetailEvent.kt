package com.pasukanlangit.id.ui_herodetail.ui

sealed class HeroDetailEvent {
    data class GetHeroFromCache(
        val id: Int
    ): HeroDetailEvent()

    object OnRemoveHeadFromQueue: HeroDetailEvent()
}
