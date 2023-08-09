package com.app.gauntlet.core.presentation.screens

import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.gauntlet.core.presentation.navigation.NavPath
import com.app.module.scanner.presentation.ScannerViewModel
import com.app.module.scanner.presentation.components.ScannerView

import kotlinx.coroutines.delay
import java.io.File
import java.io.InputStream
import java.net.CacheRequest


@Composable
fun HomeScreen(onNavigate : (route : NavPath) -> Unit = {} ){

    var local by remember {
        mutableStateOf(false)
    }

    val vm : ScannerViewModel = viewModel()
    val context = LocalContext.current

    val open = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(), onResult = {

        if(it != null)
        {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        }
            vm.processImage(bitmap)

        }

    })

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ScannerView(
                modifier = Modifier
                .fillMaxSize(), switchTorch = local, vm = vm)
        }

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
            ) {

            Text(text = vm.scanResult)

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))

            Button(onClick = {
                open.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Text(text = "Big Billy")
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))
        }


    }

}

@Composable
@Preview
private fun Preview()
{
    HomeScreen()
}