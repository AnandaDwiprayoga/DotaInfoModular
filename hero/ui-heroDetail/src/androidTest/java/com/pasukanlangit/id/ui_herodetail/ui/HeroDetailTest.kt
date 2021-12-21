package com.pasukanlangit.id.ui_herodetail.ui

import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.pasukanlangit.id.hero_datasource_test.network.data.HeroDataValid
import com.pasukanlangit.id.hero_datasource_test.network.serializeHeroData
import com.pasukanlangit.id.ui_herolist.coil.FakeImageLoader
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random


class HeroDetailTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader = FakeImageLoader.build(context)
    private val heroData = serializeHeroData(HeroDataValid.data)

    @Test
    fun isHeroDotaShown(){
        val hero = heroData[Random.nextInt(0, heroData.size - 1)]
        composeTestRule.setContent {
            val state = remember {
                HeroDetailState(
                    hero = hero
                )
            }
            HeroDetail(
                state = state,
                imageLoader = imageLoader,
                events = {})
        }

        composeTestRule.onNodeWithText(hero.localizedName).assertIsDisplayed()
        composeTestRule.onNodeWithText(hero.primaryAttribute.uiValue).assertIsDisplayed()
        composeTestRule.onNodeWithText(hero.attackType.uiValue).assertIsDisplayed()

        val proWinPercentage = (hero.proWins.toDouble() / hero.proPick.toDouble() * 100).toInt()
        composeTestRule.onNodeWithText("$proWinPercentage %")

        val turboWinPercentage = (hero.turboWins.toDouble() / hero.turboPicks.toDouble() * 100).toInt()
        composeTestRule.onNodeWithText("$turboWinPercentage %")
    }
}