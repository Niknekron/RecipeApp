package ru.niknekron.recipecomposeapp.ui.categories.model

import androidx.compose.runtime.Immutable
import ru.niknekron.recipecomposeapp.ASSETS_URI_PREFIX
import ru.niknekron.recipecomposeapp.data.model.RecipeDto

@Immutable
data class RecipeUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val method: List<String>,
    val isFavorite: Boolean,
)

fun RecipeDto.toUiModel(): RecipeUiModel {
    return RecipeUiModel(
        id = id,
        title = title,
        imageUrl = if (imageUrl.startsWith("http")) {
            imageUrl
        } else {
            ASSETS_URI_PREFIX + imageUrl
        },
        ingredients = ingredients.map { ingredient ->
            listOf(
                ingredient.quantity,
                ingredient.unitOfMeasure,
                ingredient.description,
            ).filter { it.isNotBlank() }.joinToString (" ")
        },
        method = method,
        isFavorite = false,
    )
}