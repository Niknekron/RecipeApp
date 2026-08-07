package ru.niknekron.recipecomposeapp.features.categories.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.CategoriesViewModel
import ru.niknekron.recipecomposeapp.features.theme.Dimens
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.layout.Column

@Composable
fun CategoriesScreen(
    onCategoryClick: (Int, String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: CategoriesViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    CategoriesContent(
        uiState = uiState,
        onCategoryClick = onCategoryClick,
        modifier = modifier
    )
}

@Composable
fun CategoriesContent(
    uiState: CategoriesUiState,
    onCategoryClick: (Int, String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("categories_screen")
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag("loading_indicator")
                    )
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error.orEmpty(),
                        modifier = Modifier.testTag("error_message"),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(Dimens.PaddingMedium),
                    horizontalArrangement = Arrangement.spacedBy(
                        Dimens.PaddingMedium
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        Dimens.PaddingMedium
                    )
                ) {
                    items(
                        items = uiState.categories,
                        key = { it.id }
                    ) { category ->
                        CategoryItem(
                            category = category,
                            onClick = {
                                onCategoryClick(
                                    category.id,
                                    category.title,
                                    category.imageUrl
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}