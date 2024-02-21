package com.joshuamaddelein.hackathon2024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.joshuamaddelein.hackathon2024.ui.theme.Hackathon2024Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hackathon2024Theme {
                Surface(color = Color.Transparent, modifier = Modifier.fillMaxSize()) {
                    IdQrScreen()
                }
            }
        }
    }
}

@Composable
fun IdQrScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.qrcode_me),
            contentDescription = "Id qr",
            modifier = modifier.size(300.dp).clip(
                RoundedCornerShape(3))
        )
    }
}