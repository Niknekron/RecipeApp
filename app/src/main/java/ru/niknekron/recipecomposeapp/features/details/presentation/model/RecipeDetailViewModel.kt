package ru.niknekron.recipecomposeapp.features.details.presentation.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepositoryStub
import ru.niknekron.recipecomposeapp.util.FavoriteDataStoreManager
import ru.niknekron.recipecomposeapp.PARAM_RECIPE_ID
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.toUiModel

class RecipeDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    application: Application,
) : AndroidViewModel(application) {

    private val recipeId: Int =
        savedStateHandle[PARAM_RECIPE_ID] ?: 0

    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    private var favoriteJob: Job? = null

    init {
        loadRecipe(recipeId)
        observeFavorite(recipeId)
    }

//    fun initializeWithRecipe(recipe: RecipeUiModel) {
//        _uiState.update { currentState ->
//            currentState.copy(
//                recipe = recipe,
//                isLoading = false,
//                error = null
//            )
//        }
//
//        observeFavorite(recipe.id)
//    }

    private fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val recipe = RecipesRepositoryStub
                    .getRecipeById(recipeId)
                    ?.toUiModel()

                _uiState.update { currentState ->
                    currentState.copy(
                        recipe = recipe,
                        isLoading = false,
                        error = if (recipe == null) {
                            "Рецепт не найден"
                        } else {
                            null
                        }
                    )
                }
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = exception.message ?: "Ошибка загрузки рецепта"
                    )
                }
            }
        }
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
        val safePortionsCount = portionsCount.coerceAtLeast(1)

        _uiState.update { currentState ->
            currentState.copy(
                portionsCount = safePortionsCount
            )
        }
    }
}