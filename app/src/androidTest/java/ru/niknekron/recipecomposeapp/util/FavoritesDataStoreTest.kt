package ru.niknekron.recipecomposeapp.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesDataStoreTest {

    private lateinit var context: Context
    private lateinit var manager: FavoriteDataStoreManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        manager = FavoriteDataStoreManager(context)
    }

    @After
    fun tearDown() {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }

    @Test
    fun addFavoriteSavesRecipeId() = runTest {
        // Arrange
        val recipeId = 42

        // Act
        manager.addFavorite(recipeId)

        // Assert
        val favoriteIds = manager
            .getFavoriteIdsFlow()
            .first()

        assertTrue(
            favoriteIds.contains(recipeId.toString())
        )
    }

    @Test
    fun removeFromFavoritesDeletesRecipeId() = runTest {
        // Arrange
        val recipeId = 42

        manager.addFavorite(recipeId)

        // Act
        manager.removeFavorite(recipeId)

        // Assert
        val favoriteIds = manager
            .getFavoriteIdsFlow()
            .first()

        assertFalse(
            favoriteIds.contains(recipeId.toString())
        )
    }

    @Test
    fun favoritesFlowEmitsUpdatesReactively() = runTest {
        // Arrange
        val recipeId = 42
        val recipeIdString = recipeId.toString()

        manager
            .getFavoriteIdsFlow()
            .test {
                val initialIds = awaitItem()

                assertFalse(
                    initialIds.contains(recipeIdString)
                )

                // Act
                manager.addFavorite(recipeId)

                // Assert
                val updatedIds = awaitItem()

                assertTrue(
                    updatedIds.contains(recipeIdString)
                )

                cancelAndIgnoreRemainingEvents()
            }
    }
}