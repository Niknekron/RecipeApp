package ru.niknekron.recipecomposeapp.data.repository

import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import ru.niknekron.recipecomposeapp.data.model.RecipeDto
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun getCategories(): Flow<List<CategoryDto>>

    fun getRecipesByCategory(
        categoryId: Int
    ): Flow<List<RecipeDto>>

    suspend fun getRecipe(
        recipeId: Int
    ): RecipeDto?

    fun getRecipesByIds(
        ids: List<Int>
    ): Flow<List<RecipeDto>>
}