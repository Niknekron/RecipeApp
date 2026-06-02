package ru.niknekron.recipecomposeapp.features.categories.presentation.model

data class CategoriesUiState(
    val categories:List<CategoryUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)