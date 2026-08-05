package ru.niknekron.recipecomposeapp.core.network.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
class RecipesApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: RecipesApiService

    @Before
    fun setUp() {
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
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun categoriesJsonIsDeserializedToCategoryDto() = runTest {
        // Arrange
        val responseBody = """
            [
                {
                    "id": 1,
                    "title": "Завтраки",
                    "description": "Утренние блюда",
                    "imageUrl": "breakfast.jpg"
                },
                {
                    "id": 2,
                    "title": "Десерты",
                    "description": "Сладкие блюда",
                    "imageUrl": "dessert.jpg"
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

        // Act
        val result = apiService.getCategories()

        // Assert
        assertEquals(2, result.size)

        assertEquals(1, result[0].id)
        assertEquals("Завтраки", result[0].title)
        assertEquals(
            "Утренние блюда",
            result[0].description
        )
        assertEquals(
            "breakfast.jpg",
            result[0].imageUrl
        )

        assertEquals(2, result[1].id)
        assertEquals("Десерты", result[1].title)
        assertEquals(
            "Сладкие блюда",
            result[1].description
        )
        assertEquals(
            "dessert.jpg",
            result[1].imageUrl
        )
    }
}