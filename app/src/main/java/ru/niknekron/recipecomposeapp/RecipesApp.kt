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
import ru.niknekron.recipecomposeapp.features.categories.ui.CategoriesScreen
import ru.niknekron.recipecomposeapp.features.favorites.ui.FavoritesScreen
import ru.niknekron.recipecomposeapp.features.recipes.ui.RecipesScreen
import ru.niknekron.recipecomposeapp.features.theme.RecipeComposeAppTheme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import ru.niknekron.recipecomposeapp.features.details.ui.RecipeDetailsScreen
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepositoryStub
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.toUiModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ru.niknekron.recipecomposeapp.util.FavoriteDataStoreManager

@Composable
fun RecipesApp(
    deepLinkIntent: Intent? = null,
) {
    val navController = rememberNavController()

    val context = LocalContext.current

    val favoriteDataStoreManager = remember {
        FavoriteDataStoreManager(context)
    }

    val favoriteCount by favoriteDataStoreManager
        .getFavoriteCountFlow()
        .collectAsState(initial = 0)

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
                        onCategoryClick = { categoryId, categoryTitle, imageUrl ->
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
                    FavoritesScreen(
                        recipesRepository = RecipesRepositoryStub,
                        favoriteDataStoreManager = favoriteDataStoreManager,
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
                    val recipeId = backStackEntry.arguments?.getInt(PARAM_RECIPE_ID) ?: 0

                    val recipe = RecipesRepositoryStub
                        .getRecipeById(recipeId)
                        ?.toUiModel()


                    recipe?.let {
                        RecipeDetailsScreen(
                            recipe = it
                        )

                    }
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