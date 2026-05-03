package ru.niknekron.recipecomposeapp.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.rememberAsyncImagePainter
import ru.niknekron.recipecomposeapp.core.ui.ScreenHeader
import ru.niknekron.recipecomposeapp.ui.recipes.model.RecipeUiModel
import ru.niknekron.recipecomposeapp.ui.theme.Dimens
import androidx.compose.ui.platform.LocalContext
import ru.niknekron.recipecomposeapp.utils.shareRecipe
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            painter = rememberAsyncImagePainter(model = recipe.imageUrl),
            contentDescription = recipe.title,
            text = recipe.title,
            showShareButton = true,
            onShareClick = {
                shareRecipe(
                    context = context,
                    recipeId = recipe.id,
                    recipeTitle = recipe.title
                )
            }
        )

        Text(
            text = "Ingredients",
            modifier = Modifier.padding(Dimens.PaddingMedium),
            style = MaterialTheme.typography.titleMedium
        )

        recipe.ingredients.forEachIndexed { index, ingredient ->
            IngredientItem(
                ingredient = ingredient
            )

            if (index < recipe.ingredients.lastIndex) {
                HorizontalDivider()
            }
        }

        Text(
            text = "Инструкция",
            modifier = Modifier.padding(Dimens.PaddingMedium),
            style = MaterialTheme.typography.titleMedium
        )

        recipe.method.forEachIndexed { index, step ->
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