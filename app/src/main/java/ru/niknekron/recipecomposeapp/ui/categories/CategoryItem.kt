package ru.niknekron.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import ru.niknekron.recipecomposeapp.ui.categories.model.CategoryUiModel
import ru.niknekron.recipecomposeapp.ui.theme.Dimens

@Composable
fun CategoryItem(
    category: CategoryUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SubcomposeAsyncImage(
                model = category.imageUrl,
                contentDescription = category.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(topStart = Dimens.CornerMedium, topEnd = Dimens.CornerMedium)),
                contentScale = ContentScale.Crop,
                loading = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                error = {
                    Text(
                        text = "Image error",
                        modifier = Modifier.padding(Dimens.PaddingMedium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingMedium)
            ) {
                Text(
                    text = category.title.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = category.description,
                    modifier = Modifier.padding(top = Dimens.PaddingSmall),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}