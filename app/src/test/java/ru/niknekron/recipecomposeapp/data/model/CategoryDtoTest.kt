package ru.niknekron.recipecomposeapp.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.toUiModel
import ru.niknekron.recipecomposeapp.fixtures.CategoryTestFixtures

class CategoryDtoTest {

    @Test
    fun `converts DTO to UI model`() {
        // Arrange
        val categoryDto = CategoryTestFixtures.createCategoryDto()

        // Act
        val result = categoryDto.toUiModel()

        // Assert
        assertEquals(categoryDto.id, result.id)
        assertEquals(categoryDto.title, result.title)
        assertEquals(categoryDto.description, result.description)
        assertEquals(
            "https://recipes.androidsprint.ru/api/images/${categoryDto.imageUrl}",
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
        assertEquals(dto.title, result.title)
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
        assertEquals(dto.description, result.description)
    }
}