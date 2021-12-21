package com.pasukanlangit.id.hero_interactors

import com.pasukanlangit.id.core.DataState
import com.pasukanlangit.id.core.ProgressBarState
import com.pasukanlangit.id.core.UIComponent
import com.pasukanlangit.id.hero_datasource.cache.HeroCache
import com.pasukanlangit.id.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHeroFromCache(
    private val cache: HeroCache
) {
    operator fun invoke(
        id: Int
    ): Flow<DataState<Hero>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            val cachedHero = cache.getHero(id) ?: throw Exception("Hero with id $id not exist in the cache")

            emit(DataState.Data(cachedHero))
        }catch (e: Exception){
            e.printStackTrace()
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        description = e.message ?: "Unknown Error"
                    )
                )
            )
        }finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}