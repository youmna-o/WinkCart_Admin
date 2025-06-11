package com.example.winkcart_admin.data.remote

import android.util.Log
import com.example.winkcart_admin.data.remote.retrofit.AdminServices
import com.example.winkcart_admin.model.DiscountCodeRequest
import com.example.winkcart_admin.model.DiscountCodeResponse
import com.example.winkcart_admin.model.ImageData
import com.example.winkcart_admin.model.ImageRequest
import com.example.winkcart_admin.model.InventoryLevelSetRequest
import com.example.winkcart_admin.model.PriceRule
import com.example.winkcart_admin.model.PriceRuleRequest
import com.example.winkcart_admin.model.PriceRulesResponse
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
    override suspend fun setInventoryLevel(request: InventoryLevelSetRequest) {
        Log.i("TAG", "setInventoryLevel: ${request}")
        return adminServices.setInventoryLevel(request)
    }

    override suspend fun getAllPriceRules(): PriceRulesResponse {
        return adminServices.getAllPriceRules()
    }

    override suspend fun getPriceRuleById(ruleId:Long): PriceRuleRequest {
        return adminServices.getPriceRuleById(ruleId)
    }

    override suspend fun getDiscountCodesForPriceRule(priceRuleId: Long): DiscountCodeResponse {
        return adminServices.getDiscountCodesForPriceRule(priceRuleId)
    }

    override suspend fun createPriceRule(request: PriceRuleRequest): PriceRuleRequest {
        return adminServices.createPriceRule(request)
    }

    override suspend fun createDiscountCode(priceRuleId: Long, request: DiscountCodeRequest): DiscountCodeResponse {
        return adminServices.createDiscountCode(priceRuleId, request)
    }

    override suspend fun deleteDiscountCode(priceRuleId: Long, discountCodeId: Long) {
        adminServices.deleteDiscountCode(priceRuleId, discountCodeId)
    }

    override suspend fun deletePriceRule(priceRuleId: Long) {
        adminServices.deletePriceRule(priceRuleId)
    }

    override suspend fun updatePriceRule(request: PriceRuleRequest): PriceRuleRequest {
        return adminServices.updatePriceRule(request.price_rule.id,request)
    }


}