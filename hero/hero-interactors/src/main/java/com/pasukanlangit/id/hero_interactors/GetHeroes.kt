package com.pasukanlangit.id.hero_interactors

import com.pasukanlangit.id.core.DataState
import com.pasukanlangit.id.core.ProgressBarState
import com.pasukanlangit.id.core.UIComponent
import com.pasukanlangit.id.hero_datasource.cache.HeroCache
import com.pasukanlangit.id.hero_datasource.network.HeroService
import com.pasukanlangit.id.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHeroes(
    private val cache: HeroCache,
    private val api: HeroService
) {
    operator fun invoke(): Flow<DataState<List<Hero>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            val heroes: List<Hero> = try {
                api.getHeroStats()
            }catch (e: Exception) {
                e.printStackTrace()
                emit(
                    DataState.Response(
                        uiComponent = UIComponent.Dialog(
                            title = "Network Data Error",
                            description = e.message ?: "Unknown Error"
                        )
                    )
                )
                listOf()
            }

            /** cache data from network */
            cache.insert(heroes)

            /** emit value from cache*/
            val cachedHeroes = cache.selectAll()
            emit(DataState.Data(cachedHeroes))
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