package ru.niknekron.recipecomposeapp.features.details.presentation.model

import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

data class RecipeDetailUiState(
    val recipe: RecipeUiModel? = null,
    val portionsCount: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val ingredients: List<IngredientUiModel>
        get() = recipe?.ingredients.orEmpty()

    val scaledIngredients: List<IngredientUiModel>
        get() = ingredients.map { ingredient ->
            val quantityNumber = ingredient.quantity
                .replace(",",".")
                .toDoubleOrNull()

            if(quantityNumber != null) {
                ingredient.copy(
                    quantity = formatQuantity(quantityNumber * portionsCount)
                )
            } else {
                ingredient
            }
        }
    val hasRecipe: Boolean
        get() = recipe != null
}

private fun formatQuantity(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format("%.1f", value).replace(",",".")
    }
}