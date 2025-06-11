package com.example.winkcart_admin.CouponsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.winkcart_admin.BottomNavigationBar
import com.example.winkcart_admin.CouponsScreen.components.CouponCard
import com.example.winkcart_admin.Screens
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.CouponsModel
import com.example.winkcart_admin.productsScreen.components.AdminAlert
import com.example.winkcart_admin.productsScreen.components.AdminFailureState
import com.example.winkcart_admin.productsScreen.components.AdminLoading

@Composable
fun CouponsScreen(
    navHostController: NavHostController,
    viewModel: CouponsViewModel
) {
    val state by viewModel.couponsState.collectAsState()
    var showDeleteAlert by remember { mutableStateOf(false) }
    lateinit var couponToDelete:CouponsModel

    LaunchedEffect(Unit){
        viewModel.fetchCoupons()
    }
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is ResponseStatus.Loading -> {
                    AdminLoading()
                }
                is ResponseStatus.Error -> {
                    AdminFailureState("Failed to load coupons")
                }
                is ResponseStatus.Success -> {
                    val coupons = (state as ResponseStatus.Success<MutableList<CouponsModel>>).result
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(coupons) { coupon ->
                            CouponCard(
                                coupon = coupon,
                                onClick = {
                                    navHostController.navigate(Screens.CouponsEditScr(coupon.id))
                                },
                                onDelete = {
                                    showDeleteAlert=true
                                    couponToDelete=coupon
                                }
                            )
                        }
                    }
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                        onClick = {
                            navHostController.navigate(Screens.CouponsEditScr(0))}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add new Coupon"
                        )
                    }
                }
            }
            if (showDeleteAlert){
                AdminAlert(
                    title = "Delete Coupon",
                    message = "Are you Sure You want to delete this coupon?",
                    confirmMessage = "Delete",
                    onDismissAction = { showDeleteAlert = false },
                    onConfirmAction = {
                        viewModel.deleteCoupon(coupon = couponToDelete)
                        showDeleteAlert=false
                    }
                )
            }
        }
    }
}
