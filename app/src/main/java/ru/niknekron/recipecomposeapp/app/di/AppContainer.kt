package ru.niknekron.recipecomposeapp.app.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.niknekron.recipecomposeapp.BuildConfig
import ru.niknekron.recipecomposeapp.core.network.api.NetworkConfig
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import ru.niknekron.recipecomposeapp.data.database.RecipesDatabase
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepositoryImpl

class AppContainer(
    context: Context,
) {
    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    private val okHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

    private val json: Json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

    private val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()

    private val recipesApi: RecipesApiService =
        retrofit.create(RecipesApiService::class.java)

    private val recipesDatabase: RecipesDatabase =
        RecipesDatabase.buildDatabase(
            context = context.applicationContext
        )

    val recipesRepository: RecipesRepository =
        RecipesRepositoryImpl(
            apiService = recipesApi,
            database = recipesDatabase,
        )
}