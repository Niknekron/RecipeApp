package ru.niknekron.recipecomposeapp.ui.categories.model

import androidx.compose.runtime.Immutable
import ru.niknekron.recipecomposeapp.ASSETS_URI_PREFIX
import ru.niknekron.recipecomposeapp.data.model.CategoryDto

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
            ASSETS_URI_PREFIX + imageUrl
        }
    )
}