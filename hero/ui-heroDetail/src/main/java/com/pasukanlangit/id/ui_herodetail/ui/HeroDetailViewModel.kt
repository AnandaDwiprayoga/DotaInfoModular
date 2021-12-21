package com.pasukanlangit.id.ui_herodetail.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasukanlangit.id.constant.Constants.HERO_ID_NAME_ARGUMENTS
import com.pasukanlangit.id.constant.Constants.HERO_LIST_LOGGER
import com.pasukanlangit.id.core.*
import com.pasukanlangit.id.hero_interactors.GetHeroFromCache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val getHeroFromCacheUseCase: GetHeroFromCache,
    @Named(HERO_LIST_LOGGER) private val logger: Logger,
    savedStateHandle: SavedStateHandle //saved state handle can get everything send from argument
): ViewModel(){

    val heroState = mutableStateOf(HeroDetailState())

    init {
        savedStateHandle.get<Int>(HERO_ID_NAME_ARGUMENTS)?.let { id ->
            onTriggerEvent(HeroDetailEvent.GetHeroFromCache(id))
        }
    }
    fun onTriggerEvent(event: HeroDetailEvent){
        when(event){
            is HeroDetailEvent.GetHeroFromCache -> { 
                getHeroFromCache(event.id)
            }
            is HeroDetailEvent.OnRemoveHeadFromQueue -> {
                removeHeadMessage()
            }
        }
    }

    private fun removeHeadMessage() {
        try {
            val queue = heroState.value.errorQueue
            queue.remove()
            heroState.value = heroState.value.copy(errorQueue = Queue(mutableListOf())) //force compose
            heroState.value = heroState.value.copy(errorQueue = queue)
        }catch (e: Exception){
            logger.log("Nothing to remove from Dialog Queue")
        }
    }

    private fun getHeroFromCache(id: Int) {
        getHeroFromCacheUseCase(id).onEach { dataState ->
            when(dataState){
                is DataState.Loading -> {
                    heroState.value = heroState.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Data -> {
                    heroState.value = heroState.value.copy(hero = dataState.data)
                }
                is DataState.Response -> {
                    when(dataState.uiComponent){
                        is UIComponent.Dialog -> {
                            appendToMessageQueue(dataState.uiComponent)
                        }
                        is UIComponent.None -> {
                            logger.log((dataState.uiComponent as UIComponent.None).message)
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun appendToMessageQueue(uiComponent: UIComponent){
        val queue = heroState.value.errorQueue
        queue.add(uiComponent)
        heroState.value = heroState.value.copy(errorQueue = Queue(mutableListOf())) //force compose
        heroState.value = heroState.value.copy(errorQueue = queue)
    }
}