package ru.niknekron.recipecomposeapp.data.repository

import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import ru.niknekron.recipecomposeapp.data.database.RecipesDatabase
import ru.niknekron.recipecomposeapp.data.database.dao.CategoryDao
import ru.niknekron.recipecomposeapp.data.database.dao.RecipeDao
import ru.niknekron.recipecomposeapp.data.database.entity.CategoryEntity
import ru.niknekron.recipecomposeapp.data.database.entity.RecipeEntity

class RecipesRepositoryTest {
    private lateinit var apiService: RecipesApiService
    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var recipeDao: RecipeDao

    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setUp() {
        apiService = io.mockk.mockk()
        database = io.mockk.mockk(relaxed = true)
        categoryDao = io.mockk.mockk()
        recipeDao = io.mockk.mockk()

        every {
            database.categoryDao()
        } returns categoryDao

        every {
            database.recipeDao()
        } returns recipeDao

        repository = RecipesRepositoryImpl(
            apiService = apiService,
            database = database
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCategories emits categories from database`() = runTest {
        //Arrange
        val cachedCategories = listOf(
            CategoryEntity(
                id = 1,
                name = "Завтраки",
                description = "Утренние блюда",
                imageUrl = "breakfast.jpg"
            )
        )

        every {
            categoryDao.getAllCategories()
        } returns flowOf(cachedCategories)

        coEvery {
            apiService.getCategories()
        } returns emptyList()

        coEvery {
            categoryDao.insertCategories(any())
        } just Runs

        //Act + Assert
        repository.getCategories().test {
            val result = awaitItem()

            assertEquals(1, result.size)
            assertEquals(1, result.first().id)
            assertEquals("Завтраки", result.first().title)
            assertEquals(
                "Утренние блюда",
                result.first().description
            )
            assertEquals(
                "breakfast.jpg",
                result.first().imageUrl
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getCategories still emits data when api throws exception`() = runTest {
        //Arrange
        val cachedCategories = listOf(
            CategoryEntity(
                id = 2,
                name = "Десерты",
                description = "Сладки блюда",
                imageUrl = "dessert.jpg",
            )
        )

        every {
            categoryDao.getAllCategories()
        } returns flowOf(cachedCategories)

        coEvery {
            apiService.getCategories()
        } throws IllegalStateException("Network error")

        coEvery {
            categoryDao.insertCategories(any())
        } just Runs

        //Act + Assert
        repository.getCategories().test {
            val result = awaitItem()

            assertEquals(1, result.size)
            assertEquals(2, result.first().id)
            assertEquals("Десерты", result.first().title)
            assertEquals(
                "Сладкие блюда",
                result.first().description
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRecipesByCategory returns flow filtered by categoryId`() = runTest {
        //Arrange
        val categoryId = 3

        val cachedRecipes = listOf(
            RecipeEntity(
                id = 10,
                title = "Пицца Маргарита",
                categoryId = categoryId,
                imageUrl = "pizza.jpg",
                ingredients = listOf(
                    "200:::г:::Мука"
                ),
                method = listOf(
                    "Замесить тесто",
                    "Добавить начинку"
                )
            )
        )

        every {
            recipeDao.getRecipesByCategory(categoryId)
        } returns flowOf(cachedRecipes)

        coEvery {
            apiService.getRecipesByCategory(categoryId)
        } returns emptyList()

        coEvery {
            recipeDao.insertRecipes(any())
        } just Runs

        //Act + Assert
        repository.getRecipesByCategory(categoryId).test {
            val result = awaitItem()

            assertEquals(1, result.size)
            assertEquals(10, result.first().id)
            assertEquals(
                "Пицца Маргарита",
                result.first().title
            )
            assertEquals(
                "pizza.jpg",
                result.first().imageUrl
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}