package ru.niknekron.recipecomposeapp.core.ui.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import ru.niknekron.recipecomposeapp.PARAM_RECIPE_ID
import ru.niknekron.recipecomposeapp.PARAM_CATEGORY_ID
import ru.niknekron.recipecomposeapp.PARAM_CATEGORY_TITLE
import ru.niknekron.recipecomposeapp.PARAM_CATEGORY_IMAGE_URL

sealed class Destination(val route: String) {
    data object Categories : Destination("categories")
    data object Favorites: Destination("favorites")
    data object Recipes :
        Destination("recipes/{$PARAM_CATEGORY_ID}/{$PARAM_CATEGORY_TITLE}/{$PARAM_CATEGORY_IMAGE_URL}") {

        fun createRoute(
            categoryId: Int,
            categoryTitle: String,
            categoryImageUrl: String,
        ): String {
            val encodedTitle = URLEncoder.encode(
                categoryTitle,
                StandardCharsets.UTF_8.toString()
            )

            val encodedImageUrl = URLEncoder.encode(
                categoryImageUrl,
                StandardCharsets.UTF_8.toString()
            )

            return "recipes/$categoryId/$encodedTitle/$encodedImageUrl"
        }
    }

    data object RecipeDetails : Destination("recipe/{$PARAM_RECIPE_ID}") {
        fun createRoute(recipeId: Int): String {
            return "recipe/$recipeId"
        }
    }
}