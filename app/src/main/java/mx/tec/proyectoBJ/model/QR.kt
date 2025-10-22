package mx.tec.proyectoBJ.model


import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.*
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.size

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun escaneoQR(
    paddingValues: PaddingValues
){
    var escaneos by remember {mutableStateOf(emptyList<String>())}
    var escaneo by remember {mutableStateOf(false)}
    val lifecycleOwner=LocalLifecycleOwner.current
    val cameraExecutor: ExecutorService=remember{
        Executors.newSingleThreadExecutor()
    }
    val barcodeScanner = remember{
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )
    }

    val cameraPermissionState =
        rememberPermissionState(android.Manifest.permission.CAMERA)
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted){
            cameraPermissionState.launchPermissionRequest()
        }
    }

    DisposableEffect(Unit) {
        onDispose{
            cameraExecutor.shutdown()
        }
    }
    Surface(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            //Botón para activar el modo de escaneo
            Button(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                onClick = { escaneo = true }
            ) {
                Text(text = "Escanear QR")
            }

            //Muestra cuántos registros hay
            Text(
                text = "${escaneos.size} códigos Escaneados",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )


            //Si estamos escaneando, se muestra la vista de la cámara
            if (escaneo && cameraPermissionState.status.isGranted) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(top = 12.dp),
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val cameraProviderFuture =
                            ProcessCameraProvider.getInstance(ctx)

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()

                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            val imageAnalysis = ImageAnalysis.Builder().build().apply {

                                setAnalyzer(cameraExecutor) { imageProxy ->
                                    val mediaImage = imageProxy.image
                                    if (mediaImage != null) {
                                        val inputImage = InputImage.fromMediaImage(
                                            mediaImage,
                                            imageProxy.imageInfo.rotationDegrees
                                        )
                                        barcodeScanner.process(inputImage)
                                            .addOnSuccessListener { barcodes ->
                                                for (barcode in barcodes) {
                                                    barcode.rawValue?.let { value ->
                                                        if (!escaneos.contains(value)) {
                                                            val lista =
                                                                escaneos.toMutableList()
                                                            lista.add(value)
                                                            escaneos = lista
                                                            escaneo =
                                                                false // Se detiene al encontrar uno
                                                        }
                                                    }
                                                }
                                            }

                                            .addOnFailureListener { e ->
                                                Log.e(
                                                    "Scanner",
                                                    "Error ${e.message}"
                                                )
                                            }
                                            .addOnCompleteListener {
                                                imageProxy.close()
                                            }
                                    } else {
                                        imageProxy.close()
                                    }
                                }
                            }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                        previewView
                    }

                )
            }
            //Muestra la lista de códigos escaneados con la opción de eliminarlos
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(escaneos) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp), // Ajuste de padding
                        elevation =
                            CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = item
                            )

                            IconButton(
                                onClick = {
                                    val lista =
                                        escaneos.toMutableList()
                                    lista.removeAt(index)
                                    escaneos = lista
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

