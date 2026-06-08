package ru.niknekron.recipecomposeapp.features.favorites.presentation.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepositoryStub
import ru.niknekron.recipecomposeapp.features.favorites.presentation.model.FavoritesUiState
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.toUiModel
import ru.niknekron.recipecomposeapp.util.FavoriteDataStoreManager

class FavoriteViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    val uiState = favoriteDataStoreManager
        .getFavoriteIdsFlow()
        .map {favoriteIds ->
            val recipes = favoriteIds.mapNotNull { id ->
                id.toIntOrNull()?.let { recipeId ->
                    RecipesRepositoryStub
                        .getRecipeById(recipeId)
                        ?.toUiModel()
                }
            }

            FavoritesUiState(
                recipes = recipes,
                isLoading = false,
                error = null
            )
        }
        .catch { exception ->
            emit(
                FavoritesUiState(
                    isLoading = false,
                    error = exception.message ?: "Favorites loading error"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesUiState(isLoading = true)
        )
}