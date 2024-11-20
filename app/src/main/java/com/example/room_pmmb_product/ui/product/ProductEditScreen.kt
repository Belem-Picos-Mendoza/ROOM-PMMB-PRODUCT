/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.room_pmmb_product.ui.product

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.room_pmmb_product.ProductTopAppBar
import com.example.room_pmmb_product.R
import com.example.room_pmmb_product.ui.AppViewModelProvider
import com.example.room_pmmb_product.ui.navigation.NavigationDestination
import com.example.room_pmmb_product.ui.product.ProductEntryBody
import com.example.room_pmmb_product.ui.product.ProductEntryViewModel
import com.example.room_pmmb_product.ui.theme.ProductTheme
import kotlinx.coroutines.launch
import com.example.room_pmmb_product.ui.product.ProductUiState
import com.example.room_pmmb_product.ui.product.ProductDetails
import com.example.room_pmmb_product.ui.product.ProductEditViewModel

object ProductEditDestination : NavigationDestination {
    override val route = "product_edit"
    override val titleRes = R.string.edit_item_title
    const val productIdArg = "productId"
    val routeWithArgs = "$route/{$productIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    // Usamos Scaffold para la estructura de la pantalla
    Scaffold(
        topBar = {
            ProductTopAppBar(
                title = stringResource(ProductEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        // Contenido de la pantalla donde se editan los detalles del producto
        ProductEntryBody(
            productUiState = viewModel.productUiState,
            onProductValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveProduct()  // Guardamos el producto
                    navigateBack()  // Volvemos atrás después de guardar
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductEditScreenPreview() {
    ProductTheme {
        ProductEditScreen(navigateBack = { /* Do nothing */ }, onNavigateUp = { /* Do nothing */ })
    }
}
