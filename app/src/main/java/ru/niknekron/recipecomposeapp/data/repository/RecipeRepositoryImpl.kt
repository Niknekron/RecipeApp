package ru.niknekron.recipecomposeapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import ru.niknekron.recipecomposeapp.data.model.RecipeDto
import android.util.Log
import ru.niknekron.recipecomposeapp.data.database.RecipesDatabase
import ru.niknekron.recipecomposeapp.data.database.dao.CategoryDao
import ru.niknekron.recipecomposeapp.data.database.dao.RecipeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.niknekron.recipecomposeapp.data.model.toDto
import ru.niknekron.recipecomposeapp.data.model.toEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.niknekron.recipecomposeapp.data.model.toDto

class RecipesRepositoryImpl(
    private val apiService: RecipesApiService,
    database: RecipesDatabase,
) : RecipesRepository {


    private val categoryDao: CategoryDao = database.categoryDao()

    private val recipeDao: RecipeDao = database.recipeDao()
    private companion object {
        const val TAG = "RecipesRepository"
    }

    override fun getCategories(): Flow<List<CategoryDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categoriesFromApi = apiService.getCategories()

                val categoryEntities = categoriesFromApi.map { categoryDto ->
                    categoryDto.toEntity()
                }

                categoryDao.insertCategories(categoryEntities)

                Log.d(
                    TAG,
                    "Обновлено категорий: ${categoriesFromApi.size}"
                )
            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "Ошибка при обновлении категорий",
                    exception
                )
            }
        }
        return categoryDao
            .getAllCategories()
            .map { categoryEntities ->
                categoryEntities.map {categoryEntity ->
                    categoryEntity.toDto()
                }
            }
    }

    override fun getRecipesByCategory(
        categoryId: Int
    ): Flow<List<RecipeDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val recipesFromApi =
                    apiService.getRecipesByCategory(categoryId)

                val recipeEntities = recipesFromApi.map { recipeDto ->
                    recipeDto.toEntity(
                        categoryId = categoryId
                    )
                }

                recipeDao.insertRecipes(recipeEntities)

                Log.d(
                    TAG,
                    "Обновлено рецептов категории $categoryId: ${recipesFromApi.size}"
                )
            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "Ошибка при обновлении рецептов категории $categoryId",
                    exception
                )
            }
        }

        return recipeDao
            .getRecipesByCategory(categoryId)
            .map { recipeEntities ->
                recipeEntities.map { recipeEntity ->
                    recipeEntity.toDto()
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

    override fun getRecipesByIds(
        ids: List<Int>
    ): Flow<List<RecipeDto>> {
        if (ids.isEmpty()) {
            return flowOf(emptyList())
        }

        return recipeDao
            .getRecipesByIds(ids)
            .map { recipeEntities ->
                recipeEntities.map { recipeEntity ->
                    recipeEntity.toDto()
                }
            }
    }
}