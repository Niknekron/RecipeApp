package ru.niknekron.recipecomposeapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.toUiModel

class CategoryDtoTest  {
    @Test
    fun `converts DTO to UI model`() {
        //Arrange
        val categoryDto = CategoryDto(
            id = 1,
            title = "Завтраки",
            description = "Утренние блюда",
            imageUrl = "breakfast.jpg",
        )

        //Act
        val result = categoryDto.toUiModel()

        //Assert
        assertEquals(1, result.id)
        assertEquals("Завтраки", result.title)
        assertEquals("Утренние блюда", result.description)
        assertEquals(
            "https://recipes.androidsprint.ru/api/images/breakfast.jpg",
            result.imageUrl
        )
    }
}