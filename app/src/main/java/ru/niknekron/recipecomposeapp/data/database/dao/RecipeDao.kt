package ru.niknekron.recipecomposeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.niknekron.recipecomposeapp.data.database.entity.RecipeEntity


@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>
}