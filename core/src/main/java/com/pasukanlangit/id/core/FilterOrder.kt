package com.pasukanlangit.id.core

sealed class FilterOrder {
    object Ascending: FilterOrder()
    object Descending: FilterOrder()
}
