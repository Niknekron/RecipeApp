package ru.niknekron.recipecomposeapp.ui.categories.model

import androidx.compose.runtime.Immutable
import ru.niknekron.recipecomposeapp.data.model.IngredientDto

@Immutable
data class IngredientUiModel(
    val name: String,
    val quantity: String,
    val unitOfMeasure: String,
)

fun IngredientDto.toUiModel(): IngredientUiModel {
    return IngredientUiModel(
        name = description,
        quantity = quantity,
        unitOfMeasure = unitOfMeasure,
    )
}