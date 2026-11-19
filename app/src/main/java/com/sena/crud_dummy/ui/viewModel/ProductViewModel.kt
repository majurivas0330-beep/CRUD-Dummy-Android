package com.sena.crud_dummy.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sena.crud_dummy.domain.model.ProductModel
import com.sena.crud_dummy.domain.useCase.CreateProductUseCase
import com.sena.crud_dummy.domain.useCase.DeleteProductUseCase
import com.sena.crud_dummy.domain.useCase.GetProductUseCase
import com.sena.crud_dummy.domain.useCase.UpdateProductUseCase
import com.sena.crud_dummy.ui.state.ProductUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUIState())
    val uiState: StateFlow<ProductUIState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { getProductUseCase() }
                .onSuccess { list ->
                    _uiState.update { it.copy(isLoading = false, products = list) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "No se pudieron cargar los productos"
                        )
                    }
                }
        }
    }

    fun onAddProductClick() {
        _uiState.update {
            it.copy(selectedProduct = null, isDialogVisible = true)
        }
    }

    fun onEditProductClick(product: ProductModel) {
        _uiState.update {
            it.copy(selectedProduct = product, isDialogVisible = true)
        }
    }

    fun onDismissDialog() {
        _uiState.update {
            it.copy(isDialogVisible = false, selectedProduct = null)
        }
    }

    fun saveProduct(title: String, description: String, category: String, price: Double) {
        val currentSelected = _uiState.value.selectedProduct
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            runCatching {
                if (currentSelected == null) {
                    val newProduct = ProductModel(
                        id = 0,
                        title = title,
                        description = description,
                        category = category,
                        price = price
                    )
                    createProductUseCase(newProduct)
                } else {
                    val updatedProduct = currentSelected.copy(
                        title = title,
                        description = description,
                        category = category,
                        price = price
                    )
                    updateProductUseCase(updatedProduct)
                }
            }.onSuccess { saved ->
                _uiState.update { state ->
                    val updatedList = if (currentSelected == null) {
                        listOf(saved) + state.products
                    } else {
                        state.products.map { if (it.id == saved.id) saved else it }
                    }
                    state.copy(
                        isSaving = false,
                        isDialogVisible = false,
                        selectedProduct = null,
                        products = updatedList,
                        successMessage = if (currentSelected == null) "Producto creado exitosamente" else "Producto actualizado exitosamente"
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Error al guardar el producto"
                    )
                }
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { deleteProductUseCase(id) }
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            products = state.products.filterNot { it.id == id },
                            successMessage = "Producto eliminado exitosamente"
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Error al eliminar el producto"
                        )
                    }
                }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
