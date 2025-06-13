package com.example.winkcart_admin.data.remote

import com.example.winkcart_admin.data.remote.retrofit.AdminServices
import com.example.winkcart_admin.model.DiscountCodeRequest
import com.example.winkcart_admin.model.DiscountCodeResponse
import com.example.winkcart_admin.model.ImageData
import com.example.winkcart_admin.model.InventoryLevelSetRequest
import com.example.winkcart_admin.model.PriceRuleRequest
import com.example.winkcart_admin.model.PriceRulesResponse
import com.example.winkcart_admin.model.Product

class FakeRemoteDataSource(private var productList:List<Product>) :RemoteDataSource{
    override suspend fun getAllProducts(): MutableList<Product> {
        return productList.toMutableList()
    }

    override suspend fun getProductByID(id: Long): Product {
        return productList.find { it.id==id }?: throw NoSuchElementException("No Product Found")
    }

    override suspend fun createProduct(product: Product): Product {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(id: Long, product: Product): Product {
        val newList=productList.toMutableList()
        val isRemoved=newList.removeIf { it.id==id }
        if (isRemoved){
            newList.add(product)
            productList=newList.toList()
            return product
        }
        else{
            throw NoSuchElementException("No Product Found")
        }

    }

    override suspend fun deleteProduct(id: Long) {
        val newList=productList.toMutableList()
        val isRemoved=newList.removeIf { it.id==id }
        if (isRemoved){
            productList=newList.toList()
        }else{
            throw NoSuchElementException("No Product Found")

        }
    }

    override suspend fun addImageToProduct(productId: Long, imageURl: String): ImageData {
        TODO("Not yet implemented")
    }

    override suspend fun deleteImageFromProduct(productId: Long, imageId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProductVariant(productId: Long, variantId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun setInventoryLevel(request: InventoryLevelSetRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPriceRules(): PriceRulesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getPriceRuleById(ruleId: Long): PriceRuleRequest {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscountCodesForPriceRule(priceRuleId: Long): DiscountCodeResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createPriceRule(request: PriceRuleRequest): PriceRuleRequest {
        TODO("Not yet implemented")
    }

    override suspend fun createDiscountCode(
        priceRuleId: Long,
        request: DiscountCodeRequest
    ): DiscountCodeResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDiscountCode(priceRuleId: Long, discountCodeId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePriceRule(priceRuleId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePriceRule(request: PriceRuleRequest): PriceRuleRequest {
        TODO("Not yet implemented")
    }

}