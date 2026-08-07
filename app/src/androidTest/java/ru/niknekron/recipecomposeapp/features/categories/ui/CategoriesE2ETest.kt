package ru.niknekron.recipecomposeapp.features.categories.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.niknekron.recipecomposeapp.MainActivity
import ru.niknekron.recipecomposeapp.screens.CategoriesComposeScreen
import ru.niknekron.recipecomposeapp.screens.RecipesComposeScreen

@RunWith(AndroidJUnit4::class)
class CategoriesE2ETest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun categoriesScreenLoadsContent() = run {
        step("Открыть приложение и дождаться загрузки категорий") {
            onComposeScreen<CategoriesComposeScreen>(
                composeTestRule
            ) {
                categoriesGrid {
                    assertIsDisplayed()
                }
            }
        }
    }

    @Test
    fun clickingCategoryOpensRecipesScreen() = run {
        step("Дождаться загрузки категорий") {
            onComposeScreen<CategoriesComposeScreen>(
                composeTestRule
            ) {
                categoriesGrid {
                    assertIsDisplayed()
                }
            }
        }

        step("Нажать на первую категорию") {
            onComposeScreen<CategoriesComposeScreen>(
                composeTestRule
            ) {
                categoryItem {
                    performClick()
                }
            }
        }

        step("Проверить открытие экрана рецептов") {
            onComposeScreen<RecipesComposeScreen>(
                composeTestRule
            ) {
                loadingIndicator {
                    assertIsDisplayed()
                }
            }
        }
    }
}