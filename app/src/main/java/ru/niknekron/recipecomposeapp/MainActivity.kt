package ru.niknekron.recipecomposeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.niknekron.recipecomposeapp.data.model.CategoryDto

class MainActivity : ComponentActivity() {

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
                val url = URL("https://recipes.androidsprint.ru/api/category")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"
                connection.connect()

                val response = BufferedReader(
                    connection.inputStream.reader()
                ).readText()

                val categories = Json.decodeFromString(
                    ListSerializer(CategoryDto.serializer()),
                    response
                )

                println("Получено категорий: ${categories.size}")

                categories.forEach { category ->
                    println("Категория: ${category.title}")
                }

                categories.forEach { category ->
                    loadRecipesForCategory(category)
                }

                connection.disconnect()
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
                val recipesUrl = URL(
                    "https://recipes.androidsprint.ru/api/category/${category.id}/recipes"
                )

                val recipesConnection =
                    recipesUrl.openConnection() as HttpURLConnection

                recipesConnection.requestMethod = "GET"
                recipesConnection.connect()

                val recipesResponse = BufferedReader(
                    recipesConnection.inputStream.reader()
                ).readText()

                println(
                    "Категория '${category.title}': responseCode=${recipesConnection.responseCode}"
                )

                println(
                    "Категория '${category.title}': получено ${recipesResponse.length} символов ответа"
                )

                recipesConnection.disconnect()
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
