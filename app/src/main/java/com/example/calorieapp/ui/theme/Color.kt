package com.example.calorieapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val primaryLight = Color(0xFF2B6A46)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFAFF1C3)
val onPrimaryContainerLight = Color(0xFF00210F)
val secondaryLight = Color(0xFF4F6354)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFD1E8D5)
val onSecondaryContainerLight = Color(0xFF0C1F13)
val tertiaryLight = Color(0xFF3B6470)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFBEEAF7)
val onTertiaryContainerLight = Color(0xFF001F26)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFF6FBF3)
val onBackgroundLight = Color(0xFF181D19)
val surfaceLight = Color(0xFFF6FBF3)
val onSurfaceLight = Color(0xFF181D19)
val surfaceVariantLight = Color(0xFFDCE5DB)
val onSurfaceVariantLight = Color(0xFF414942)
val outlineLight = Color(0xFF717971)
val outlineVariantLight = Color(0xFFC0C9C0)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2C322D)
val inverseOnSurfaceLight = Color(0xFFEDF2EB)
val inversePrimaryLight = Color(0xFF94D5A9)
val surfaceDimLight = Color(0xFFD6DBD4)
val surfaceBrightLight = Color(0xFFF6FBF3)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF0F5EE)
val surfaceContainerLight = Color(0xFFEAEFE8)
val surfaceContainerHighLight = Color(0xFFE5EAE2)
val surfaceContainerHighestLight = Color(0xFFDFE4DD)

val primaryDark = Color(0xFF94D5A9)
val onPrimaryDark = Color(0xFF00391E)
val primaryContainerDark = Color(0xFF0D5130)
val onPrimaryContainerDark = Color(0xFFAFF1C3)
val secondaryDark = Color(0xFFB5CCB9)
val onSecondaryDark = Color(0xFF213527)
val secondaryContainerDark = Color(0xFF374B3D)
val onSecondaryContainerDark = Color(0xFFD1E8D5)
val tertiaryDark = Color(0xFFA3CDDB)
val onTertiaryDark = Color(0xFF033640)
val tertiaryContainerDark = Color(0xFF214C58)
val onTertiaryContainerDark = Color(0xFFBEEAF7)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF0F1511)
val onBackgroundDark = Color(0xFFDFE4DD)
val surfaceDark = Color(0xFF0F1511)
val onSurfaceDark = Color(0xFFDFE4DD)
val surfaceVariantDark = Color(0xFF414942)
val onSurfaceVariantDark = Color(0xFFC0C9C0)
val outlineDark = Color(0xFF8A938B)
val outlineVariantDark = Color(0xFF414942)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFDFE4DD)
val inverseOnSurfaceDark = Color(0xFF2C322D)
val inversePrimaryDark = Color(0xFF2B6A46)
val surfaceDimDark = Color(0xFF0F1511)
val surfaceBrightDark = Color(0xFF353B36)
val surfaceContainerLowestDark = Color(0xFF0A0F0C)
val surfaceContainerLowDark = Color(0xFF181D19)
val surfaceContainerDark = Color(0xFF1C211D)
val surfaceContainerHighDark = Color(0xFF262B27)
val surfaceContainerHighestDark = Color(0xFF313631)

val ColorScheme.focusedTextFieldText
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

val ColorScheme.unfocusedTextFieldText
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF94A3B8) else Color(0xFF475569)

val ColorScheme.textFieldContainer
    @Composable
    get() = if (isSystemInDarkTheme()) primaryDark.copy(alpha = 0.6f) else primaryLight

