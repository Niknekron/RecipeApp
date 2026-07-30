package ru.niknekron.recipecomposeapp.fixtures

import ru.niknekron.recipecomposeapp.data.model.IngredientDto
import ru.niknekron.recipecomposeapp.data.model.RecipeDto

object RecipeTestFixtures {
    fun createIngredientDto(
        quantity: String = "200",
        unitOfMeasure: String = "r",
        description: String = "Паста",
    ): IngredientDto {
        return IngredientDto(
            quantity = quantity,
            unitOfMeasure = unitOfMeasure,
            description = description,
        )
    }

    fun createRecipeDto(
        id: Int = 1,
        title: String = "Паста Карбонара",
        ingredients: List<IngredientDto> = listOf(
            createIngredientDto()
        ),
        method: List<String> = listOf(
            "Отварить пасту",
            "Слушать ингредиенты"
        ),
        imageUrl: String = "pasta.jpg",
    ): RecipeDto {
        return RecipeDto(
            id = id,
            title = title,
            ingredients = ingredients,
            method = method,
            imageUrl = imageUrl,
        )
    }

    fun createRecipeDtoList(
        count: Int = 3,
    ): List<RecipeDto> {
        return List(count) { index ->
            createRecipeDto(
                id = index + 1,
                title = "Рецепт ${index + 1}",
                imageUrl = "recipe_${index + 1}.jpg",
            )
        }
    }
}