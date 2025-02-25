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

package com.example.room_pmmb_product.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.room_pmmb_product.ui.home.HomeViewModel
import com.example.room_pmmb_product.ui.product.ProductDetailsViewModel
import com.example.room_pmmb_product.ui.product.ProductEditViewModel
import com.example.room_pmmb_product.ui.product.ProductEntryViewModel
import com.example.room_pmmb_product.ProductApplication

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            ProductEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.productRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            ProductEntryViewModel(inventoryApplication().container.productRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            ProductDetailsViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.productRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(inventoryApplication().container.productRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [ProductApplication].
 */
fun CreationExtras.inventoryApplication(): ProductApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ProductApplication)
