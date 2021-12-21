package com.pasukanlangit.id.hero_domain

sealed class UIComponentState {
    object Show: UIComponentState()
    object Hide: UIComponentState()
}
