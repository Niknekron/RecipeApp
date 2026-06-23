package ru.niknekron.recipecomposeapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import ru.niknekron.recipecomposeapp.data.model.RecipeDto

class RecipesRepositoryImpl(
    private val apiService: RecipesApiService,
) : RecipesRepository {

    override suspend fun getCategories(): List<CategoryDto> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getCategories()
            } catch (exception: Exception) {
                println(
                    "Ошибка при загрузке категорий: ${exception.message}"
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
                println(
                    "Ошибка при загрузке рецептов категории $categoryId: ${exception.message}"
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
                getCategories()
                    .flatMap { category ->
                        getRecipesByCategory(category.id)
                    }
                    .firstOrNull { recipe ->
                        recipe.id == recipeId
                    }
            } catch (exception: Exception) {
                println(
                    "Ошибка при загрузке рецепта $recipeId: ${exception.message}"
                )
                throw exception
            }
        }
    }
}