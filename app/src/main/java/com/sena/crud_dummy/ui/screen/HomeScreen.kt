package com.sena.crud_dummy.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sena.crud_dummy.ui.composable.ConfirmDeleteDialog
import com.sena.crud_dummy.ui.composable.LoadingComponent
import com.sena.crud_dummy.ui.composable.ProductFormDialog
import com.sena.crud_dummy.ui.section.ProductListSection
import com.sena.crud_dummy.ui.section.ProductTopBarSection
import com.sena.crud_dummy.ui.viewModel.ProductViewModel

@Composable
fun HomeScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var productToDeleteId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessages()
        }
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            ProductTopBarSection(
                onRefresh = { viewModel.loadProducts() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddProductClick() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text(text = "+ Nuevo")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading && uiState.products.isEmpty()) {
                LoadingComponent()
            } else {
                ProductListSection(
                    products = uiState.products,
                    onEditClick = { product -> viewModel.onEditProductClick(product) },
                    onDeleteClick = { id -> productToDeleteId = id },
                    onRefresh = { viewModel.loadProducts() }
                )
            }

            if (uiState.isDialogVisible) {
                ProductFormDialog(
                    initialProduct = uiState.selectedProduct,
                    isSaving = uiState.isSaving,
                    onDismiss = { viewModel.onDismissDialog() },
                    onSave = { title, description, category, price ->
                        viewModel.saveProduct(title, description, category, price)
                    }
                )
            }

            productToDeleteId?.let { id ->
                ConfirmDeleteDialog(
                    onConfirm = {
                        viewModel.deleteProduct(id)
                        productToDeleteId = null
                    },
                    onDismiss = { productToDeleteId = null }
                )
            }
        }
    }
}
