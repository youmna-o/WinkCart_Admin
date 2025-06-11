package com.example.winkcart_admin.data.repository

import android.util.Log
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.remote.RemoteDataSource
import com.example.winkcart_admin.model.CouponsModel
import com.example.winkcart_admin.model.DiscountCodeRequest
import com.example.winkcart_admin.model.ImageData
import com.example.winkcart_admin.model.InventoryLevelSetRequest
import com.example.winkcart_admin.model.PriceRule
import com.example.winkcart_admin.model.PriceRuleRequest
import com.example.winkcart_admin.model.PriceRulesResponse
import com.example.winkcart_admin.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ProductRepoImpl (private val remoteDataSource: RemoteDataSource):ProductRepo{
    override suspend fun getAllProducts(): Flow<MutableList<Product>> = flow {
        try {
            val products= remoteDataSource.getAllProducts()
            emit(products)
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProductById(id: Long): Flow<Product> = flow{
        try {
            val product= remoteDataSource.getProductByID(id)
            emit(product)
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createProduct(createdProduct: Product): Flow<Product> =flow{
        try {
            val product= remoteDataSource.createProduct(createdProduct)
            emit(product)
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateProduct(productUpdated: Product): Flow<Product> = flow{
        try {
            if(productUpdated.title.isNotBlank()){
                val product= remoteDataSource.updateProduct(productUpdated.id,productUpdated)
                emit(product)
            }
            else{
                throw IllegalArgumentException("Product Title Cant be blank")
            }
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteProduct(id: Long) {
        withContext(Dispatchers.IO){
            try {
                remoteDataSource.deleteProduct(id)
            }catch (ex:Exception){
                throw ex
            }
        }

    }

    override suspend fun addImageToProduct(productId: Long, imageURl: String): Flow<ImageData> =flow{
        try {
            val imageData= remoteDataSource.addImageToProduct(productId,imageURl)
            emit(imageData)
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteImageFromProduct(productId: Long, imageId: Long) {
        withContext(Dispatchers.IO){
            try {
                remoteDataSource.deleteImageFromProduct(productId, imageId)
            }catch (ex:Exception){
                throw ex
            }
        }
    }

    override suspend fun deleteVariant(productId: Long, variantId: Long) {
        withContext(Dispatchers.IO){
            remoteDataSource.deleteProductVariant(productId,variantId)
        }
    }

    override suspend fun setInventoryLevel(value: Int, inventoryItemId: Long, locationID: Long) {
        withContext(Dispatchers.IO){
            remoteDataSource.setInventoryLevel(
                request = InventoryLevelSetRequest(
                    inventory_item_id = inventoryItemId,
                    available = value,
                    location_id = locationID
                )
            )
        }

    }
    override suspend fun getAllCoupons(): MutableList<CouponsModel> = withContext(Dispatchers.IO) {
        val rulesResponse = remoteDataSource.getAllPriceRules()

        rulesResponse.price_rules.map { rule ->
            val codesResponse = remoteDataSource.getDiscountCodesForPriceRule(rule.id)
            Log.i("TAG", "getCouponDataById:codes: ${codesResponse}")
            val discountCodeMap = codesResponse.discountCodeList
                ?.associate { it.id to it.code }
                ?: emptyMap()

            CouponsModel(
                id = rule.id,
                title = rule.title,
                targetType = rule.targetType,
                valueType = rule.valueType,
                value = rule.value,
                startsAt = rule.startsAt,
                endsAt = rule.endsAt ?: "NA",
                usageLimit = rule.usageLimit ?: 0,
                discountCodeMap = discountCodeMap.toMutableMap()
            )
        }.toMutableList()
    }

    override suspend fun getCouponDataById(id: Long): PriceRule = withContext(Dispatchers.IO){
        val priceRule=remoteDataSource.getPriceRuleById(id).price_rule
        val codesResponse = remoteDataSource.getDiscountCodesForPriceRule(priceRule.id)
        Log.i("TAG", "getCouponDataById:codes: ${codesResponse}")
        val discountCodeMap = codesResponse.discountCodeList
            ?.associate { it.id to it.code }
            ?: emptyMap()
        priceRule.discountCodeMap=discountCodeMap.toMutableMap()
        priceRule
    }


    override suspend fun createCoupon(request: PriceRuleRequest, discountCodeRequests: List<DiscountCodeRequest>): PriceRule = withContext(Dispatchers.IO) {
        val rule = remoteDataSource.createPriceRule(request)
        Log.i("TAG", "createCoupon: ${rule}")
        discountCodeRequests.forEach {
            val code=remoteDataSource.createDiscountCode(rule.price_rule.id, it)
            Log.i("TAG", "createCoupon: ${code}")
        }
        getCouponDataById(rule.price_rule.id)
    }

    override suspend fun deleteCoupon(priceRuleId: Long) = withContext(Dispatchers.IO) {
        remoteDataSource.deletePriceRule(priceRuleId)
    }

    override suspend fun updatePriceRule(request: PriceRuleRequest,discountCodeRequests: List<DiscountCodeRequest>) : PriceRule = withContext(Dispatchers.IO) {
        val rule = remoteDataSource.updatePriceRule(request)
        Log.i("TAG", "updatedPriceRule: ${rule}")
        discountCodeRequests.forEach {
            val code=remoteDataSource.createDiscountCode(rule.price_rule.id, it)
            Log.i("TAG", "updatedPriceRule:codes: ${code}")
        }
        getCouponDataById(rule.price_rule.id)
    }

    override suspend fun deleteDiscountCode(productId: Long, codeId: Long) = withContext(Dispatchers.IO) {
        remoteDataSource.deleteDiscountCode(
            priceRuleId = productId,
            discountCodeId = codeId
        )

    }

}