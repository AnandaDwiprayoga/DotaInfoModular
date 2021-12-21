package com.pasukanlangit.id.mydotainfo.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.pasukanlangit.id.hero_datasource.cache.HeroCache
import com.pasukanlangit.id.hero_datasource.network.HeroService
import com.pasukanlangit.id.hero_datasource_test.cache.HeroCacheFake
import com.pasukanlangit.id.hero_datasource_test.cache.HeroDatabaseFake
import com.pasukanlangit.id.hero_datasource_test.network.HeroServiceFake
import com.pasukanlangit.id.hero_datasource_test.network.HeroServiceResponseType
import com.pasukanlangit.id.hero_interactors.FilterHeroes
import com.pasukanlangit.id.hero_interactors.GetHeroFromCache
import com.pasukanlangit.id.hero_interactors.GetHeroes
import com.pasukanlangit.id.hero_interactors.HeroInteractors
import com.pasukanlangit.id.mydotainfo.di.HeroInteractorsModule
import com.pasukanlangit.id.mydotainfo.navigation.Screens
import com.pasukanlangit.id.mydotainfo.ui.theme.MyDotaInfoTheme
import com.pasukanlangit.id.ui_herodetail.ui.HeroDetail
import com.pasukanlangit.id.ui_herodetail.ui.HeroDetailViewModel
import com.pasukanlangit.id.ui_herolist.coil.FakeImageLoader
import com.pasukanlangit.id.ui_herolist.ui.HeroList
import com.pasukanlangit.id.ui_herolist.ui.HeroListViewModel
import com.pasukanlangit.id.ui_herolist.ui.test.TAG_HERO_NAME
import com.pasukanlangit.id.ui_herolist.ui.test.TAG_HERO_SEARCH_BAR
import dagger.Provides
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@UninstallModules(HeroInteractorsModule::class)
class HeroListE2EModule {
    @Provides
    @Singleton
    fun provideHeroCache(): HeroCache =
        HeroCacheFake(HeroDatabaseFake())

    @Provides
    @Singleton
    fun provideHeroService(): HeroService =
        HeroServiceFake.build(
            type = HeroServiceResponseType.ValidData
        )

    @Provides
    @Singleton
    fun provideHeroInteractors(
        cache: HeroCache,
        service: HeroService
    ): HeroInteractors =
        HeroInteractors(
            getHeroes = GetHeroes(
                cache = cache,
                api = service
            ),
            getHeroFromCache = GetHeroFromCache(
                cache = cache
            ),
            filterHeroes = FilterHeroes()
        )

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader = FakeImageLoader.build(context = context)

    @Before
    fun setUp(){
        composeTestRule.setContent {
            MyDotaInfoTheme {
                val navController = rememberNavController()
                BoxWithConstraints {
                    val width = constraints.maxWidth/2
                    NavHost(
                        navController = navController,
                        startDestination = Screens.HeroListScreen.route
                    ){
                        composable(
                            route = Screens.HeroListScreen.route,
                        ){
                            val viewModel: HeroListViewModel = hiltViewModel()
                            HeroList(
                                state = viewModel.heroesState.value,
                                event = viewModel::onTriggerEvent,
                                navigateToDetailScreen = { heroId ->
                                    navController.navigate("${Screens.HeroDetailScreen.route}/$heroId")
                                },
                                imageLoader = imageLoader,
                            )
                        }
                        composable(
                            route = "${Screens.HeroDetailScreen.route}/{${com.pasukanlangit.id.constant.Constants.HERO_ID_NAME_ARGUMENTS}}",
                            arguments = Screens.HeroDetailScreen.arguments,
                        ){
                            val viewModel: HeroDetailViewModel = hiltViewModel()
                            HeroDetail(
                                state = viewModel.heroState.value,
                                events = viewModel::onTriggerEvent,
                                imageLoader = imageLoader
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testSearchHeroByName(){
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("TAG") // For learning the ui tree system

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Anti-Mage")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Anti-Mage",
        )
        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextClearance()

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Storm Spirit")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Storm Spirit",
        )
        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextClearance()

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Mirana")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Mirana",
        )
    }

}