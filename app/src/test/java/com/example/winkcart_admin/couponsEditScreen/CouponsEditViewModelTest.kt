package com.example.winkcart_admin.couponsEditScreen

import com.example.winkcart_admin.CouponsEditScreen.CouponsEditViewModel
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.model.CouponFormState
import com.example.winkcart_admin.model.PrerequisiteSubtotalRange
import com.example.winkcart_admin.model.PriceRule
import com.example.winkcart_admin.model.Product

import com.example.winkcart_admin.productsScreen.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CouponsEditViewModelTest {

    private lateinit var repo: ProductRepo
    private lateinit var viewModel: CouponsEditViewModel

    private val testProduct = Product(id = 1, title = "Product 1")

    private val testCoupon = PriceRule(
        id = 2L,
        title = "Test Coupon",
        targetType = "line_item",
        targetSelection = "all",
        allocationMethod = "across",
        valueType = "percentage",
        value = "-10.0",
        customerSelection = "all",
        startsAt = "2024-01-01T00:00:00Z",
        endsAt = "2025-01-01T00:00:00Z",
        usageLimit = 10,
        oncePerCustomer = true,
        prerequisiteSubtotalRange = PrerequisiteSubtotalRange("50.0"),
        discountCodeMap = mutableMapOf(1L to "CODE10"),
        entitledProductIds = emptyList()
    )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() = runTest {
        repo = mockk(relaxed = true)
        coEvery { repo.getAllProducts() } returns flowOf(mutableListOf(testProduct))
        coEvery { repo.getCouponDataById(2L) } returns testCoupon
        coEvery { repo.createCoupon(any(), any()) } returns testCoupon
        coEvery { repo.updatePriceRule(any(), any()) } returns testCoupon
        viewModel = CouponsEditViewModel(repo)
        advanceUntilIdle()
    }

    @Test
    fun `loadCoupon sets default PriceRule when id is 0`() = runTest {
        viewModel.loadCoupon(0L)
        val response = viewModel.couponResponse.value
        assertTrue(response is ResponseStatus.Success)
        assertEquals(0L, (response as ResponseStatus.Success).result.id)
    }

    @Test
    fun `loadCoupon fetches and updates state when id is not 0`() = runTest {
        viewModel.loadCoupon(2L)
        advanceUntilIdle()
        val response = viewModel.couponResponse.value
        assertTrue(response is ResponseStatus.Success)
        assertEquals(2L, (response as ResponseStatus.Success).result.id)
        assertEquals("Test Coupon", viewModel.formState.value.title)
    }

    @Test
    fun `uploadCouponUpdate creates new coupon when id is 0`() = runTest {
        viewModel.onFieldChange(CouponFormState(id = 0L, title = "New", value = "-15"))
        viewModel.uploadCouponUpdate()
        advanceUntilIdle()
        val response = viewModel.couponResponse.value
        assertTrue(response is ResponseStatus.Success)
        assertEquals("Test Coupon", (response as ResponseStatus.Success).result.title)
    }

    @Test
    fun `uploadCouponUpdate updates existing coupon when id is not 0`() = runTest {
        viewModel.onFieldChange(CouponFormState(id = 2L, title = "Updated", value = "-20"))
        viewModel.uploadCouponUpdate()
        advanceUntilIdle()
        val response = viewModel.couponResponse.value
        assertTrue(response is ResponseStatus.Success)
        assertEquals("Test Coupon", (response as ResponseStatus.Success).result.title)
    }

    @Test
    fun `isProductIdValid returns true if product exists`() {
        assertTrue(viewModel.isProductIdValid(1))
        assertFalse(viewModel.isProductIdValid(99))
    }
}
