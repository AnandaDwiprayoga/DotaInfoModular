package com.pasukanlangit.id.mydotainfo.navigation

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument

sealed class Screens(
    val route: String,
    val arguments: List<NamedNavArgument>
){
    object HeroListScreen: Screens(
        route = "heroList",
        arguments = emptyList()
    )

    object HeroDetailScreen: Screens(
        route = "heroDetail",
        arguments = listOf(
            navArgument(com.pasukanlangit.id.constant.Constants.HERO_ID_NAME_ARGUMENTS){
                type = NavType.IntType
            }
        )
    )

}
