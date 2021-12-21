package com.pasukanlangit.id.ui_herolist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithmitch.hero_domain.HeroAttribute
import com.pasukanlangit.id.constant.Constants.HERO_LIST_LOGGER
import com.pasukanlangit.id.core.DataState
import com.pasukanlangit.id.core.Logger
import com.pasukanlangit.id.core.Queue
import com.pasukanlangit.id.core.UIComponent
import com.pasukanlangit.id.hero_domain.HeroFilter
import com.pasukanlangit.id.hero_interactors.FilterHeroes
import com.pasukanlangit.id.hero_interactors.GetHeroes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class HeroListViewModel @Inject constructor(
    private val getHeroesUseCase: GetHeroes,
    private val filterHeroesUseCase: FilterHeroes,
    @Named(HERO_LIST_LOGGER) private val logger: Logger
): ViewModel(){
    val heroesState: MutableState<HeroListState> = mutableStateOf(HeroListState())

    init {
        onTriggerEvent(HeroListEvent.GetHeroes)
    }

    fun onTriggerEvent(event: HeroListEvent){
        when(event){
            is HeroListEvent.GetHeroes -> {
                getHeroes()
            }
            is HeroListEvent.FilterHeroes -> {
                filterHeroes()
            }
            is HeroListEvent.UpdateSearchKeyword -> {
                updateSearchKeyword(event.heroName)
            }
            is HeroListEvent.UpdateHeroFilter -> {
                updateHeroFilter(event.heroFilter)
            }
            is HeroListEvent.UpdateHeroAttribute -> {
                updateAttributeFilter(event.heroAttribute)
            }
            is HeroListEvent.UpdateFilterDialogState -> {
                heroesState.value = heroesState.value.copy(filterDialogState = event.uiComponentState)
            }
            is HeroListEvent.OnRemoveHeadFromQueue -> {
                removeHeadMessage()
            }
        }
    }

    private fun removeHeadMessage() {
        try {
            val queue = heroesState.value.errorQueue
            queue.remove()
            heroesState.value = heroesState.value.copy(errorQueue = Queue(mutableListOf()))
            heroesState.value = heroesState.value.copy(errorQueue = queue)
        }catch (e: Exception){
            logger.log("Nothing to remove from Dialog Queue")
        }
    }

    private fun updateAttributeFilter(heroAttribute: HeroAttribute) {
        heroesState.value = heroesState.value.copy(heroAttribute = heroAttribute)
        filterHeroes()
    }

    private fun updateHeroFilter(heroFilter: HeroFilter) {
        heroesState.value = heroesState.value.copy(heroFilter = heroFilter)
        filterHeroes()
    }

    private fun updateSearchKeyword(heroName: String) {
        heroesState.value = heroesState.value.copy(searchKeyword = heroName)
    }

    private fun filterHeroes() {
        val filteredHeroes = filterHeroesUseCase(
            currentHeroes = heroesState.value.heroes,
            searchKeyword = heroesState.value.searchKeyword,
            heroFilter = heroesState.value.heroFilter,
            heroAttribute = heroesState.value.heroAttribute
        )
        heroesState.value = heroesState.value.copy(filteredHeroes = filteredHeroes)
    }


    private fun getHeroes(){
        getHeroesUseCase().onEach { dataState ->
            when(dataState){
                is DataState.Response -> {
                    when(dataState.uiComponent) {
                        is UIComponent.Dialog -> {
                            appendToMessageQueue(dataState.uiComponent)
                        }
                        is UIComponent.None -> {
                            logger.log((dataState.uiComponent as UIComponent.None).message)
                        }
                    }
                }
                is DataState.Data -> {
                    heroesState.value = heroesState.value.copy(heroes = dataState.data ?: listOf())
                    filterHeroes()
                }
                is DataState.Loading -> {
                    heroesState.value = heroesState.value.copy(progressBarState = dataState.progressBarState)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun appendToMessageQueue(uiComponent: UIComponent){
        val queue = heroesState.value.errorQueue
        queue.add(uiComponent)
        heroesState.value = heroesState.value.copy(errorQueue = Queue(mutableListOf())) //force compose
        heroesState.value = heroesState.value.copy(errorQueue = queue)
    }
}