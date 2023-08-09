package com.app.gauntlet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.app.gauntlet.core.presentation.navigation.NavHostController
import com.app.gauntlet.core.presentation.screens.HomeScreen
import com.app.gauntlet.core.presentation.viewmodels.MainActivityViewModel
import com.app.gauntlet.core.theme.GauntletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            SideEffect {
                viewModel.onCreate(this)
            }

            GauntletTheme {
                // A surface container using the 'background' color from the theme
                NavHostController()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    GauntletTheme {
        HomeScreen()
    }
}