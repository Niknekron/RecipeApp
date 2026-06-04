package ru.niknekron.recipecomposeapp.features.recipes.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepositoryStub
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiState
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.toUiModel

class RecipesViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val categoryId: Int = savedStateHandle["categoryId"] ?: 0

    private val categoryTitle: String =
        Uri.decode(savedStateHandle["categoryTitle"] ?: "")

    private val categoryImageUrl: String =
        Uri.decode(savedStateHandle["categoryImageUrl"] ?: "")

    private val _uiState = MutableStateFlow(
        RecipeUiState(
            categoryTitle = categoryTitle,
            categoryImageUrl = categoryImageUrl
        )
    )

    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val recipes = RecipesRepositoryStub
                    .getRecipesByCategoryId(categoryId)
                    .map { recipeDto ->
                        recipeDto.toUiModel()
                    }

                _uiState.update { currentState ->
                    currentState.copy(
                        recipes = recipes,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = exception.message ?: "Ошибка загрузки рецептов"
                    )
                }
            }
        }
    }
}