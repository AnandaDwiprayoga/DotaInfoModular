package com.pasukanlangit.id.core

sealed class DataState<T> {
    //this called error in common, but it can return positive return so rename to Response
    data class Response<T>(
        val uiComponent: UIComponent
    ): DataState<T>()

    data class Data<T>(
        val data: T? = null,
    ): DataState<T>()

    data class Loading<T>(
        val progressBarState: ProgressBarState = ProgressBarState.Idle
    ): DataState<T>()
}
