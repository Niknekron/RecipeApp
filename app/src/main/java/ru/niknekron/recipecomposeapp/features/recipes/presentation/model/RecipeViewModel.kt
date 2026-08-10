package ru.niknekron.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.niknekron.recipecomposeapp.PARAM_CATEGORY_ID
import ru.niknekron.recipecomposeapp.PARAM_CATEGORY_IMAGE_URL
import ru.niknekron.recipecomposeapp.PARAM_CATEGORY_TITLE
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiState
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.toUiModel
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@HiltViewModel
class RecipesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository,
) : ViewModel() {

    private val categoryId: Int =
        savedStateHandle[PARAM_CATEGORY_ID] ?: 0

    private val categoryTitle: String =
        URLDecoder.decode(
            savedStateHandle[PARAM_CATEGORY_TITLE] ?: "",
            StandardCharsets.UTF_8.toString()
        )

    private val categoryImageUrl: String =
        URLDecoder.decode(
            savedStateHandle[PARAM_CATEGORY_IMAGE_URL] ?: "",
            StandardCharsets.UTF_8.toString()
        )

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
                repository
                    .getRecipesByCategory(categoryId)
                    .collect { recipeDtos ->
                        val recipes = recipeDtos.map { recipeDto ->
                            recipeDto.toUiModel()
                        }

                        _uiState.update { currentState ->
                            currentState.copy(
                                recipes = recipes,
                                isLoading = false,
                                error = null
                            )
                        }
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