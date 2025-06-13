package com.example.winkcart_admin.data.repo

import com.example.winkcart_admin.data.remote.FakeRemoteDataSource
import com.example.winkcart_admin.data.remote.RemoteDataSource
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.data.repository.ProductRepoImpl
import com.example.winkcart_admin.model.Image
import com.example.winkcart_admin.model.Option
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.model.Variant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class ProductRepoTest {
    lateinit var remoteDataSource: RemoteDataSource
    lateinit var repo:ProductRepo

    val remoteProductList= listOf(
        Product(
            id = 1001,
            title = "Classic T-Shirt",
            body_html = "<strong>Classic fit with soft cotton</strong>",
            vendor = "Acme Clothing",
            product_type = "T-Shirts",
            created_at = "2025-05-01T10:00:00Z",
            handle = "classic-tshirt",
            updated_at = "2025-06-01T12:00:00Z",
            published_at = "2025-06-01T13:00:00Z",
            tags = "tshirt, cotton, unisex",
            status = "active",
            variants = listOf(
                Variant(
                    id = 2001,
                    product_id = 1001,
                    title = "Small / Red",
                    price = "19.99",
                    position = 1,
                    inventory_policy = "deny",
                    compare_at_price = "24.99",
                    option1 = "Small",
                    option2 = "Red",
                    option3 = null,
                    sku = "TSHIRT-RED-S",
                    inventory_quantity = 50,
                    inventory_item_id = 3001
                ),
                Variant(
                    id = 2002,
                    product_id = 1001,
                    title = "Medium / Red",
                    price = "19.99",
                    position = 2,
                    inventory_policy = "deny",
                    compare_at_price = "24.99",
                    option1 = "Medium",
                    option2 = "Red",
                    option3 = null,
                    sku = "TSHIRT-RED-M",
                    inventory_quantity = 30,
                    inventory_item_id = 3002
                )
            ),
            options = listOf(
                Option(
                    id = 4001,
                    product_id = 1001,
                    name = "Size",
                    values = listOf("Small", "Medium", "Large")
                ),
                Option(
                    id = 4002,
                    product_id = 1001,
                    name = "Color",
                    values = listOf("Red", "Blue")
                )
            ),
            images = listOf(
                Image(
                    id = 5001,
                    product_id = 1001,
                    src = "https://example.com/images/tshirt-red.jpg"
                ),
                Image(
                    id = 5002,
                    product_id = 1001,
                    src = "https://example.com/images/tshirt-blue.jpg"
                )
            )
        ),
        Product(
            id = 1002,
            title = "Leather Wallet",
            body_html = "<em>Handmade from genuine leather</em>",
            vendor = "Urban Gear",
            product_type = "Accessories",
            created_at = "2025-03-15T11:20:00Z",
            handle = "leather-wallet",
            updated_at = "2025-06-10T08:30:00Z",
            published_at = "2025-06-11T09:00:00Z",
            tags = "wallet, leather, handmade",
            status = "active",
            variants = listOf(
                Variant(
                    id = 2010,
                    product_id = 1002,
                    title = "One Size / Brown",
                    price = "49.99",
                    position = 1,
                    inventory_policy = "continue",
                    compare_at_price = null,
                    option1 = "One Size",
                    option2 = "Brown",
                    option3 = null,
                    sku = "WALLET-BROWN-OS",
                    inventory_quantity = 12,
                    inventory_item_id = 3010
                )
            ),
            options = listOf(
                Option(
                    id = 4010,
                    product_id = 1002,
                    name = "Size",
                    values = listOf("One Size")
                ),
                Option(
                    id = 4011,
                    product_id = 1002,
                    name = "Color",
                    values = listOf("Brown", "Black")
                )
            ),
            images = listOf(
                Image(
                    id = 5010,
                    product_id = 1002,
                    src = "https://example.com/images/wallet-brown.jpg"
                )
            )
        )
    )
    @Before
    fun setup() {
        remoteDataSource = FakeRemoteDataSource(remoteProductList)
        repo = ProductRepoImpl(remoteDataSource = remoteDataSource)
    }
    @Test
    fun `getAllProducts should return the list of products`() = runTest {
        val products = repo.getAllProducts().first()
        assertThat(products.size, `is`(2))
        assertThat(products[0].title, `is`("Classic T-Shirt"))
        assertThat(products[1].title, `is`("Leather Wallet"))
    }

    @Test
    fun `getProductById should return correct product`() = runTest{
        val product = repo.getProductById(1002).first()
        assertThat(product.title, `is`("Leather Wallet"))
        assertThat(product.vendor, `is`("Urban Gear"))
    }
    @Test
    fun `updateProduct should update product title`() = runTest{
        val updatedProduct = remoteProductList[0].copy(title = "Updated T-Shirt")
        val result = repo.updateProduct(updatedProduct).first()
        assertThat(result.title, `is`("Updated T-Shirt"))
    }
    @Test(expected = IllegalArgumentException::class)
    fun `updateProduct should throw when title is blank`() = runTest{
        val invalidProduct = remoteProductList[0].copy(title = "")
        repo.updateProduct(invalidProduct).first()
    }
    @Test
    fun `deleteProduct should remove product`() = runTest{
        repo.deleteProduct(1001)
        val products = repo.getAllProducts().first()
        assertThat(products.size, `is`(1))
        assertThat(products[0].id, `is`(1002L))
    }




}