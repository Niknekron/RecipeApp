package ru.niknekron.recipecomposeapp.features.recipes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.compose.rememberAsyncImagePainter
import ru.niknekron.recipecomposeapp.core.ui.ScreenHeader
import ru.niknekron.recipecomposeapp.features.recipes.presentation.RecipesViewModel
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiState
import ru.niknekron.recipecomposeapp.features.theme.Dimens
import androidx.compose.ui.platform.testTag

@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    RecipesContent(
        uiState = uiState,
        onRecipeClick = onRecipeClick,
        modifier = modifier
    )
}
@Composable
fun RecipesContent(
    uiState: RecipeUiState,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag("loading_indicator")
                )
            }
        }

        uiState.error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error.orEmpty(),
                    modifier = Modifier.testTag("error_message"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(Dimens.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
            ) {
                item {
                    ScreenHeader(
                        painter = rememberAsyncImagePainter(model = uiState.categoryImageUrl),
                        contentDescription = uiState.categoryTitle,
                        text = uiState.categoryTitle
                    )
                }

                items(
                    items = uiState.recipes,
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
}
