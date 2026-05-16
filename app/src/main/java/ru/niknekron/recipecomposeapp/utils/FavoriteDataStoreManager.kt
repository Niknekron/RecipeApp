package ru.niknekron.recipecomposeapp.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class FavoriteDataStoreManager(
    private val context: Context,
) {
    suspend fun isFavorite(recipeId: Int): Boolean {
        val preferences = context.dataStore.data.first()
        val favoriteIds = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS].orEmpty()

        return favoriteIds.contains(recipeId.toString())
    }

    suspend fun addFavorite(recipeId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS].orEmpty()
            val updatedFavorites = currentFavorites + recipeId.toString()

            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = updatedFavorites
        }
    }

    suspend fun removeFavorite(recipeId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS].orEmpty()
            val updatedFavorites = currentFavorites - recipeId.toString()

            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = updatedFavorites
        }
    }
}