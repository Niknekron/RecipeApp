package ru.niknekron.recipecomposeapp.data.model

data class RecipeDto(
    val id: Int,
    val title: String,
    val ingredients: List<IngerdientDto>,
    val method: List<String>,
    val imageUrl: String,

    )