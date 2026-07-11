package ru.niknekron.recipecomposeapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(

    @PrimaryKey
    val id: Int,
    val title: String,
    val categoryId: Int,
    val imageUrl: String,
    val ingredients: String,
    val method: String,
)
