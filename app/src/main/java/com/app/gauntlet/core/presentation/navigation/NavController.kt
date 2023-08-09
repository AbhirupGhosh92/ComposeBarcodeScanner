package com.app.gauntlet.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.gauntlet.MainActivity
import com.app.gauntlet.core.presentation.screens.HomeScreen
import com.app.gauntlet.core.presentation.screens.QrScannerScreen
import com.app.gauntlet.core.presentation.screens.SpalshScreen
import com.app.gauntlet.core.presentation.viewmodels.MainActivityViewModel

sealed class NavPath {
    data class START_TO_SPLASH(val name : String = "START_TO_SPLASH") : NavPath()
    data class HOME_TO_SCANNER(val name : String = "HOME_TO_SCANNER") : NavPath()
    data class SPLASH_TO_HOME(val name : String = "SPLASH_TO_HOME") : NavPath()
    data class START_TO_HOME(val name: String = "START_TO_HOME") : NavPath()
}
object Navigator{
    fun navigate(navController: NavHostController,route : NavPath){

        when(route)
        {
            is NavPath.HOME_TO_SCANNER -> {
                navController.navigate(route.name)
            }
            is NavPath.START_TO_SPLASH -> {
                navController.navigate(route.name)
            }

            is NavPath.SPLASH_TO_HOME -> {
                navController.navigate(route.name){
                    popUpTo(NavPath.START_TO_SPLASH().name){
                        inclusive = true
                    }
                }
            }

            is NavPath.START_TO_HOME -> {
                navController.navigate(route.name)
            }
        }
    }
}


@Composable
fun NavHostController(){
    val navController = rememberNavController()
    val viewmodel : MainActivityViewModel = viewModel(LocalContext.current as MainActivity)
    NavHost(navController = navController, startDestination = if(viewmodel.showSplash) NavPath.START_TO_SPLASH().name else NavPath.START_TO_HOME().name) {
        composable(NavPath.SPLASH_TO_HOME().name) {
                HomeScreen {
                    Navigator.navigate(navController, it)
                }
        }

        composable(NavPath.START_TO_SPLASH().name) { SpalshScreen{
            Navigator.navigate(navController,it)
        } }

        composable(NavPath.START_TO_HOME().name) { HomeScreen {
            Navigator.navigate(navController,it)
        } }
        composable(NavPath.HOME_TO_SCANNER().name) { QrScannerScreen{
            Navigator.navigate(navController,it)
        } }
    }
}