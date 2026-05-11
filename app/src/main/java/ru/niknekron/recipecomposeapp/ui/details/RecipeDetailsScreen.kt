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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import ru.niknekron.recipecomposeapp.util.FavoritePrefsManager
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel,
    modifier: Modifier = Modifier,
) {

    var portionsCount by rememberSaveable {
        mutableStateOf(1)
    }

    val scaledIngredients = remember(recipe.ingredients, portionsCount) {
        recipe.ingredients.map { ingredient ->
            val quantityNumber = ingredient.quantity.replace(",",".").toDoubleOrNull()

            if (quantityNumber != null) {
                ingredient.copy (
                    quantity = formatQuantity(quantityNumber * portionsCount)
                )
            } else {
                ingredient
            }
        }
    }

    val context = LocalContext.current

    val favoritePrefsManager = remember {
        FavoritePrefsManager(context)
    }

    var isFavorite by remember(recipe.id) {
        mutableStateOf(
            favoritePrefsManager.isFavorite(recipe.id)
        )
    }
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
            },
            showFavoriteButton = true,
            isFavorite = isFavorite,
            onFavoriteToggle = {
                if (isFavorite) {
                    favoritePrefsManager.removeFromFavorites(recipe.id)
                } else {
                    favoritePrefsManager.addToFavorites(recipe.id)
                }

                isFavorite = !isFavorite
            }
        )

        Text(
            text = "Ingredients",
            modifier = Modifier.padding(Dimens.PaddingMedium),
            style = MaterialTheme.typography.titleMedium
        )

        scaledIngredients.forEachIndexed { index, ingredient ->
            IngredientItem(
                ingredient = ingredient
            )

            if (index < scaledIngredients.lastIndex) {
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
private fun formatQuantity(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format("%.1f", value).replace(",", ".")
    }
}