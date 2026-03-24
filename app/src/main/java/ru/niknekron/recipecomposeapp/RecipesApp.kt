package ru.niknekron.recipecomposeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.niknekron.recipecomposeapp.core.ui.navigation.BottomNavigation
import ru.niknekron.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import ru.niknekron.recipecomposeapp.ui.Favorites.FavoritesScreen
import ru.niknekron.recipecomposeapp.ui.categories.CategoriesScreen
import ru.niknekron.recipecomposeapp.ui.recipes.RecipeScreen


@Composable
fun RecipesApp() {
    var currentScreen by remember { mutableStateOf(ScreenId.CATEGORIES) }

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
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                ScreenId.FAVORITES -> {
                    FavoritesScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                ScreenId.RECIPES -> {
                    RecipeScreen(
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