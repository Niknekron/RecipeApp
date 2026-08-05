package ru.niknekron.recipecomposeapp.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.niknekron.recipecomposeapp.data.database.RecipesDatabase
import ru.niknekron.recipecomposeapp.data.database.entity.CategoryEntity
import ru.niknekron.recipecomposeapp.data.database.entity.RecipeEntity

@RunWith(AndroidJUnit4::class)
class RecipesDaoTest {

    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var recipeDao: RecipeDao

    @Before
    fun setUp() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            RecipesDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        categoryDao = database.categoryDao()
        recipeDao = database.recipeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertsAndRetrievesCategories() = runTest {
        // Arrange
        val categories = listOf(
            CategoryEntity(
                id = 1,
                name = "Завтраки",
                description = "Утренние блюда",
                imageUrl = "breakfast.jpg"
            ),
            CategoryEntity(
                id = 2,
                name = "Обеды",
                description = "Основные блюда",
                imageUrl = "lunch.jpg"
            )
        )

        // Act
        categoryDao.insertCategories(categories)

        val result = categoryDao
            .getAllCategories()
            .first()

        // Assert
        assertEquals(categories.size, result.size)
        assertEquals(categories.first().id, result.first().id)
        assertEquals(categories.first().name, result.first().name)
    }

    @Test
    fun insertReplacesDuplicateCategory() = runTest {
        // Arrange
        val originalCategory = CategoryEntity(
            id = 1,
            name = "Завтраки",
            description = "Старое описание",
            imageUrl = "old.jpg"
        )

        val updatedCategory = CategoryEntity(
            id = 1,
            name = "Новые завтраки",
            description = "Новое описание",
            imageUrl = "new.jpg"
        )

        // Act
        categoryDao.insertCategories(
            listOf(originalCategory)
        )

        categoryDao.insertCategories(
            listOf(updatedCategory)
        )

        val result = categoryDao
            .getAllCategories()
            .first()

        // Assert
        assertEquals(1, result.size)
        assertEquals(updatedCategory.id, result.first().id)
        assertEquals(updatedCategory.name, result.first().name)
        assertEquals(
            updatedCategory.description,
            result.first().description
        )
        assertEquals(
            updatedCategory.imageUrl,
            result.first().imageUrl
        )
    }

    @Test
    fun getRecipesByCategoryReturnsCorrectItems() = runTest {
        // Arrange
        val recipes = listOf(
            RecipeEntity(
                id = 1,
                title = "Омлет",
                categoryId = 1,
                imageUrl = "omelette.jpg",
                ingredients = listOf("2|||шт|||Яйца"),
                method = listOf("Взбить яйца")
            ),
            RecipeEntity(
                id = 2,
                title = "Каша",
                categoryId = 1,
                imageUrl = "porridge.jpg",
                ingredients = listOf("100|||г|||Крупа"),
                method = listOf("Сварить кашу")
            ),
            RecipeEntity(
                id = 3,
                title = "Суп",
                categoryId = 2,
                imageUrl = "soup.jpg",
                ingredients = listOf("1|||л|||Вода"),
                method = listOf("Сварить суп")
            )
        )

        // Act
        recipeDao.insertRecipes(recipes)

        val result = recipeDao
            .getRecipesByCategory(categoryId = 1)
            .first()

        // Assert
        assertEquals(2, result.size)
        assertTrue(
            result.all { recipe ->
                recipe.categoryId == 1
            }
        )
        assertEquals(
            setOf(1, 2),
            result.map { recipe -> recipe.id }.toSet()
        )
    }

    @Test
    fun emptyDatabaseReturnsEmptyList() = runTest {
        // Act
        val result = categoryDao
            .getAllCategories()
            .first()

        // Assert
        assertTrue(result.isEmpty())
    }
}