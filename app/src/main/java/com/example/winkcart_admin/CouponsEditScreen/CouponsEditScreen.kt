package com.example.winkcart_admin.CouponsEditScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.winkcart_admin.BottomNavigationBar
import com.example.winkcart_admin.CouponsEditScreen.components.CouponEditForm
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.productsScreen.components.AdminFailureState
import com.example.winkcart_admin.productsScreen.components.AdminLoading


@Composable
fun CouponsEditScreen(
    navHostController: NavHostController,
    viewModel: CouponsEditViewModel,
    couponId: Long
) {
    val formState by viewModel.formState.collectAsState()
    val loadingState by viewModel.couponResponse.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.loadCoupon(couponId)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (loadingState) {
                is ResponseStatus.Loading -> AdminLoading()
                is ResponseStatus.Error -> AdminFailureState("Error Loading Coupon")
                is ResponseStatus.Success -> CouponEditForm(
                    formState = formState,
                    isNew = formState.id == 0L,
                    onFieldChange = { state -> viewModel.onFieldChange(state) },
                    onFinishUpdate = { viewModel.uploadCouponUpdate() },
                    onProductIdChange = { viewModel.isProductIdValid(it) },
                    onDeleteCode = { viewModel.deleteDiscountCode(it) },
                )
            }

        }

    }

}

