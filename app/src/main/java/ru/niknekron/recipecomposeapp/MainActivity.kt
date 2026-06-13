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
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.niknekron.recipecomposeapp.data.model.CategoryDto

class MainActivity : ComponentActivity() {

    private var deepLinkIntent by mutableStateOf<Intent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println(
            "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}"
        )

        Thread{
            try {
                println(
                    "Выполняю запрос на потоке: ${Thread.currentThread().name}"
                )

                val url = URL(
                    "https://recipes.androidsprint.ru/api/category"
                )
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"

                connection.connect()

                println("Response code: ${connection.responseCode}")
                println("Response message: ${connection.responseMessage}")

                val response = BufferedReader(
                    connection.inputStream.reader()
                ).readText()

                println(response)

                val categories = Json.decodeFromString(
                    ListSerializer(CategoryDto.serializer()),
                    response
                )

                println(
                    "Получено категорий: ${categories.size}"
                )

                categories.forEach { category ->
                    println(
                        "Категория: ${category.title}"
                    )
                }

                connection.disconnect()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }.start()

        deepLinkIntent = intent

        setContent {
            RecipesApp(
                deepLinkIntent = deepLinkIntent
            )
        }
    }



    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        deepLinkIntent = intent
    }
}