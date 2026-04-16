package ru.niknekron.recipecomposeapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.niknekron.recipecomposeapp.core.ui.navigation.BottomNavigation
import ru.niknekron.recipecomposeapp.ui.categories.CategoriesScreen
import ru.niknekron.recipecomposeapp.ui.favorites.FavoritesScreen
import ru.niknekron.recipecomposeapp.ui.recipes.RecipesScreen
import ru.niknekron.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesApp() {
    var currentScreen by remember { mutableStateOf(ScreenId.CATEGORIES) }
    var selectedCategoryId by remember { mutableIntStateOf(0) }
    var selectedCategoryTitle by remember { mutableStateOf("Бургеры") }

    RecipeComposeAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        currentScreen = ScreenId.CATEGORIES
                    },
                    onFavoriteClick = {
                        currentScreen = ScreenId.FAVORITES
                    }
                )
            }
        ) { innerPadding ->
            when (currentScreen) {
                ScreenId.CATEGORIES -> {
                    CategoriesScreen(
                        modifier = Modifier.padding(innerPadding),
                        onCategoryClick = { categoryId ->
                            selectedCategoryId = categoryId

                            selectedCategoryTitle = when (categoryId) {
                                0 -> "Бургеры"
                                1 -> "Десерты"
                                2 -> "Пицца"
                                3 -> "Рыба"
                                4 -> "Супы"
                                5 -> "Салаты"
                                else -> "Рецепты"
                            }

                            currentScreen = ScreenId.RECIPES
                        }
                    )
                }

                ScreenId.FAVORITES -> {
                    FavoritesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                ScreenId.RECIPES -> {
                    RecipesScreen(
                        categoryId = selectedCategoryId,
                        categoryTitle = selectedCategoryTitle,
                        onRecipeClick = { recipeId ->
                            // пока заглушка
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipesAppPreview() {
    RecipesApp()
}