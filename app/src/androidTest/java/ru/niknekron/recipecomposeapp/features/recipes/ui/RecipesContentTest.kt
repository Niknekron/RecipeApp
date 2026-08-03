package ru.niknekron.recipecomposeapp.features.recipes.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiState
import ru.niknekron.recipecomposeapp.features.theme.RecipeComposeAppTheme

class RecipesContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsLoadingState() {
        // Arrange
        val uiState = RecipeUiState(
            isLoading = true
        )

        // Act
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                RecipesContent(
                    uiState = uiState,
                    onRecipeClick = {}
                )
            }
        }

        // Assert
        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    @Test
    fun showsErrorState() {
        // Arrange
        val errorMessage = "Network error"

        val uiState = RecipeUiState(
            isLoading = false,
            error = errorMessage
        )

        // Act
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                RecipesContent(
                    uiState = uiState,
                    onRecipeClick = {}
                )
            }
        }

        // Assert
        composeTestRule
            .onNodeWithTag("error_message")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun showsEmptyState() {
        // Arrange
        val uiState = RecipeUiState()

        // Act
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                RecipesContent(
                    uiState = uiState,
                    onRecipeClick = {}
                )
            }
        }

        // Assert
        composeTestRule
            .onNodeWithTag("empty_state")
            .assertIsDisplayed()
    }

    @Test
    fun displaysRecipeList() {
        // Arrange
        val recipe = RecipeUiModel(
            id = 1,
            title = "Паста Карбонара",
            imageUrl = "pasta.jpg",
            ingredients = emptyList(),
            method = emptyList(),
            isFavorite = false
        )

        val uiState = RecipeUiState(
            recipes = listOf(recipe),
            isLoading = false,
            error = null
        )

        // Act
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                RecipesContent(
                    uiState = uiState,
                    onRecipeClick = {}
                )
            }
        }

        // Assert
        composeTestRule
            .onNodeWithText(recipe.title.uppercase())
            .assertIsDisplayed()
    }
}
