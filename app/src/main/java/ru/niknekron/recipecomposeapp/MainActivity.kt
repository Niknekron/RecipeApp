package ru.niknekron.recipecomposeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.niknekron.recipecomposeapp.core.network.api.NetworkConfig
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private val okHttpClient: OkHttpClient = OkHttpClient()

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(
            json.asConverterFactory(
                "application/json".toMediaType()
            )
        )
        .build()

    private val apiService: RecipesApiService =
        retrofit.create(RecipesApiService::class.java)

    private var deepLinkIntent by mutableStateOf<Intent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deepLinkIntent = intent

        println(
            "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}"
        )

        loadCategories()

        setContent {
            RecipesApp(
                deepLinkIntent = deepLinkIntent
            )
        }
    }

    private fun loadCategories() {
        thread {
            println(
                "Запрашиваю категории через Retrofit на потоке: ${Thread.currentThread().name}"
            )

            try {
                val categories = runBlocking {
                    apiService.getCategories()
                }

                println("Получено категорий: ${categories.size}")

                categories.forEach { category ->
                    println("Категория: ${category.title}")
                }

                categories.forEach { category ->
                    loadRecipesForCategory(category)
                }
            } catch (exception: Exception) {
                println(
                    "Ошибка при загрузке категорий: ${exception.message}"
                )
                exception.printStackTrace()
            }
        }
    }

    private fun loadRecipesForCategory(category: CategoryDto) {
        thread {
            println(
                "Запрашиваю рецепты категории '${category.title}' через Retrofit на потоке: ${Thread.currentThread().name}"
            )

            try {
                val recipes = runBlocking {
                    apiService.getRecipesByCategory(category.id)
                }

                println(
                    "Категория '${category.title}': получено рецептов: ${recipes.size}"
                )
            } catch (exception: Exception) {
                println(
                    "Ошибка при загрузке рецептов категории '${category.title}': ${exception.message}"
                )
                exception.printStackTrace()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        deepLinkIntent = intent
    }
}
