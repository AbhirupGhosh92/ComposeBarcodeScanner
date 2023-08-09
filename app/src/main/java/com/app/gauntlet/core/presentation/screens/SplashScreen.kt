package com.app.gauntlet.core.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.app.gauntlet.core.presentation.navigation.NavPath
import kotlinx.coroutines.delay

@Composable
fun SpalshScreen(onNavigate : (route : NavPath) -> Unit = {})
{
    LaunchedEffect(key1 = true ){
        delay(2000)
        onNavigate(NavPath.SPLASH_TO_HOME())
    }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Splash screen")
    }
}