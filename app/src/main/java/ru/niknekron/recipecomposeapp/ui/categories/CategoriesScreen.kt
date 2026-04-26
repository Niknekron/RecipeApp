package ru.niknekron.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.niknekron.recipecomposeapp.R
import ru.niknekron.recipecomposeapp.core.ui.ScreenHeader
import ru.niknekron.recipecomposeapp.data.repository.getCategories
import ru.niknekron.recipecomposeapp.ui.recipes.model.toUiModel
import ru.niknekron.recipecomposeapp.ui.theme.Dimens
import ru.niknekron.recipecomposeapp.ui.theme.RecipeComposeAppTheme


@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (Int) -> Unit,
) {
    val categories = getCategories().map { it.toUiModel() }

    Column(
       modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        ScreenHeader(
            painter = painterResource(id = R.drawable.categories_image),
            contentDescription = "Categories header image",
            text = "Categories"
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Dimens.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
            verticalArrangement =  Arrangement.spacedBy(Dimens.PaddingMedium),
        ) {
            items(
                items = categories,
                key = { it.id }
            ) { category ->
                CategoryItem(
                    category = category,
                    onClick = { onCategoryClick(category.id)}
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    RecipeComposeAppTheme {
        CategoriesScreen(
            onCategoryClick = {}
        )
    }
}