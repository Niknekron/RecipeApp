package ru.niknekron.recipecomposeapp.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import ru.niknekron.recipecomposeapp.ui.theme.Dimens

@Composable
fun ScreenHeader(
    painter: Painter,
    contentDescription: String,
    text: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.HeaderHeight)
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Surface(
            modifier = Modifier
                .align (Alignment.BottomStart)
                .padding(Dimens.PaddingMedium),
            shape = RoundedCornerShape(Dimens.CornerMedium),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(Dimens.PaddingMedium),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}