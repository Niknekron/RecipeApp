package ru.niknekron.recipecomposeapp.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.flow.map
import ru.niknekron.recipecomposeapp.R
import ru.niknekron.recipecomposeapp.core.ui.ScreenHeader
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepositoryStub
import ru.niknekron.recipecomposeapp.ui.recipes.RecipeItem
import ru.niknekron.recipecomposeapp.ui.recipes.model.toUiModel
import ru.niknekron.recipecomposeapp.ui.theme.Dimens
import ru.niknekron.recipecomposeapp.util.FavoriteDataStoreManager
import androidx.compose.runtime.remember

@Composable
fun FavoritesScreen(
    recipesRepository: RecipesRepositoryStub,
    favoriteDataStoreManager: FavoriteDataStoreManager,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val favoriteRecipesFlow = remember(
        favoriteDataStoreManager,
        recipesRepository
    ) {
        favoriteDataStoreManager
            .getFavoriteIdsFlow()
            .map { ids ->

                ids.mapNotNull { id ->

                    id.toIntOrNull()?.let { recipeId ->

                        recipesRepository
                            .getRecipeById(recipeId)
                            ?.toUiModel()
                    }
                }
            }
    }

    val favoriteRecipes by favoriteRecipesFlow.collectAsState(
        initial = emptyList()
    )

    if (favoriteRecipes.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "У вас пока нет избранных рецептов",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(Dimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
        ) {
            items(
                items = favoriteRecipes,
                key = { it.id }
            ) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onClick = { recipeId ->
                        onRecipeClick(recipeId)
                    }
                )
            }
        }
    }
}