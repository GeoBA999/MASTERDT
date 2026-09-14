package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MasterDTColorScheme = darkColorScheme(
  primary = AccentGold,
  onPrimary = Color(0xFF071410),
  secondary = AccentTeal,
  onSecondary = Color(0xFF071410),
  tertiary = FunctionalLime,
  onTertiary = Color(0xFF071410),
  background = DeepForestBg,
  onBackground = Color.White,
  surface = CardForestBg,
  onSurface = Color.White,
  surfaceVariant = DarkForestBg,
  onSurfaceVariant = TextMuted,
  error = FunctionalCoral,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = MasterDTColorScheme,
    typography = Typography,
    content = content
  )
}
