package com.pasukanlangit.id.ui_herolist.ui

import com.codingwithmitch.hero_domain.HeroAttribute
import com.pasukanlangit.id.core.ProgressBarState
import com.pasukanlangit.id.core.Queue
import com.pasukanlangit.id.core.UIComponent
import com.pasukanlangit.id.hero_domain.Hero
import com.pasukanlangit.id.hero_domain.HeroFilter
import com.pasukanlangit.id.hero_domain.UIComponentState

data class HeroListState (
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val heroes: List<Hero> = listOf(),
    val filteredHeroes: List<Hero> = listOf(),
    val searchKeyword: String = "",
    val heroFilter: HeroFilter = HeroFilter.Hero(),
    val heroAttribute: HeroAttribute = HeroAttribute.Unknown,
    val filterDialogState: UIComponentState = UIComponentState.Hide,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf())
)