package ru.niknekron.recipecomposeapp.util

import android.content.Context
import androidx.core.content.edit

class FavoritePrefsManager(
    context: Context,
) {
    private val sharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun isFavorite(recipeId: Int): Boolean {
        return getAllFavorites().contains(recipeId.toString())
    }

    fun addToFavorites(recipeId: Int) {
        val updatedFavorites = getAllFavorites().toMutableSet()
        updatedFavorites.add(recipeId.toString())

        sharedPreferences.edit {
            putStringSet(KEY_FAVORITE_RECIPE_IDS, updatedFavorites)
        }
    }

    fun removeFromFavorites(recipeId: Int) {
        val updatedFavorites = getAllFavorites().toMutableSet()
        updatedFavorites.remove(recipeId.toString())

        sharedPreferences.edit {
            putStringSet(KEY_FAVORITE_RECIPE_IDS, updatedFavorites)
        }
    }

    fun getAllFavorites(): Set<String> {
        return sharedPreferences
            .getStringSet(KEY_FAVORITE_RECIPE_IDS, emptySet())
            .orEmpty()
    }

    private companion object {
        const val PREFS_NAME = "recipe_app_prefs"
        const val KEY_FAVORITE_RECIPE_IDS = "favorite_recipe_ids"
    }
}