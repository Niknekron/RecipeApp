package ru.niknekron.recipecomposeapp.ui.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.niknekron.recipecomposeapp.R
import ru.niknekron.recipecomposeapp.core.ui.ScreenHeader
import ru.niknekron.recipecomposeapp.data.repository.getRecipesByCategoryId
import ru.niknekron.recipecomposeapp.ui.recipes.model.toUiModel
import ru.niknekron.recipecomposeapp.ui.recipes.model.RecipeUiModel
import ru.niknekron.recipecomposeapp.ui.theme.Dimens

@Composable
fun RecipesScreen(
    categoryId: Int,
    categoryTitle: String,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var recipes by remember { mutableStateOf<List<RecipeUiModel>>(emptyList()) }

    LaunchedEffect(categoryId) {
        recipes = getRecipesByCategoryId(categoryId).map { it.toUiModel() }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ScreenHeader(
            painter = painterResource(id = R.drawable.categories_image),
            contentDescription = "Recipes header image",
            text = categoryTitle
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Dimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
        ) {
            items(
                items = recipes,
                key = { it.id }
            ) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onClick = onRecipeClick
                )
            }
        }
    }
}