package com.example.lmsnowplaying

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(heightDp = 540, widthDp = 960)
@Composable
fun ErrorScreen(){

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .background(Color.LightGray)
        .fillMaxSize()) {
            Text(text = "Logitech Media Server Details not Set")
        }

}
