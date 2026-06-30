package ru.niknekron.recipecomposeapp.features.categories.presentation.model

import androidx.compose.runtime.Immutable
import ru.niknekron.recipecomposeapp.data.model.CategoryDto
import ru.niknekron.recipecomposeapp.IMAGES_BASE_URL

@Immutable
data class CategoryUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
)

fun CategoryDto.toUiModel(): CategoryUiModel {
    return CategoryUiModel(
        id = id,
        title = title,
        description = description,
        imageUrl = if (imageUrl.startsWith("http")) {
            imageUrl
        } else {
            IMAGES_BASE_URL + imageUrl
        }
    )
}