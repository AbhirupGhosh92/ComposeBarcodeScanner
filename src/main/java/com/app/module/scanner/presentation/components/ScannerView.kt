package com.app.module.scanner.presentation.components

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.module.scanner.databinding.ScannerLayoutBinding
import com.app.module.scanner.presentation.ScannerViewModel


@Composable
fun ScannerView(modifier:Modifier = Modifier,
                maxZoom : Float = 0.8f,
                switchTorch : Boolean = false,
                pauseCamera : Boolean = false,
                vm : ScannerViewModel,
                scanResult: (value : String)-> Unit = {})
{

    LaunchedEffect(key1 = switchTorch)
    {
        vm.setTorch(switchTorch)
    }

    LaunchedEffect(key1 = pauseCamera){
        vm.pauseCamera(pauseCamera)
    }

    LaunchedEffect(key1 = vm.scanResult)
    {
        scanResult(vm.scanResult)
    }

    var showCameraPreview by remember{
        mutableStateOf(false)
    }



    val permission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        showCameraPreview = it
    }

    val context = LocalContext.current
    val inflater = LayoutInflater.from(context)
    val binding = ScannerLayoutBinding.inflate(inflater)
    val lifecycleOwner = LocalLifecycleOwner.current


    DisposableEffect(lifecycleOwner)
    {


        val observer = LifecycleEventObserver { source, event ->
            when(event)
            {
                Lifecycle.Event.ON_CREATE -> {

                }
                Lifecycle.Event.ON_START -> {

                }
                Lifecycle.Event.ON_RESUME -> {

                }
                Lifecycle.Event.ON_PAUSE -> {

                }
                Lifecycle.Event.ON_STOP -> {

                }
                Lifecycle.Event.ON_DESTROY -> {

                }
                Lifecycle.Event.ON_ANY -> {

                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    SideEffect {
        permission.launch(android.Manifest.permission.CAMERA)
    }

    if(showCameraPreview) {
        AndroidView(factory = {
            binding.viewFinder
            binding.root
        }, update = {
            vm.initPreview( context,it, lifecycleOwner,maxZoom)

        }, modifier = modifier)
    }
    else{
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "No Camera Permission granted")
        }

    }
}