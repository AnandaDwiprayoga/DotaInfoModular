package com.pasukanlangit.id.ui_herolist.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.pasukanlangit.id.hero_datasource_test.network.data.HeroDataValid
import com.pasukanlangit.id.hero_datasource_test.network.serializeHeroData
import com.pasukanlangit.id.ui_herolist.coil.FakeImageLoader
import org.junit.Rule
import org.junit.Test

class HeroListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader = FakeImageLoader.build(context)
    private val heroData = serializeHeroData(HeroDataValid.data)


    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @Test
    fun areHeroesShown(){
        composeTestRule.setContent {
            val state = remember {
                HeroListState(
                    heroes = heroData,
                    filteredHeroes = heroData
                )
            }
            HeroList(
                state = state,
                imageLoader = imageLoader,
                event = {},
                navigateToDetailScreen = {}
            )
        }
        //check item is displayed
        composeTestRule.onNodeWithText("Anti-Mage").assertIsDisplayed()
        composeTestRule.onNodeWithText("Axe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bane").assertIsDisplayed()
    }
}