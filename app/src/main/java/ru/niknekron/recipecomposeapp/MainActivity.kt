package ru.niknekron.recipecomposeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : ComponentActivity() {

    private val okHttpClient: OkHttpClient = OkHttpClient()

    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

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
        threadPool.execute {
            println(
                "Запрашиваю категории на потоке: ${Thread.currentThread().name}"
            )

            try {
                val request = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/category")
                    .build()

                val response = okHttpClient
                    .newCall(request)
                    .execute()

                val responseBody = response.body?.string()
                    ?:throw IllegalStateException(
                        "Пустой ответ сервера"
                    )

                val categories = Json.decodeFromString(
                    ListSerializer(CategoryDto.serializer()),
                    responseBody
                )

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
        threadPool.execute {
            println(
                "Запрашиваю рецепты категории '${category.title}' на потоке: ${Thread.currentThread().name}"
            )

            try {

                val request = Request.Builder()
                    .url(
                        "https://recipes.androidsprint.ru/api/category/${category.id}/recipes"
                    )
                    .build()

                val response = okHttpClient
                    .newCall(request)
                    .execute()

                val responseBody = response.body?.string()
                    ?: throw IllegalStateException(
                        "Пустой ответ сервера"
                    )

                println(
                    "Категория '${category.title}' выполняется на потоке: ${Thread.currentThread().name}"
                )

                println(
                    "Категория '${category.title}': получено ${responseBody.length} символов"
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

    override fun onDestroy() {
        super.onDestroy()

        threadPool.shutdown()
    }
}
