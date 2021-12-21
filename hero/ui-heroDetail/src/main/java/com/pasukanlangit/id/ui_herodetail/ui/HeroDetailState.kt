package com.pasukanlangit.id.ui_herodetail.ui

import com.pasukanlangit.id.core.ProgressBarState
import com.pasukanlangit.id.core.Queue
import com.pasukanlangit.id.core.UIComponent
import com.pasukanlangit.id.hero_domain.Hero

data class HeroDetailState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val hero: Hero? = null,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf())
)