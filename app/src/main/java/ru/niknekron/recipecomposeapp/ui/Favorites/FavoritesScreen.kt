package ru.niknekron.recipecomposeapp.ui.Favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.niknekron.recipecomposeapp.R
import ru.niknekron.recipecomposeapp.core.ui.ScreenHeader
import ru.niknekron.recipecomposeapp.ui.theme.Dimens
import ru.niknekron.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        ScreenHeader(
            painter = painterResource(id = R.drawable.bcg_favorites),
            contentDescription = "Favorites header image",
            text = "Favorites",
        )

        Text(
            text = "A list of featured recipes will appear here soon.",
            modifier = Modifier.padding(Dimens.PaddingMedium),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    RecipeComposeAppTheme {
        FavoritesScreen()
    }
}