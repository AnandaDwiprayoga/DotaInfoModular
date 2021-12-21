package com.pasukanlangit.id.core

sealed class UIComponent{
    data class Dialog(
        val title: String,
        val description: String
    ): UIComponent()

    //this for error logging
    data class None(
        val message: String
    ): UIComponent()
}
