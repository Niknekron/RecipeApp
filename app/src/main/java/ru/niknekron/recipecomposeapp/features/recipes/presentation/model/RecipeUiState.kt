package ru.niknekron.recipecomposeapp.features.recipes.presentation.model

import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

data class RecipeUiState(
    val recipes: List<RecipeUiModel> = emptyList(),
    val categoryTitle: String = "",
    val categoryImageUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val isEmpty: Boolean
        get() = recipes.isEmpty() && !isLoading && error == null
}