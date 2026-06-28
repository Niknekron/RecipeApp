package ru.niknekron.recipecomposeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.json.Json
import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.niknekron.recipecomposeapp.core.network.api.NetworkConfig
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit



class MainActivity : ComponentActivity() {


    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .client(okHttpClient)
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
                deepLinkIntent = deepLinkIntent,
                apiService = apiService
            )
        }
    }

    private fun loadCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            println(
                "Запрашиваю категории через Retrofit на потоке: ${Thread.currentThread().name}"
            )

            try {
                val categories = apiService.getCategories()

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
        CoroutineScope(Dispatchers.IO).launch {
            println(
                "Запрашиваю рецепты категории '${category.title}' через Retrofit на потоке: ${Thread.currentThread().name}"
            )

            try {
                val recipes = apiService.getRecipesByCategory(category.id)

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
