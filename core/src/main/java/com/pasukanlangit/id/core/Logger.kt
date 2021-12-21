package com.pasukanlangit.id.core

class Logger(
    private val tag: String,
    private val isDebug: Boolean = true
) {
    fun log(msg: String){
        if(!isDebug){
            //it's run in production mode 
            
        }else{
            printLogD(tag, msg)
        }
    }

    //factory class to get this logger instance
    companion object Factory {
        fun buildDebug(tag: String): Logger =
            Logger(tag = tag, isDebug = true)

        fun buildRelease(tag: String): Logger =
            Logger(tag = tag, isDebug = false)
    }

    private fun printLogD(tag: String, msg: String) {
        println("$tag: $msg")
    }
}