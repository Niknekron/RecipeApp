package ru.niknekron.recipecomposeapp.ui.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.niknekron.recipecomposeapp.ui.recipes.model.RecipeUiModel
import ru.niknekron.recipecomposeapp.ui.theme.Dimens



@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel,
    modifier: Modifier = Modifier,
) {
    Text(
        text = recipe.title,
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.PaddingMedium),
        style = MaterialTheme.typography.headlineMedium
    )
}