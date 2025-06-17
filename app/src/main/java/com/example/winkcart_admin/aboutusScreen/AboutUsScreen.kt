package com.example.winkcart_admin.aboutusScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.winkcart_admin.BottomNavigationBar
import com.example.winkcart_admin.R
import com.example.winkcart_admin.ui.theme.BackgroundColor
import com.example.winkcart_admin.ui.theme.HeaderTextColor


@Composable
fun AboutUsScreen() {
    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundColor)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text ="About Us",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = HeaderTextColor
            )

            Spacer(modifier = Modifier.height(16.dp))


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.about_us_st1),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.about_us_st2),
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.development_team),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeaderTextColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            val teamMembers = listOf(
                R.string.team_member1,
                R.string.team_member2,
                R.string.team_member3,
                R.string.team_member4
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                teamMembers.forEach {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = stringResource(it),
                            fontSize = 16.sp,
                            color = HeaderTextColor,
                            modifier = Modifier
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
