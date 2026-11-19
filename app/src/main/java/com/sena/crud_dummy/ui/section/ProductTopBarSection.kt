package com.sena.crud_dummy.ui.section

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTopBarSection(
    onRefresh: () -> Unit
) {
    TopAppBar(
        title = { Text("Gestión de Productos") },
        actions = {
            TextButton(onClick = onRefresh) {
                Text("Recargar")
            }
        }
    )
}
