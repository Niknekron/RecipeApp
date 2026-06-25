package ru.niknekron.recipecomposeapp.data.repository

import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import ru.niknekron.recipecomposeapp.data.model.RecipeDto

interface RecipesRepository {
    suspend fun getCategories(): List<CategoryDto>

    suspend fun getRecipesByCategory(
        categoryId: Int
    ): List<RecipeDto>

    suspend fun getRecipe(
        recipeId: Int
    ): RecipeDto?
}