package ru.niknekron.recipecomposeapp.features.details.presentation.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.niknekron.recipecomposeapp.util.FavoriteDataStoreManager
import ru.niknekron.recipecomposeapp.PARAM_RECIPE_ID
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.toUiModel
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository,
    private val favoriteDataStoreManager: FavoriteDataStoreManager,
) : ViewModel() {

    private val recipeId: Int =
        savedStateHandle[PARAM_RECIPE_ID] ?: 0

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    private var favoriteJob: Job? = null

    init {
        observeRecipe()
        observeFavorite(recipeId)
    }

    private fun observeRecipe() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                repository
                    .getRecipe(recipeId)
                    .collect { recipeDto ->
                        if (recipeDto == null) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    recipe = null,
                                    isLoading = true,
                                    error = null
                                )
                            }
                        } else {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    recipe = recipeDto.toUiModel(),
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }
                    }
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = exception.message
                            ?: "Ошибка загрузки рецепта"
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
        _uiState.update { currentState ->
            currentState.copy(
                portionsCount = portionsCount.coerceAtLeast(1)
            )
        }
    }
}