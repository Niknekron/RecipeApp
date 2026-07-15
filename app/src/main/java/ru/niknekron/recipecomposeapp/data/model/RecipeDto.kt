package ru.niknekron.recipecomposeapp.data.model

import ru.niknekron.recipecomposeapp.data.database.entity.RecipeEntity

data class RecipeDto(
    val id: Int,
    val title: String,
    val ingredients: List<IngredientDto>,
    val method: List<String>,
    val imageUrl: String,
    )

fun RecipeDto.toEntity(
    categoryId: Int
): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        categoryId = categoryId,
        imageUrl = imageUrl,
        ingredients = ingredients.map { ingredient ->
            "${ingredient.quantity} ${ingredient.unitOfMeasure} ${ingredient.description}"
        },
        method = method,
    )
}

fun RecipeEntity.toDto(): RecipeDto {
    return RecipeDto(
        id = id,
        title = title,
        ingredients = ingredients.map { ingredient ->
            IngredientDto(
                quantity = "",
                unitOfMeasure = "",
                description = ingredient
            )
        },
        method = method,
        imageUrl = imageUrl
    )
}