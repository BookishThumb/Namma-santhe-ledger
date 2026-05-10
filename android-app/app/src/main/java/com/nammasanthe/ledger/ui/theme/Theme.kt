package com.nammasanthe.ledger.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = VendorPrimary,
    onPrimary = VendorOnPrimary,
    primaryContainer = VendorLight,
    onPrimaryContainer = VendorDark,
    secondary = CustomerPrimary,
    onSecondary = CustomerOnPrimary,
    secondaryContainer = CustomerLight,
    onSecondaryContainer = CustomerDark,
    background = Background,
    surface = Surface,
    onBackground = OnSurface,
    onSurface = OnSurface,
    error = CreditRed,
)

@Composable
fun NammaSantheLedgerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = NammaTypography,
        content = content
    )
}
