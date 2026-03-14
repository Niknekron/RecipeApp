package ru.niknekron.recipecomposeapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val RecipesAppDarkColorScheme= darkColorScheme(
    primary = PrimaryColorDark,
    onPrimary = TextPrimaryColorDark,

    secondary = AccentBlueDark,
    onSecondary = SurfaceColorDark,

    tertiary =  AccentColorDark,
    onTertiary = SurfaceColorDark,

    background = BackgroundColorDark,
    onBackground = TextPrimaryColorDark,

    surface = SurfaceColorDark,
    onSurfaceVariant = TextSecondaryColorDark,

    error = AccentColorDark,
    onError = TextPrimaryColorDark,

)

private val RecipesAppLightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = SurfaceColor,

    secondary = TextSecondaryColor,
    onSecondary = SurfaceColor,

    tertiary = AccentColor,
    onTertiary = SurfaceColor,

    background = BackgroundColor,
    onBackground = TextPrimaryColor,

    surface = SurfaceColor,
    onSurface = TextPrimaryColor,

    surfaceVariant = DividerColor,
    onSurfaceVariant = TextSecondaryColor,

    error = AccentColor,
    onError = SurfaceColor,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun RecipeComposeAppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = if(isSystemInDarkTheme()) {
        RecipesAppDarkColorScheme
    } else {
        RecipesAppLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = recipesAppTypography,
        content = content
    )
}