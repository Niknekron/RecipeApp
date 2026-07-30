package ru.niknekron.recipecomposeapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.niknekron.recipecomposeapp.fixtures.RecipeTestFixtures
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.toUiModel

class RecipeDtoMapperTest {

    @Test
    fun `maps DTO to UI model correctly`() {
        //Arrange
        val recipeDto = RecipeTestFixtures.createRecipeDto(
            id = 1,
            title = "Паста карбонара",
        )

        //Act
        val result = recipeDto.toUiModel()

        //Assert
        assertEquals(1, result.id)
        assertEquals("Паста Карбонара", result.title)
        assertEquals(recipeDto.ingredients, result.ingredients)
        assertEquals(recipeDto.method, result.method)
    }

    @Test
    fun `prepends base url to relative imageUrl`() {
        //Arrange
        val recipeDto = RecipeTestFixtures.createRecipeDto(
            imageUrl = "pasta.jpg"
        )

        //Act
        val result = recipeDto.toUiModel()

        //Assert
        assertEquals(
            "https://recipes.androidsprint.ru/api/images/pasta.jpg",
            result.imageUrl
        )
    }

    @Test
    fun `preserves full imageUrl starting with http`() {

        //Arrange
        val recipeDto = RecipeTestFixtures.createRecipeDto(
            imageUrl = "https://site.com/image.jpg"
        )

        //Act
        val result = recipeDto.toUiModel()

        //Assert
        assertEquals(
            "https://site.com/image.jpg",
            result.imageUrl
        )
    }
}