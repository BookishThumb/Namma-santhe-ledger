package com.nammasanthe.ledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nammasanthe.ledger.navigation.NavGraph
import com.nammasanthe.ledger.ui.theme.NammaSantheLedgerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NammaSantheLedgerTheme {
                NavGraph()
            }
        }
    }
}
