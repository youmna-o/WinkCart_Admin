package com.example.winkcart_admin.couponsScreen

import app.cash.turbine.test
import com.example.winkcart_admin.CouponsScreen.CouponsViewModel
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.model.CouponsModel
import com.example.winkcart_admin.productsScreen.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CouponsViewModelTest {

    private lateinit var repo: ProductRepo
    private lateinit var viewModel: CouponsViewModel

    private val testCoupons = mutableListOf(
        CouponsModel(
            id = 1,
            title = "New Year Sale",
            targetType = "ALL",
            valueType = "PERCENTAGE",
            value = "-10",
            startsAt = "2025-01-01T00:00:00Z",
            endsAt = "2025-01-10T23:59:59Z",
            usageLimit = 100,
            discountCodeMap = mutableMapOf(1L to "NewYear10")
        ),
        CouponsModel(
            id = 2,
            title = "Summer Sale",
            targetType = "CATEGORY",
            valueType = "FIXED",
            value = "-20",
            startsAt = "2025-06-01T00:00:00Z",
            endsAt = "2025-06-30T23:59:59Z",
            usageLimit = 50,
            discountCodeMap = mutableMapOf(2L to "SUMMER20")
        )
    )


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        repo = mockk(relaxed = true)
        viewModel = CouponsViewModel(repo)
    }

    @Test
    fun `fetchCoupons emits Success when coupons loaded successfully`() = runTest {
        // Arrange
        coEvery { repo.getAllCoupons() } returns testCoupons

        // Act
        viewModel.fetchCoupons()
        advanceUntilIdle()

        // Assert
        viewModel.couponsState.test {

            val successState = awaitItem()
            assert(successState is ResponseStatus.Success)
            successState as ResponseStatus.Success
            assertThat(successState.result.size, `is`(2))
            assertThat(successState.result[0].discountCodeMap[1L], `is`("NewYear10"))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteCoupon removes coupon from list`() = runTest {
        // Arrange
        coEvery { repo.getAllCoupons() } returns testCoupons
        coEvery { repo.deleteCoupon(any()) } returns Unit

        viewModel.fetchCoupons()
        advanceUntilIdle()

        viewModel.couponsState.test {
            awaitItem() // Success

            // Act
            viewModel.deleteCoupon(testCoupons[0])
            advanceUntilIdle()

            awaitItem() // Loading
            val finalState = awaitItem() // Success

            // Assert
            assert(finalState is ResponseStatus.Success)
            finalState as ResponseStatus.Success
            assertThat(finalState.result.any { it.id == 1L }, `is`(false))
            assertThat(finalState.result.size, `is`(1))

            cancelAndIgnoreRemainingEvents()
        }
    }
}
