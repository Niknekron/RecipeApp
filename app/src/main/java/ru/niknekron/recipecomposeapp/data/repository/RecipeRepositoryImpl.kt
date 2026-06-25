package ru.niknekron.recipecomposeapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import ru.niknekron.recipecomposeapp.data.model.RecipeDto
import android.util.Log

class RecipesRepositoryImpl(
    private val apiService: RecipesApiService,
) : RecipesRepository {

    private companion object {
        const val TAG = "RecipesRepository"
    }

    override suspend fun getCategories(): List<CategoryDto> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getCategories()
            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "Ошибка при загрузке категорий",
                    exception
                )
                emptyList()
            }
        }
    }

    override suspend fun getRecipesByCategory(
        categoryId: Int
    ): List<RecipeDto> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getRecipesByCategory(categoryId)
            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "Ошибка при загрузке рецептов категории $categoryId",
                    exception
                )
                emptyList()
            }
        }
    }

    override suspend fun getRecipe(
        recipeId: Int
    ): RecipeDto? {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getRecipe(recipeId)
            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "Ошибка при загрузке рецепта $recipeId",
                    exception
                )

                null
            }
        }
    }
}