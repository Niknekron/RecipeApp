package ru.niknekron.recipecomposeapp.features.details.presentation.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import ru.niknekron.recipecomposeapp.util.FavoriteDataStoreManager

class RecipeDetailsViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    private var favoriteJob: Job? = null

    fun initializeWithRecipe(recipe: RecipeUiModel) {
        _uiState.update { currentState ->
            currentState.copy(
                recipe = recipe,
                isLoading = false,
                error = null
            )
        }

        observeFavorite(recipe.id)
    }

    private fun observeFavorite(recipeId: Int) {
        favoriteJob?.cancel()

        favoriteJob = viewModelScope.launch {
            favoriteDataStoreManager
                .isFavoriteFlow(recipeId)
                .collect { isFavorite ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isFavorite = isFavorite
                        )
                    }
                }
        }
    }

    fun toggleFavorite() {
        val recipe = _uiState.value.recipe ?: return
        val isFavorite = _uiState.value.isFavorite

        viewModelScope.launch {
            if (isFavorite) {
                favoriteDataStoreManager.removeFavorite(recipe.id)
            } else {
                favoriteDataStoreManager.addFavorite(recipe.id)
            }
        }
    }

    fun updatePortions(portionsCount: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                portionsCount = portionsCount
            )
        }
    }
}