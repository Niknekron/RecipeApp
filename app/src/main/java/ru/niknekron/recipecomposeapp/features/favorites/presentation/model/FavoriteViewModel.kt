package ru.niknekron.recipecomposeapp.features.favorites.presentation.model

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.toUiModel
import ru.niknekron.recipecomposeapp.util.FavoriteDataStoreManager
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: RecipesRepository,
    private val favoriteDataStoreManager: FavoriteDataStoreManager,
) : ViewModel() {


    val uiState = favoriteDataStoreManager
        .getFavoriteIdsFlow()
        .flatMapLatest { favoriteIds ->
            val recipeIds = favoriteIds.mapNotNull { id ->
                id.toIntOrNull()
            }

            if (recipeIds.isEmpty()) {
                flowOf(emptyList())
            } else {
                repository.getRecipesByIds(recipeIds)
            }
        }
        .map { recipeDtos ->
            FavoritesUiState(
                recipes = recipeDtos.map { recipeDto ->
                    recipeDto.toUiModel()
                },
                isLoading = false,
                error = null
            )
        }
        .catch { exception ->
            emit(
                FavoritesUiState(
                    recipes = emptyList(),
                    isLoading = false,
                    error = exception.message ?: "Ошибка загрузки избранного"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState(
                isLoading = true
            )
        )
}