package ru.niknekron.recipecomposeapp.features.categories.presentation

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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.CategoriesViewModel
import ru.niknekron.recipecomposeapp.fixtures.CategoryTestFixtures

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

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

    @Test
    fun `loads categories from repository`() = runTest {
        //Arrange
        val categories =
            CategoryTestFixtures.createCategoryDtoList(
                count = 3
            )

        every {
            repository.getCategories()
        } returns flowOf(categories)

        val viewModel = CategoriesViewModel(
            repository = repository
        )

        //Act + Assert
        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals(categories.size, state.categories.size)
            assertFalse(state.isLoading)
            assertNull(state.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows empty list when repository returns data`() = runTest {
        //Arrange
        every{
            repository.getCategories()
        } returns flowOf(emptyList())

        val viewModel = CategoriesViewModel(
            repository = repository
        )

        //Act + Assert
        viewModel.uiState.test {
            val state = awaitItem()

            assertTrue(state.categories.isEmpty())
            assertFalse(state.isLoading)
            assertNull(state.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `show error when repository throws`() = runTest {
        //Arrange
        every{
            repository.getCategories()
        }returns flow {
            throw IOException("Network error")
        }

        val viewModel = CategoriesViewModel(
            repository = repository
        )

        //Act + Assert
        viewModel.uiState.test {
            val state = awaitItem()

            assertFalse(state.isLoading)
            assertTrue(state.error != null)

            cancelAndIgnoreRemainingEvents()
        }
    }
}