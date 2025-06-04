package com.example.winkcart_admin.data.remote

import com.example.winkcart_admin.data.remote.retrofit.AdminServices
import com.example.winkcart_admin.model.ImageData
import com.example.winkcart_admin.model.ImageRequest
import com.example.winkcart_admin.model.ImageResponse
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.model.SingleProductResponse

class RemoteDataSourceImpl(private val adminServices: AdminServices) :RemoteDataSource{
    override suspend fun getAllProducts(): MutableList<Product> {
        return adminServices.getProducts().products
    }

    override suspend fun getProductByID(id:Long): Product {
        return adminServices.getProductById(id).product
    }

    override suspend fun createProduct(product: Product): Product {
        return adminServices.createProduct(SingleProductResponse(product)).product
    }

    override suspend fun updateProduct(id: Long, product: Product): Product {
        return adminServices.updateProduct(id, SingleProductResponse(product)).product
    }

    override suspend fun deleteProduct(id: Long) {
        return adminServices.deleteProduct(id)
    }

    override suspend fun addImageToProduct(productId: Long, imageURl: String): ImageData {
        return adminServices.addImageToProduct(productId, ImageRequest(ImageData(imageURl))).image
    }

    override suspend fun deleteImageFromProduct(productId: Long, imageId: Long) {
        return adminServices.deleteImageFromProduct(productId, imageId)
    }

    override suspend fun deleteProductVariant(productId: Long, variantId: Long) {
        return adminServices.deleteProductVariant(productId,variantId)
    }
}