package com.example.winkcart_admin.ProfileScreen

import androidx.compose.material3.Scaffold
import androidx.navigation.NavHostController
import com.example.winkcart_admin.BottomNavigationBar


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.winkcart_admin.R
import com.example.winkcart_admin.Screens
import com.example.winkcart_admin.ui.theme.BackgroundColor
import com.example.winkcart_admin.ui.theme.HeaderTextColor

@Composable
fun ProfileScreen(navHostController: NavHostController, viewModel: ProfileViewModel = hiltViewModel()) {
    val loggedInState = viewModel.loggedInState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                Image(
                    painter = painterResource(R.drawable.product_placeholder),
                    contentDescription = "app Icon",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Welcome Admin",
                    fontSize = 24.sp
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    onClick = {
                        if (loggedInState.value) {
                            viewModel.logOut()
                        }
                        navHostController.navigate(Screens.LoginScr)
                    }
                ) {
                    Text(text = if (loggedInState.value) "Log Out" else "Log In")
                }
            }

            SettingsCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                settingIcon = R.drawable.about_us,
                settingName = "About Us",
                onClick = {
                    navHostController.navigate(Screens.AboutUsScr)
                }
            )
        }
    }
}

@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    settingIcon: Int = R.drawable.about_us,
    settingName: String = "About us",
    onClick: () -> Unit = {}

) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = onClick
    ) {
        Row(
            Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(settingIcon),
                contentDescription = settingName,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = settingName,
                color = HeaderTextColor,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.PlayArrow, contentDescription = "go to details")
        }
    }
}

