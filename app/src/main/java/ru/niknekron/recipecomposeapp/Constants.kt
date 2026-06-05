package ru.niknekron.recipecomposeapp

const val PARAM_RECIPE_ID = "recipeId"
const val ASSETS_URI_PREFIX = "file:///android_asset/"
const val DEEP_LINK_SCHEME = "recipeapp"
const val DEEP_LINK_BASE_URL = "https://recipes.androidsprint.ru"
const val PARAM_CATEGORY_ID = "categoryId"
const val PARAM_CATEGORY_TITLE = "categoryTitle"
const val PARAM_CATEGORY_IMAGE_URL = "categoryImageUrl"

fun createRecipeDeepLink(recipeId: Int): String {
    return "$DEEP_LINK_BASE_URL/recipe/$recipeId"
}