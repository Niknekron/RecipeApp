package ru.niknekron.recipecomposeapp.core.ui.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Destination(val route: String) {
    data object Categories : Destination("categories")
    data object Favorites: Destination("favorites")
    data object Recipes : Destination("recipes/{categoryId}/{categoryTitle}") {
        fun createRoute(categoryId:Int , categoryTitle: String): String {
            val encodedTitle = URLEncoder.encode(categoryTitle, StandardCharsets.UTF_8.toString())
            return "recipes/${categoryId}/${encodedTitle}"
        }
    }

    data object  RecipeDetails : Destination("recipe/{recipeId}") {
        fun createRoute(recipeId: Int): String {
            return "recipe/$recipeId"
        }
    }
}