package ru.niknekron.recipecomposeapp.app.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.niknekron.recipecomposeapp.BuildConfig
import ru.niknekron.recipecomposeapp.core.network.api.NetworkConfig
import ru.niknekron.recipecomposeapp.core.network.api.RecipesApiService
import ru.niknekron.recipecomposeapp.data.database.RecipesDatabase

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRecipesApiService(): RecipesApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()

        return retrofit.create(RecipesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipesDatabase(
        @ApplicationContext context: Context,
    ): RecipesDatabase {
        return RecipesDatabase.buildDatabase(context )
    }
}