# Compose Barcode Scanner [![](https://jitpack.io/v/AbhirupGhosh92/ComposeBarcodeScanner.svg)](https://jitpack.io/#AbhirupGhosh92/ComposeBarcodeScanner)

## About

Barcode scanning is an important use case and we often need to find an easy solution for our application. I have implemented a barcode/QRcode scanner in this library, which can easily be used as a composable.

## Installation

The library can be easily installed using the following gradle statement

``` gradle
  dependencies {
      implementation("com.github.AbhirupGhosh92:ComposeBarcodeScanner:{latest-version}")
    }
```

## Features

This library has the following features :-

*  Jetpack Compose supported ```ScannerView```
*  [ML kit](https://developers.google.com/ml-kit/vision/barcode-scanning) integrated , which allows for faster scanning for a variety of codes
*  Auto camera zoom enabled , which allows the camera to auto zoom on the barcode
*  Torch switch on and off control
*  Read image from gallery supported

## Example

We can use the scanner view inside any composable in the following way :- 

First we need to instantiate the ```ScannerViewModel``` like 

``` Kotlin
 val vm : ScannerViewModel = viewModel()
```

Now we need to pass the instance of this ```vm``` to the ```ScannerView``` in the following way

``` Kotlin
 Surface(color = MaterialTheme.colorScheme.background) {
            ScannerView(
                modifier = Modifier
                .fillMaxSize(), switchTorch = isTorchOn, vm = vm)
        }
```

This will initialise the scanner view with a preview and start scanning for codes. There are also arguments to set the max zoom , to pause the scanning and a callback method to receive the scan result.

However, the scan result can also be received from the ```vm.scanResult```

Similarly we can fetch a bitmap from the gallery and pass it to the method ``` vm.processImage(bitmap)``` for reading the code from the gallery. This will also give us the result in the same ```vm.scanResult``` or the view callback
