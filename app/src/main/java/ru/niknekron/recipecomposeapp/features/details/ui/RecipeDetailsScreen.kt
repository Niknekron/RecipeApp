package ru.niknekron.recipecomposeapp.features.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import ru.niknekron.recipecomposeapp.core.ui.ScreenHeader
import ru.niknekron.recipecomposeapp.features.details.presentation.model.RecipeDetailsViewModel
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import ru.niknekron.recipecomposeapp.features.theme.Dimens
import ru.niknekron.recipecomposeapp.utils.shareRecipe

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel,
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailsViewModel = viewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(recipe.id) {
        viewModel.initializeWithRecipe(recipe)
    }

    val currentRecipe = uiState.recipe ?: recipe

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            painter = rememberAsyncImagePainter(model = currentRecipe.imageUrl),
            contentDescription = currentRecipe.title,
            text = currentRecipe.title,
            showShareButton = true,
            onShareClick = {
                shareRecipe(
                    context = context,
                    recipeId = currentRecipe.id,
                    recipeTitle = currentRecipe.title
                )
            },
            showFavoriteButton = true,
            isFavorite = uiState.isFavorite,
            onFavoriteToggle = viewModel::toggleFavorite
        )

        Text(
            text = "Ingredients",
            modifier = Modifier.padding(Dimens.PaddingMedium),
            style = MaterialTheme.typography.titleMedium
        )

        uiState.scaledIngredients.forEachIndexed { index, ingredient ->
            IngredientItem(
                ingredient = ingredient
            )

            if (index < uiState.scaledIngredients.lastIndex) {
                HorizontalDivider()
            }
        }

        Text(
            text = "Инструкция",
            modifier = Modifier.padding(Dimens.PaddingMedium),
            style = MaterialTheme.typography.titleMedium
        )

        currentRecipe.method.forEachIndexed { index, step ->
            Text(
                text = "${index + 1}. $step",
                modifier = Modifier.padding(
                    start = Dimens.PaddingMedium,
                    end = Dimens.PaddingMedium,
                    bottom = Dimens.PaddingSmall
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}