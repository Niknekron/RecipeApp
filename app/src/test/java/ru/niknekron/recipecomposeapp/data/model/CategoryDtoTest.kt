package ru.niknekron.recipecomposeapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.toUiModel
import ru.niknekron.recipecomposeapp.fixtures.CategoryTestFixtures

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

    @Test
    fun `mapper maps empty title correctly`() {
        // Arrange
        val dto = CategoryTestFixtures.createCategoryDto(
            title = ""
        )

        // Act
        val result = dto.toUiModel()

        // Assert
        assertEquals("", result.title)
    }

    @Test
    fun `mapper preserves very long description`() {

        // Arrange
        val description = "Описание".repeat(100)

        val dto = CategoryTestFixtures.createCategoryDto(
            description = description
        )

        // Act
        val result = dto.toUiModel()

        // Assert
        assertEquals(description, result.description)
    }
}