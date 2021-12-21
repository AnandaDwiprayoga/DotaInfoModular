package com.pasukanlangit.id.mydotainfo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import coil.ImageLoader
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pasukanlangit.id.mydotainfo.navigation.Screens
import com.pasukanlangit.id.mydotainfo.ui.theme.MyDotaInfoTheme
import com.pasukanlangit.id.ui_herodetail.ui.HeroDetail
import com.pasukanlangit.id.ui_herodetail.ui.HeroDetailViewModel
import com.pasukanlangit.id.ui_herolist.ui.HeroList
import com.pasukanlangit.id.ui_herolist.ui.HeroListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyDotaInfoTheme {
                val navController = rememberAnimatedNavController()
                BoxWithConstraints {
                    val width = constraints.maxWidth/2
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Screens.HeroListScreen.route
                    ){
                        addHeroList(
                            navController = navController,
                            imageLoader = imageLoader,
                            width = width
                        )

                        addHeroDetail(imageLoader = imageLoader, width = width)
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun NavGraphBuilder.addHeroList(
    navController: NavController,
    imageLoader: ImageLoader,
    width: Int
){
    composable(
        route = Screens.HeroListScreen.route,
        exitTransition = {_,_ ->
            slideOutHorizontally(
                targetOffsetX = {-width},
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {_,_ ->
            slideInHorizontally(
                initialOffsetX = {-width},
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        }
    ){
        val viewModel: HeroListViewModel = hiltViewModel()
        HeroList(
            state = viewModel.heroesState.value,
            imageLoader = imageLoader,
            event = viewModel::onTriggerEvent,
            navigateToDetailScreen = { heroId ->
                navController.navigate("${Screens.HeroDetailScreen.route}/$heroId")
            }
        )
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.addHeroDetail(
    imageLoader: ImageLoader,
    width: Int
){
    composable(
        route = "${Screens.HeroDetailScreen.route}/{${com.pasukanlangit.id.constant.Constants.HERO_ID_NAME_ARGUMENTS}}",
        arguments = Screens.HeroDetailScreen.arguments,
        enterTransition = {_,_ ->
            slideInHorizontally(
                initialOffsetX = {width},
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {_,_ ->
            slideOutHorizontally(
                targetOffsetX = {width},
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        }
    ){
        val viewModel: HeroDetailViewModel = hiltViewModel()
        HeroDetail(
            state = viewModel.heroState.value,
            imageLoader = imageLoader,
            events = viewModel::onTriggerEvent
        )
    }
}