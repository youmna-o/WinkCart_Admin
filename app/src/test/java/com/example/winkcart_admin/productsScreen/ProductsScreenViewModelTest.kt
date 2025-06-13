package com.example.winkcart_admin.productsScreen

import app.cash.turbine.test
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.model.Product
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {

    private lateinit var repo: ProductRepo
    private lateinit var viewModel: ProductsViewModel

    private val sampleProducts = listOf(
        Product(id = 1, title = "Test Product 1"),
        Product(id = 2, title = "Test Product 2")
    )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        repo = mockk(relaxed = true)
        coEvery { repo.getAllProducts() } returns flowOf(sampleProducts.toMutableList())
        viewModel = ProductsViewModel(repo)
    }

    @Test
    fun `fetchProducts emits Success with products`() = runTest {


        viewModel.filteredProducts.test {
            viewModel.fetchProducts()

            awaitItem() // Loading
            val finalState = awaitItem() // Success

            assertThat(finalState is ResponseStatus.Success, `is`(true))
            finalState as ResponseStatus.Success
            assertThat(finalState.result.size, `is`(2))
            assertThat(finalState.result[0].title, `is`("Test Product 1"))

        }
    }
    @Test
    fun `deleteProduct removes product from list`() = runTest {
        val initialProducts = sampleProducts.toMutableList()
        coEvery { repo.getAllProducts() } returns flowOf(initialProducts)
        coEvery { repo.deleteProduct(1) } returns Unit

        viewModel = ProductsViewModel(repo)
        advanceUntilIdle()

        viewModel.products.test {

            val initial = awaitItem() // Success
            assertThat(initial is ResponseStatus.Success, `is`(true))
            initial as ResponseStatus.Success
            assertThat(initial.result.size, `is`(2))

            viewModel.deleteProduct(1)
            advanceUntilIdle()

            awaitItem() // Loading
            val final = awaitItem() // Success

            assertThat(final is ResponseStatus.Success, `is`(true))
            final as ResponseStatus.Success
            assertThat(final.result.any { it.id == 1L}, `is`(false))
            assertThat(final.result.size, `is`(1))

        }
    }



}
