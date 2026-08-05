package ru.niknekron.recipecomposeapp.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import ru.niknekron.recipecomposeapp.data.database.RecipesDatabase
import ru.niknekron.recipecomposeapp.data.database.dao.CategoryDao
import ru.niknekron.recipecomposeapp.data.database.entity.CategoryEntity
import ru.niknekron.recipecomposeapp.data.model.CategoryDto

@RunWith(AndroidJUnit4::class)
class RecipesRepositoryIntegrationTest {

    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var repository: RecipesRepositoryImpl

    private val apiService: RecipesApiService = mockk()

    @Before
    fun setUp() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            RecipesDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        categoryDao = database.categoryDao()

        repository = RecipesRepositoryImpl(
            apiService = apiService,
            database = database
        )
    }

    @After
    fun tearDown() {
        database.close()
        clearAllMocks()
    }

    @Test
    fun savesDataToCacheAfterSuccessfulApiCall() = runTest {
        // Arrange
        val categoriesFromApi = listOf(
            CategoryDto(
                id = 1,
                title = "Завтраки",
                description = "Утренние блюда",
                imageUrl = "breakfast.jpg"
            )
        )

        coEvery {
            apiService.getCategories()
        } returns categoriesFromApi

        // Act + Assert
        repository.getCategories().test {
            /*
             * В зависимости от порядка выполнения корутин первым элементом
             * может быть пустой кеш либо уже обновлённый список.
             */
            var result = awaitItem()

            if (result.isEmpty()) {
                result = awaitItem()
            }

            assertEquals(categoriesFromApi.size, result.size)
            assertEquals(
                categoriesFromApi.first().id,
                result.first().id
            )
            assertEquals(
                categoriesFromApi.first().title,
                result.first().title
            )
            assertEquals(
                categoriesFromApi.first().description,
                result.first().description
            )

            cancelAndIgnoreRemainingEvents()
        }

        // Проверяем, что данные действительно записались в Room.
        val cachedCategories = categoryDao
            .getAllCategories()
            .first()

        assertEquals(categoriesFromApi.size, cachedCategories.size)
        assertEquals(
            categoriesFromApi.first().id,
            cachedCategories.first().id
        )
        assertEquals(
            categoriesFromApi.first().title,
            cachedCategories.first().name
        )
        assertEquals(
            categoriesFromApi.first().description,
            cachedCategories.first().description
        )

        coVerify(exactly = 1) {
            apiService.getCategories()
        }
    }

    @Test
    fun returnsCachedDataWhenApiFails() = runTest {
        // Arrange
        val cachedCategories = listOf(
            CategoryEntity(
                id = 2,
                name = "Десерты",
                description = "Сладкие блюда",
                imageUrl = "dessert.jpg"
            )
        )

        categoryDao.insertCategories(cachedCategories)

        coEvery {
            apiService.getCategories()
        } throws IOException("Network error")

        // Act + Assert
        repository.getCategories().test {
            val result = awaitItem()

            assertEquals(cachedCategories.size, result.size)
            assertEquals(
                cachedCategories.first().id,
                result.first().id
            )
            assertEquals(
                cachedCategories.first().name,
                result.first().title
            )
            assertEquals(
                cachedCategories.first().description,
                result.first().description
            )
            assertEquals(
                cachedCategories.first().imageUrl,
                result.first().imageUrl
            )

            cancelAndIgnoreRemainingEvents()
        }

        // Ошибка API не должна удалить данные из кеша.
        val categoriesAfterFailure = categoryDao
            .getAllCategories()
            .first()

        assertTrue(categoriesAfterFailure.isNotEmpty())
        assertEquals(
            cachedCategories,
            categoriesAfterFailure
        )

        coVerify(exactly = 1) {
            apiService.getCategories()
        }
    }
}