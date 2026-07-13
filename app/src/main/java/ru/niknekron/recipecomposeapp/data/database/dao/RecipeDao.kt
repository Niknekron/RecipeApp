package ru.niknekron.recipecomposeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.niknekron.recipecomposeapp.data.database.entity.RecipeEntity

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(
        recipes: List<RecipeEntity>
    )

    @Query(
        "SELECT * FROM recipes WHERE categoryId = :categoryId"
    )
    fun getRecipesByCategory(
        categoryId: Int
    ): Flow<List<RecipeEntity>>

    @Query(
        "SELECT * FROM recipes WHERE id = :recipeId LIMIT 1"
    )
    fun getRecipeById(
        recipeId: Int
    ): Flow<RecipeEntity?>

    @Query(
        "SELECT * FROM recipes WHERE id IN (:recipeIds)"
    )
    fun getRecipesByIds(
        recipeIds: List<Int>
    ): Flow<List<RecipeEntity>>
}