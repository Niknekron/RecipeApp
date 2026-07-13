package ru.niknekron.recipecomposeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.niknekron.recipecomposeapp.data.database.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query(
        "SELECT * FROM categories ORDER BY name ASC"
    )
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(
        categories: List<CategoryEntity>
    )
}