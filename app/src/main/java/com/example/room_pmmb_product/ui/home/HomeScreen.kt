package com.example.room_pmmb_product.ui.home


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.room_pmmb_product.R
import com.example.room_pmmb_product.Datos.Product
import com.example.room_pmmb_product.ProductTopAppBar
import com.example.room_pmmb_product.ui.AppViewModelProvider
import com.example.room_pmmb_product.ui.product.formattedPrice
import com.example.room_pmmb_product.ui.navigation.NavigationDestination
import com.example.room_pmmb_product.ui.theme.ProductTheme


/**
 * Entry route for Product screen
 */
object ProductDestination : NavigationDestination {
    override val route = "product"
    override val titleRes = R.string.product_title // Actualizamos el título para la pantalla de productos
}


@Composable
private fun ProductList(
    productList: List<Product>,
    onProductClick: (Int) -> Unit,  // Cambié el tipo de (Product) -> Unit a (Int) -> Unit
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = productList, key = { it.id }) { product ->
            ProductItem(
                product = product,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onProductClick(product.id) } // Ahora pasamos solo el id
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navigateToProductEntry: () -> Unit,
    navigateToProductUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ProductTopAppBar(
                title = stringResource(ProductDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToProductEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        },
    ) { innerPadding ->
        ProductBody(
            productList = homeUiState.ProductList,
            onProductClick = navigateToProductUpdate,  // Aquí pasa el id
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun ProductBody(
    productList: List<Product>,
    onProductClick: (Int) -> Unit,  // Cambié el tipo a (Int) -> Unit
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {

        if (productList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_product_description), // Texto actualizado para productos
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            ProductList(
                productList = productList,
                onProductClick = { onProductClick(it) },  // Pasamos solo el id
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun ProductItem(
    product: Product, modifier: Modifier = Modifier
) {
    // Reducir el tamaño de la tarjeta
    Card(
        modifier = modifier
            .fillMaxWidth(1f) // Ocupar todo el ancho disponible
            .padding(dimensionResource(id = R.dimen.padding_small)), // Añadimos un pequeño margen
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Mantener una pequeña sombra
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
            horizontalAlignment = Alignment.Start // Alineamos todos los elementos a la izquierda
        ) {

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                maxLines = 1,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)),
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.product_id, product.id),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start
            )
            // Precio del producto
            Text(
                text = stringResource(R.string.tit_price, product.price.toString()),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )


            // Fila para la categoría
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(R.string.product_category, product.category),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                text = stringResource(R.string.product_description_req, product.description),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start // Alineación a la izquierda
            )


            // Cantidad disponible (alineada a la izquierda)
            Text(
                text = stringResource(R.string.disponible, product.quantity),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start // Alineación a la izquierda
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductBodyPreview() {
    ProductTheme {
        ProductBody(listOf(
            Product(1, "Juego", "Descripción del juego", "Categoría 1", 100.0, 20f),
            Product(2, "Juego", "Descripción del juego", "Categoría 1", 100.0, 20f),
            Product(3, "Juego", "Descripción del juego", "Categoría 1", 100.0, 20f)
        ), onProductClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ProductBodyEmptyListPreview() {
    ProductTheme {
        ProductBody(listOf(), onProductClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    ProductTheme {
        ProductItem(
            Product(1, "Juego", "Descripción del juego", "Categoría 1", 100.0, 20f)
        )
    }
}