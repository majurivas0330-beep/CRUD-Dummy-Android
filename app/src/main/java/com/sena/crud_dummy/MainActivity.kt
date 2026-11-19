package com.sena.crud_dummy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sena.crud_dummy.ui.screen.HomeScreen
import com.sena.crud_dummy.ui.theme.CRUDDummyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CRUDDummyTheme {
                HomeScreen()
            }
        }
    }
}

