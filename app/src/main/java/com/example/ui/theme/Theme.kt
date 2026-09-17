package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFFFF898A),
  onPrimary = FyaRedDeep,
  primaryContainer = FyaRedDark,
  onPrimaryContainer = Color(0xFFFFDAD9),
  secondary = Color(0xFF94A3B8),
  onSecondary = Slate900,
  secondaryContainer = Slate700,
  onSecondaryContainer = Slate200,
  tertiary = AccentGold,
  onTertiary = Color.Black,
  background = Slate900,
  onBackground = Slate100,
  surface = Slate800,
  onSurface = Slate100,
  surfaceVariant = Slate700,
  onSurfaceVariant = Slate300,
  outline = Slate600,
  outlineVariant = Slate700
)

private val LightColorScheme = lightColorScheme(
  primary = FyaRed,
  onPrimary = Color.White,
  primaryContainer = FyaRedContainer,
  onPrimaryContainer = FyaOnRedContainer,
  secondary = Slate600,
  onSecondary = Color.White,
  secondaryContainer = Slate100,
  onSecondaryContainer = Slate800,
  tertiary = AccentGold,
  onTertiary = Color.White,
  background = SoftOffWhite,
  onBackground = TextPrimaryLight,
  surface = CardLightBg,
  onSurface = TextPrimaryLight,
  surfaceVariant = Slate100,
  onSurfaceVariant = TextSecondaryLight,
  outline = Slate200,
  outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun LexicoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  LexicoTheme(darkTheme = darkTheme, content = content)
}


