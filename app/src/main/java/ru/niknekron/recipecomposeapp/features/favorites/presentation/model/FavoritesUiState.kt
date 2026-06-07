package ru.niknekron.recipecomposeapp.features.favorites.presentation.model

import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

data class FavoritesUiState(
    val recipes: List<RecipeUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val isEmpty: Boolean
        get() = recipes.isEmpty() && !isLoading && error == null
}