package ru.niknekron.recipecomposeapp.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import ru.niknekron.recipecomposeapp.data.database.RecipesDatabase

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CompleteDataFlowTest {

    private lateinit var database: RecipesDatabase
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: RecipesApiService
    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(
            UnconfinedTestDispatcher()
        )

        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            RecipesDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        mockWebServer = MockWebServer()
        mockWebServer.start()

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(RecipesApiService::class.java)

        repository = RecipesRepositoryImpl(
            apiService = apiService,
            database = database
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        database.close()
        mockWebServer.shutdown()
    }

    @Test
    fun categoriesAreLoadedFromApiAndStoredInCache() = runTest {
        // Arrange
        val responseBody = """
            [
                {
                    "id": 1,
                    "title": "Завтраки",
                    "description": "Утренние блюда",
                    "imageUrl": "breakfast.jpg"
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader(
                    "Content-Type",
                    "application/json"
                )
                .setBody(responseBody)
        )

        // Act + Assert
        repository.getCategories().test {
            var result = awaitItem()

            if (result.isEmpty()) {
                result = awaitItem()
            }

            assertEquals(1, result.size)
            assertEquals(1, result.first().id)
            assertEquals(
                "Завтраки",
                result.first().title
            )
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

        // Проверяем реальный кеш Room
        val cachedCategories = database
            .categoryDao()
            .getAllCategories()
            .first()

        assertEquals(1, cachedCategories.size)
        assertEquals(1, cachedCategories.first().id)
        assertEquals(
            "Завтраки",
            cachedCategories.first().name
        )
        assertEquals(
            "Утренние блюда",
            cachedCategories.first().description
        )
        assertEquals(
            "breakfast.jpg",
            cachedCategories.first().imageUrl
        )
    }

    @Test
    fun emptyApiResponseStoresEmptyCache() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader(
                    "Content-Type",
                    "application/json"
                )
                .setBody("[]")
        )

        // Act + Assert
        repository.getCategories().test {
            val result = awaitItem()

            assertTrue(result.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }

        val cachedCategories = database
            .categoryDao()
            .getAllCategories()
            .first()

        assertTrue(cachedCategories.isEmpty())
    }
}