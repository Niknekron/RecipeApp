package ru.niknekron.recipecomposeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.niknekron.recipecomposeapp.data.database.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query(
        "SELECT * FROM categories ORDER BY name ASC"
    )
    fun getAllCategories(): Flow<List<CategoryEntity>>
}