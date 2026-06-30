package ru.niknekron.recipecomposeapp.features.recipes.presentation.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.niknekron.recipecomposeapp.ASSETS_URI_PREFIX
import ru.niknekron.recipecomposeapp.data.model.RecipeDto
import ru.niknekron.recipecomposeapp.IMAGES_BASE_URL

@Parcelize
@Immutable
data class RecipeUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val ingredients: List<IngredientUiModel>,
    val method: List<String>,
    val isFavorite: Boolean,
) : Parcelable

fun RecipeDto.toUiModel(): RecipeUiModel {
    return RecipeUiModel(
        id = id,
        title = title,
        imageUrl = if (imageUrl.startsWith("http")) {
            imageUrl
        } else {
            IMAGES_BASE_URL + imageUrl
        },
        ingredients = ingredients.map { it.toUiModel() },
        method = method,
        isFavorite = false
    )
}