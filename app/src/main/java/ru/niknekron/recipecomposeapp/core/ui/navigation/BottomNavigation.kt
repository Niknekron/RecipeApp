package ru.niknekron.recipecomposeapp.core.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigation(
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    favoriteCount: Int = 0,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onCategoriesClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Categories",
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Button(
            onClick = onFavoriteClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {

            /** ИЗМЕНЕНИЕ: оборачиваем Text в BadgedBox */
            BadgedBox(
                badge = {

                    /** ИЗМЕНЕНИЕ: показываем badge только если count > 0 */
                    if (favoriteCount > 0) {
                        Badge {
                            Text(
                                text = favoriteCount.toString()
                            )
                        }
                    }
                }
            ) {

                /** ИЗМЕНЕНИЕ: сам текст Favorites теперь внутри BadgedBox */
                Text(
                    text = "Favorites",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}