package com.pasukanlangit.id.ui_herolist.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import coil.ImageLoader
import com.pasukanlangit.id.components.DefaultScreenUI
import com.pasukanlangit.id.hero_domain.UIComponentState
import com.pasukanlangit.id.ui_herolist.components.HeroItem
import com.pasukanlangit.id.ui_herolist.components.HeroListFilter
import com.pasukanlangit.id.ui_herolist.components.HeroListToolbar

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun HeroList(
    state: HeroListState,
    imageLoader: ImageLoader,
    event: (HeroListEvent) -> Unit,
    navigateToDetailScreen: (Int) -> Unit
) {
    DefaultScreenUI(
        queue = state.errorQueue,
        progressBarState = state.progressBarState,
        onRemoveHeadFromQueue = {
            event(HeroListEvent.OnRemoveHeadFromQueue)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column {
                HeroListToolbar(
                    inputSearch = state.searchKeyword,
                    onInputSearch = { searchValue ->
                        event(HeroListEvent.UpdateSearchKeyword(searchValue))
                    },
                    onExecuteSearch = {
                        event(HeroListEvent.FilterHeroes)
                    },
                    onShowFilterDialog = {
                        event(HeroListEvent.UpdateFilterDialogState(UIComponentState.Show))
                    }
                )
                if(state.filterDialogState is UIComponentState.Show){
                    HeroListFilter(
                        onCloseDialog = {
                            event(HeroListEvent.UpdateFilterDialogState(UIComponentState.Hide))
                        },
                        onUpdateHeroFilter = {heroFilter ->
                            event(HeroListEvent.UpdateHeroFilter(heroFilter))
                        },
                        onUpdateAttributeFilter = { heroAttribute ->
                            event(HeroListEvent.UpdateHeroAttribute(heroAttribute = heroAttribute))
                        },
                        heroFilter = state.heroFilter,
                        attributeDefault = state.heroAttribute
                    )
                }

                LazyColumn {
                    items(state.filteredHeroes){ hero ->
                        HeroItem(
                            hero = hero,
                            onSelectHero = { heroId ->
                                navigateToDetailScreen(heroId)
                            },
                            imageLoader = imageLoader
                        )
                    }
                }

            }
        }
    }
}