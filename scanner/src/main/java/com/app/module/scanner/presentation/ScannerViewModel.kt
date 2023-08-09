package com.app.module.scanner.presentation

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

class ScannerViewModel : ViewModel() {

    private lateinit var camera: Camera
    private var cameraPaused = false
    private lateinit var analysis: ImageAnalysis
    private lateinit var options : BarcodeScannerOptions
    private lateinit var scanner: BarcodeScanner

    fun setTorch(state : Boolean)
    {
        if(::camera.isInitialized) {
            camera.cameraControl.enableTorch(state)
        }
    }

    fun pauseCamera(state: Boolean)
    {
        cameraPaused = state
    }


    var scanResult by mutableStateOf("")
        private set

    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {

        imageProxy.image?.let { image ->
            val inputImage =
                InputImage.fromMediaImage(
                    image,
                    imageProxy.imageInfo.rotationDegrees
                )

            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    val barcode = barcodeList.getOrNull(0)
                    // `rawValue` is the decoded value of the barcode
                    barcode?.rawValue?.let { value ->
                        // update our textView to show the decoded value
                       if(value.isNullOrEmpty().not()) {
                           Log.d("scanned", value)
                           scanResult = value
                       }
                    }
                }
                .addOnFailureListener {
                    // This failure will happen if the barcode scanning model
                    // fails to download from Google Play Services
                    Log.e("scanned", it.message.orEmpty())
                }.addOnCompleteListener {
                    // When the image is from CameraX analysis use case, must
                    // call image.close() on received images when finished
                    // using them. Otherwise, new images may not be received
                    // or the camera may stall.
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }

    private fun initBarcodeScanner(maxZoom : Float) : ImageAnalysis
    {
        if(::analysis.isInitialized.not()) {
                options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_CODE_128,
                        Barcode.FORMAT_CODE_39,
                        Barcode.FORMAT_CODE_93,
                        Barcode.FORMAT_EAN_8,
                        Barcode.FORMAT_EAN_13,
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_UPC_A,
                        Barcode.FORMAT_UPC_E,
                        Barcode.FORMAT_PDF417
                    )
                    .enableAllPotentialBarcodes()
                    .setZoomSuggestionOptions(
                        ZoomSuggestionOptions.Builder {
                            if (::camera.isInitialized) {
                                camera.cameraControl.setZoomRatio(it)
                            }
                            return@Builder true
                        }
                            .setMaxSupportedZoomRatio(maxZoom)
                            .build())
                    .build()

                scanner = BarcodeScanning.getClient(options)
                analysis = ImageAnalysis.Builder()
                    .build()

                analysis.setAnalyzer(
                    // newSingleThreadExecutor() will let us perform analysis on a single worker thread
                    Executors.newSingleThreadExecutor()
                ) {
                    if (cameraPaused.not()) {
                        processImageProxy(scanner, it)
                    }
                }
        }

        return analysis
    }
    fun initPreview(context: Context,view : PreviewView, lifecycleOwner : LifecycleOwner,maxZoom: Float)
    {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(view.context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(view.surfaceProvider)
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
               camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    previewUseCase,
                    initBarcodeScanner(maxZoom)
                )
            } catch (illegalStateException: IllegalStateException) {
                // If the use case has already been bound to another lifecycle or method is not called on main thread.
                Log.e("Camera", illegalStateException.message.orEmpty())
            } catch (illegalArgumentException: IllegalArgumentException) {
                // If the provided camera selector is unable to resolve a camera to be used for the given use cases.
                Log.e("Camera", illegalArgumentException.message.orEmpty())
            }

        }, ContextCompat.getMainExecutor(context))
    }

    fun processImage(image: Bitmap,callback : (result:String) -> Unit = {})
    {
        if(::options.isInitialized.not())
            options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_CODE_39,
                Barcode.FORMAT_CODE_93,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E,
                Barcode.FORMAT_PDF417
            )
            .enableAllPotentialBarcodes()
            .build()
        if(::scanner.isInitialized.not())
            scanner = BarcodeScanning.getClient(options)
        val inpImage = InputImage.fromBitmap(image,0)
        scanner.process(inpImage).addOnSuccessListener { barcodeList ->
            val barcode = barcodeList.getOrNull(0)
            // `rawValue` is the decoded value of the barcode
            barcode?.rawValue?.let { value ->
                // update our textView to show the decoded value
                if(value.isNullOrEmpty().not()) {
                    Log.d("scanned", value)
                    scanResult = value
                    callback(value)
                }
            }
        }
            .addOnFailureListener {
                // This failure will happen if the barcode scanning model
                // fails to download from Google Play Services
                Log.e("scanned", it.message.orEmpty())
            }.addOnCompleteListener {
                // When the image is from CameraX analysis use case, must
                // call image.close() on received images when finished
                // using them. Otherwise, new images may not be received
                // or the camera may stall.

            }

    }



}