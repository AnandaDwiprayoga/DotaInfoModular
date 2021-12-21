package com.pasukanlangit.id.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pasukanlangit.id.core.ProgressBarState
import com.pasukanlangit.id.core.Queue
import com.pasukanlangit.id.core.UIComponent

@Composable
fun DefaultScreenUI(
    queue: Queue<UIComponent>,
    progressBarState: ProgressBarState = ProgressBarState.Idle,
    onRemoveHeadFromQueue: () -> Unit,
    content: @Composable () -> Unit
){
    val scaffoldState = rememberScaffoldState() //<- preparing for if sometimes need snackbar, bottommenu, drawermenu wich need scaffold state
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            content()
            if(!queue.isEmpty()){
                queue.peek()?.let { uiComponent ->
                    if(uiComponent is UIComponent.Dialog){
                        GenericDialog(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            title = uiComponent.title,
                            description = uiComponent.description,
                            onRemoveHeadFromQueue = onRemoveHeadFromQueue
                        )
                    }
                }
            }
            if(progressBarState is ProgressBarState.Loading){
                CircularIndeterminateProgressBar()
            }
        }
    }
}