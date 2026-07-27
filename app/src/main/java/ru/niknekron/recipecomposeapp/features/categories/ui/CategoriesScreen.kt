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

@Composable
fun CategoriesScreen(
    onCategoryClick: (Int, String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: CategoriesViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
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
                    key = { category ->
                        category.id
                    }
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