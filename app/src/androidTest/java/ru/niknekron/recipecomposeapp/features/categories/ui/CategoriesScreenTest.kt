package ru.niknekron.recipecomposeapp.features.categories.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.CategoryUiModel
import ru.niknekron.recipecomposeapp.features.theme.RecipeComposeAppTheme

@RunWith(AndroidJUnit4::class)
class CategoriesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysCategories() {
        //Arrange
        val category = CategoryUiModel(
            id = 1,
            title = "Завтраки",
            description = "Утренние блюда",
            imageUrl = "breakfast.jpg"
        )

        val uiState = CategoriesUiState(
            categories = listOf(category),
            isLoading = false,
            error = null
        )

        //Act
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                CategoriesContent(
                    uiState = uiState,
                    onCategoryClick = { _, _, _ -> }
                )
            }
        }
        //Assert
        composeTestRule
            .onNodeWithText(category.title.uppercase())
            .assertIsDisplayed()
    }

    @Test
    fun clickingCategoryNavigatesToRecipes() {
        //Arrange
        val category = CategoryUiModel(
            id = 7,
            title = "Десерты",
            description = "Сладкие блюда",
            imageUrl = "dessert.jpg",
        )

        val uiState = CategoriesUiState(
            categories = listOf(category),
            isLoading = false,
            error = null
        )

        var clickedId: Int? = null

        composeTestRule.setContent {
            RecipeComposeAppTheme {
                CategoriesContent(
                    uiState = uiState,
                    onCategoryClick = { id, _, _ ->
                        clickedId = id
                    }
                )
            }
        }

        //Act
        composeTestRule
            .onNodeWithText(category.title.uppercase())
            .performClick()

        //Assert
        assertEquals(category.id, clickedId)
    }

    @Test
    fun showLoadingState() {
        //Arrange
        val uiState = CategoriesUiState(
            categories = emptyList(),
            isLoading = true,
            error = null
        )

        //Act
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                CategoriesContent(
                    uiState = uiState,
                    onCategoryClick = { _, _, _ -> }
                )
            }
        }

        //Assert
        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }
}