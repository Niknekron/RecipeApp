package ru.niknekron.recipecomposeapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.niknekron.recipecomposeapp.data.database.converter.Converters
import ru.niknekron.recipecomposeapp.data.database.dao.CategoryDao
import ru.niknekron.recipecomposeapp.data.database.dao.RecipeDao
import ru.niknekron.recipecomposeapp.data.database.entity.CategoryEntity
import ru.niknekron.recipecomposeapp.data.database.entity.RecipeEntity

@Database(
    entities = [
        CategoryEntity::class,
        RecipeEntity::class,
               ],
    version = 2,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun recipeDao(): RecipeDao

    companion object {

        fun buildDatabase(
            context: Context,
        ): RecipesDatabase {
            return Room.databaseBuilder(
                context,
                RecipesDatabase::class.java,
                "recipes_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}