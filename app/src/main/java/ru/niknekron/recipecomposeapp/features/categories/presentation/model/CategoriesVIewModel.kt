package ru.niknekron.recipecomposeapp.features.categories.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.toUiModel

class CategoriesViewModel(
    private val repository: RecipesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val categories = repository
                    .getCategories()
                    .map { categoryDto ->
                        categoryDto.toUiModel()
                    }

                _uiState.update { currentState ->
                    currentState.copy(
                        categories = categories,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = exception.message ?: "Ошибка загрузки категорий"
                    )
                }
            }
        }
    }
}


