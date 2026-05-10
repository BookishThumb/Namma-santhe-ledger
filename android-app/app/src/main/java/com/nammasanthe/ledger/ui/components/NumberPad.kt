package com.nammasanthe.ledger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material3.ripple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val subLetters = mapOf(
    "1" to "", "2" to "ABC", "3" to "DEF",
    "4" to "GHI", "5" to "JKL", "6" to "MNO",
    "7" to "PQRS", "8" to "TUV", "9" to "WXYZ", "0" to ""
)

private val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "del")

/** Google-dialer style circular number pad */
@Composable
fun NumberPad(
    onDigit: (String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        keys.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    when {
                        key.isEmpty() -> Spacer(Modifier.size(76.dp))
                        key == "del"  -> DeleteKey(onDelete)
                        else          -> DigitKey(key, subLetters[key] ?: "", onDigit)
                    }
                }
            }
        }
    }
}

@Composable
private fun DigitKey(digit: String, letters: String, onClick: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.05f))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, radius = 38.dp, color = Color.Black),
                role = Role.Button,
                onClick = { onClick(digit) }
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(
                text = digit,
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 32.sp
            )
            if (letters.isNotEmpty()) {
                Text(
                    text = letters,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    lineHeight = 11.sp
                )
            }
        }
    }
}

@Composable
private fun DeleteKey(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, radius = 38.dp, color = Color.Black),
                role = Role.Button,
                onClick = onClick
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Backspace,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
            modifier = Modifier.size(28.dp)
        )
    }
}
