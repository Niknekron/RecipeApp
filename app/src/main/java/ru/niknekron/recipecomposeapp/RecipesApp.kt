package ru.niknekron.recipecomposeapp

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import ru.niknekron.recipecomposeapp.core.ui.navigation.BottomNavigation
import ru.niknekron.recipecomposeapp.core.ui.navigation.Destination
import ru.niknekron.recipecomposeapp.features.categories.ui.CategoriesScreen
import ru.niknekron.recipecomposeapp.features.details.ui.RecipeDetailsScreen
import ru.niknekron.recipecomposeapp.features.favorites.presentation.model.FavoriteViewModel
import ru.niknekron.recipecomposeapp.features.favorites.ui.FavoritesScreen
import ru.niknekron.recipecomposeapp.features.recipes.ui.RecipesScreen
import ru.niknekron.recipecomposeapp.features.theme.RecipeComposeAppTheme
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.niknekron.recipecomposeapp.features.details.presentation.model.RecipeDetailsViewModel
import ru.niknekron.recipecomposeapp.features.recipes.presentation.RecipesViewModel

@Composable
fun RecipesApp(
    deepLinkIntent: Intent? = null,
){
    val navController = rememberNavController()
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()

    val favoriteCount by favoriteViewModel
        .favoriteCount
        .collectAsState()


    LaunchedEffect(deepLinkIntent) {
        deepLinkIntent?.data?.let { uri ->
            val recipeId = when (uri.scheme) {
                DEEP_LINK_SCHEME -> {
                    if (uri.host == "recipe") {
                        uri.pathSegments.firstOrNull()?.toIntOrNull()
                    } else {
                        null
                    }
                }

                "https", "http" -> {
                    if (uri.pathSegments.firstOrNull() == "recipe") {
                        uri.pathSegments.getOrNull(1)?.toIntOrNull()
                    } else {
                        null
                    }
                }

                else -> null
            }

            if (recipeId != null) {
                delay(100)

                navController.navigate(
                    Destination.RecipeDetails.createRoute(recipeId)
                )
            }
        }
    }

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
                    },
                    favoriteCount = favoriteCount
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
                        onCategoryClick = { categoryId, categoryTitle, categoryImageUrl ->
                            navController.navigate(
                                Destination.Recipes.createRoute(
                                    categoryId = categoryId,
                                    categoryTitle = categoryTitle,
                                    categoryImageUrl = categoryImageUrl
                                )
                            )
                        }
                    )
                }

                composable(
                    route = Destination.Favorites.route
                ) { backStackEntry ->

                    val favoriteViewModel: FavoriteViewModel =
                        hiltViewModel(backStackEntry)

                    FavoritesScreen(
                        viewModel = favoriteViewModel,
                        onRecipeClick = { recipeId ->
                            navController.navigate(
                                Destination.RecipeDetails.createRoute(recipeId)
                            )
                        }
                    )
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(
                        navArgument(PARAM_CATEGORY_ID) {
                            type = NavType.IntType
                        },
                        navArgument(PARAM_CATEGORY_TITLE) {
                            type = NavType.StringType
                        },
                        navArgument(PARAM_CATEGORY_IMAGE_URL) {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    val recipesViewModel: RecipesViewModel =
                        hiltViewModel(backStackEntry)

                    RecipesScreen(
                        viewModel = recipesViewModel,
                        onRecipeClick = { recipeId ->
                            navController.navigate(
                                Destination.RecipeDetails.createRoute(recipeId)
                            )
                        }
                    )
                }

                composable(
                    route = Destination.RecipeDetails.route,
                    arguments = listOf(
                        navArgument(PARAM_RECIPE_ID) {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val recipeDetailsViewModel: RecipeDetailsViewModel =
                        hiltViewModel(backStackEntry)

                    RecipeDetailsScreen(
                        viewModel = recipeDetailsViewModel
                    )
                }
            }
        }
    }
}