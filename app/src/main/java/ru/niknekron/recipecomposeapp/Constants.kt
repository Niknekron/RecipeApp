package ru.niknekron.recipecomposeapp

const val PARAM_RECIPE_ID = "recipeId"

const val DEEP_LINK_SCHEME = "recipeapp"
const val DEEP_LINK_BASE_URL = "https://recipes.androidsprint.ru"

fun createRecipeDeepLink(recipeId: Int): String {
    return "$DEEP_LINK_BASE_URL/recipe/$recipeId"
}