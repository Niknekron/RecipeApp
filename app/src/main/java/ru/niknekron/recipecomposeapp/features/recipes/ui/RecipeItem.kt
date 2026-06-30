package ru.niknekron.recipecomposeapp.features.recipes.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import ru.niknekron.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import ru.niknekron.recipecomposeapp.features.theme.Dimens
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import ru.niknekron.recipecomposeapp.R
import ru.niknekron.recipecomposeapp.core.ui.RecipeImage

@Composable
fun RecipeItem(
    recipe: RecipeUiModel,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onClick(recipe.id) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.ElevationMedium
        )
    ) {
        Column {
            RecipeImage(
                imageUrl = recipe.imageUrl,
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(
                        RoundedCornerShape(
                            topStart = Dimens.CornerMedium,
                            topEnd = Dimens.CornerMedium
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Text(
                text = recipe.title,
                modifier = Modifier.padding(Dimens.PaddingMedium),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}