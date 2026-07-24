package ru.niknekron.recipecomposeapp.features.favorites.presentation.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.toUiModel
import ru.niknekron.recipecomposeapp.util.FavoriteDataStoreManager

class FavoriteViewModel(
    application: Application,
    private val repository: RecipesRepository,
) : AndroidViewModel(application) {

    private val favoriteDataStoreManager =
        FavoriteDataStoreManager(application)

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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState(
                isLoading = true
            )
        )
}