package ru.niknekron.recipecomposeapp.data.repository

import android.util.Log
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import ru.niknekron.recipecomposeapp.data.database.RecipesDatabase
import ru.niknekron.recipecomposeapp.data.database.dao.CategoryDao
import ru.niknekron.recipecomposeapp.data.database.dao.RecipeDao
import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import ru.niknekron.recipecomposeapp.data.model.RecipeDto
import ru.niknekron.recipecomposeapp.data.model.toDto
import ru.niknekron.recipecomposeapp.data.model.toEntity

class RecipesRepositoryImpl @Inject constructor(
    private val apiService: RecipesApiService,
    database: RecipesDatabase,
) : RecipesRepository {

    private val categoryDao: CategoryDao = database.categoryDao()

    private val recipeDao: RecipeDao = database.recipeDao()

    override fun getCategories(): Flow<List<CategoryDto>> {
        return channelFlow {
            launch(Dispatchers.IO) {
                try {
                    val categoriesFromApi = apiService.getCategories()

                    val categoryEntities =
                        categoriesFromApi.map { categoryDto ->
                            categoryDto.toEntity()
                        }

                    categoryDao.insertCategories(categoryEntities)

                    Log.d(
                        TAG,
                        "Обновлено категорий: ${categoriesFromApi.size}"
                    )
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    Log.e(
                        TAG,
                        "Ошибка при обновлении категорий",
                        exception
                    )
                }
            }

            categoryDao
                .getAllCategories()
                .map { categoryEntities ->
                    categoryEntities.map { categoryEntity ->
                        categoryEntity.toDto()
                    }
                }
                .collect { categories ->
                    send(categories)
                }
        }
    }

    override fun getRecipesByCategory(
        categoryId: Int,
    ): Flow<List<RecipeDto>> {
        return channelFlow {
            launch(Dispatchers.IO) {
                try {
                    val recipesFromApi =
                        apiService.getRecipesByCategory(categoryId)

                    val recipeEntities =
                        recipesFromApi.map { recipeDto ->
                            recipeDto.toEntity(
                                categoryId = categoryId
                            )
                        }

                    recipeDao.insertRecipes(recipeEntities)

                    Log.d(
                        TAG,
                        "Обновлено рецептов категории $categoryId: " +
                                "${recipesFromApi.size}"
                    )
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    Log.e(
                        TAG,
                        "Ошибка при обновлении рецептов " +
                                "категории $categoryId",
                        exception
                    )
                }
            }

            recipeDao
                .getRecipesByCategory(categoryId)
                .map { recipeEntities ->
                    recipeEntities.map { recipeEntity ->
                        recipeEntity.toDto()
                    }
                }
                .collect { recipes ->
                    send(recipes)
                }
        }
    }

    override fun getRecipe(
        recipeId: Int,
    ): Flow<RecipeDto?> {
        return channelFlow {
            launch(Dispatchers.IO) {
                try {
                    /*
                     * Берём существующий рецепт из Room,
                     * чтобы сохранить его categoryId.
                     *
                     * В RecipeDto поле categoryId отсутствует,
                     * поэтому получить его только из ответа API нельзя.
                     */
                    val cachedRecipe =
                        recipeDao
                            .getRecipeById(recipeId)
                            .first()

                    val freshRecipe =
                        apiService.getRecipe(recipeId)

                    if (cachedRecipe != null) {
                        val updatedEntity =
                            freshRecipe.toEntity(
                                categoryId = cachedRecipe.categoryId
                            )

                        recipeDao.insertRecipes(
                            listOf(updatedEntity)
                        )

                        Log.d(
                            TAG,
                            "Рецепт $recipeId обновлён в базе данных"
                        )
                    } else {
                        Log.w(
                            TAG,
                            "Рецепт $recipeId получен из API, " +
                                    "но не сохранён: неизвестен categoryId"
                        )
                    }
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    Log.e(
                        TAG,
                        "Ошибка при обновлении рецепта $recipeId",
                        exception
                    )
                }
            }

            recipeDao
                .getRecipeById(recipeId)
                .map { recipeEntity ->
                    recipeEntity?.toDto()
                }
                .collect { recipe ->
                    send(recipe)
                }
        }
    }

    override fun getRecipesByIds(
        ids: List<Int>,
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

    private companion object {
        const val TAG = "RecipesRepository"
    }
}