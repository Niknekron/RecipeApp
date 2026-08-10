package ru.niknekron.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.niknekron.recipecomposeapp.PARAM_CATEGORY_ID
import ru.niknekron.recipecomposeapp.PARAM_CATEGORY_IMAGE_URL
import ru.niknekron.recipecomposeapp.PARAM_CATEGORY_TITLE
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.fixtures.RecipeTestFixtures

@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewModelTest {

    private val repository: RecipesRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun createViewModel(
        categoryId: Int = 1,
        categoryTitle: String = "Завтраки",
        categoryImageUrl: String = "breakfast.jpg",
    ): RecipesViewModel {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                PARAM_CATEGORY_ID to categoryId,
                PARAM_CATEGORY_TITLE to categoryTitle,
                PARAM_CATEGORY_IMAGE_URL to categoryImageUrl
            )
        )

        return RecipesViewModel(
            savedStateHandle = savedStateHandle,
            repository = repository
        )
    }

    @Test
    fun `loads recipes for category`() = runTest {
        // Arrange
        val categoryId = 1
        val recipes = RecipeTestFixtures.createRecipeDtoList(
            count = 3
        )

        every {
            repository.getRecipesByCategory(categoryId)
        } returns flowOf(recipes)

        // Act
        val viewModel = createViewModel(
            categoryId = categoryId
        )

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals(recipes.size, state.recipes.size)
            assertEquals(recipes.first().id, state.recipes.first().id)
            assertEquals(
                recipes.first().title,
                state.recipes.first().title
            )
            assertFalse(state.isLoading)
            assertEquals(null, state.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state reflects category title from savedState`() = runTest {
        // Arrange
        val categoryTitle = "Завтраки"

        every {
            repository.getRecipesByCategory(1)
        } returns flowOf(emptyList())

        // Act
        val viewModel = createViewModel(
            categoryId = 1,
            categoryTitle = categoryTitle
        )

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals(categoryTitle, state.categoryTitle)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows error when repository throws`() = runTest {
        // Arrange
        every {
            repository.getRecipesByCategory(1)
        } returns flow {
            throw IOException("Network error")
        }

        // Act
        val viewModel = createViewModel(
            categoryId = 1
        )

        // Assert
        viewModel.uiState.test {
            val state = awaitItem()

            assertFalse(state.isLoading)
            assertNotNull(state.error)
            assertTrue(state.recipes.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }
}