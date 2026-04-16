package ru.niknekron.recipecomposeapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.niknekron.recipecomposeapp.core.ui.navigation.BottomNavigation
import ru.niknekron.recipecomposeapp.core.ui.navigation.Destination
import ru.niknekron.recipecomposeapp.ui.categories.CategoriesScreen
import ru.niknekron.recipecomposeapp.ui.favorites.FavoritesScreen
import ru.niknekron.recipecomposeapp.ui.recipes.RecipesScreen
import ru.niknekron.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun RecipesApp() {
    val navController = rememberNavController()

    RecipeComposeAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        navController.navigate(Destination.Categories.route)
                    },
                    onFavoriteClick = {
                        navController.navigate(Destination.Favorites.route)
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Destination.Categories.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Destination.Categories.route) {
                    CategoriesScreen(
                        onCategoryClick = { categoryId ->
                            val categoryTitle = when (categoryId) {
                                0 -> "Бургеры"
                                1 -> "Десерты"
                                2 -> "Пицца"
                                3 -> "Рыба"
                                4 -> "Супы"
                                5 -> "Салаты"
                                else -> "Рецепты"
                            }

                            navController.navigate(
                                Destination.Recipes.createRoute(
                                    categoryId = categoryId,
                                    categoryTitle = categoryTitle
                                )
                            )
                        }
                    )
                }

                composable(route = Destination.Favorites.route) {
                    FavoritesScreen()
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(
                        navArgument("categoryId") {
                            type = NavType.IntType
                        },
                        navArgument("categoryTitle") {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    val categoryId =
                        backStackEntry.arguments?.getInt("categoryId") ?: 0

                    val encodedTitle =
                        backStackEntry.arguments?.getString("categoryTitle").orEmpty()

                    val categoryTitle = URLDecoder.decode(
                        encodedTitle,
                        StandardCharsets.UTF_8.toString()
                    )

                    RecipesScreen(
                        categoryId = categoryId,
                        categoryTitle = categoryTitle,
                        onRecipeClick = { recipeId ->
                        }
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