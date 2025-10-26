package mx.tec.proyectoBJ.view

import android.Manifest
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Un Composable que implementa una pantalla completa para el escaneo de códigos QR.
 *
 * Esta función integra varias tecnologías para proporcionar una experiencia de escaneo robusta:
 * - **Accompanist Permissions:** Gestiona la solicitud del permiso de la cámara en tiempo de ejecución.
 * - **CameraX:** Proporciona una vista previa de la cámara y un flujo de análisis de imágenes.
 * - **ML Kit Vision:** Procesa los frames de la cámara para detectar y decodificar códigos QR.
 *
 * La UI permite al usuario iniciar un escaneo, muestra una vista de la cámara mientras
 * escanea, y mantiene una lista de los códigos QR únicos que han sido escaneados.
 * El escaneo se detiene automáticamente después de detectar el primer código QR.
 *
 * @param paddingValues El padding proporcionado por un `Scaffold` u otro contenedor padre
 *                      para evitar que el contenido se solape con las barras del sistema.
 *
 * Creado por: Carlos Antonio Tejero Andrade A01801062
 */
@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EscaneoQR(
    paddingValues: PaddingValues
){
    // Estado para almacenar los valores de los QR escaneados (cadenas de texto).
    var escaneos by remember { mutableStateOf(emptyList<String>()) }
    // Estado para controlar si la vista de la cámara está activa.
    var escaneo by remember { mutableStateOf(false) }

    // Propietarios del ciclo de vida y contexto local, necesarios para CameraX.
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // Executor para ejecutar el análisis de imágenes en un hilo separado.
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    // Cliente del escáner de códigos de barras de ML Kit, configurado solo para QR.
    val barcodeScanner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )
    }

    // --- Manejo de Permisos de la Cámara ---
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(Unit) {
        // Solicita el permiso si aún no ha sido otorgado.
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    // Libera el Executor cuando el Composable se elimina de la composición.
    DisposableEffect(Unit) {
        onDispose {
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
            // Botón para activar el modo de escaneo.
            Button(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                onClick = { escaneo = true }
            ) {
                Text(text = "Escanear QR")
            }

            // Muestra un contador de los códigos escaneados.
            Text(
                text = "${escaneos.size} códigos escaneados",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )

            // --- Vista de la Cámara ---
            // Muestra la vista de la cámara solo si el modo de escaneo está activo y se tienen los permisos.
            if (escaneo && cameraPermissionState.status.isGranted) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(top = 12.dp),
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()

                            // Configura el caso de uso de `Preview` para mostrar la imagen de la cámara.
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            // Configura el caso de uso de `ImageAnalysis` para procesar los frames.
                            val imageAnalysis = ImageAnalysis.Builder().build().apply {
                                setAnalyzer(cameraExecutor) { imageProxy ->
                                    val mediaImage = imageProxy.image
                                    if (mediaImage != null) {
                                        val inputImage = InputImage.fromMediaImage(
                                            mediaImage,
                                            imageProxy.imageInfo.rotationDegrees
                                        )
                                        // Procesa la imagen con ML Kit.
                                        barcodeScanner.process(inputImage)
                                            .addOnSuccessListener { barcodes ->
                                                for (barcode in barcodes) {
                                                    barcode.rawValue?.let { value ->
                                                        // Añade el valor a la lista solo si es nuevo.
                                                        if (!escaneos.contains(value)) {
                                                            val lista = escaneos.toMutableList()
                                                            lista.add(value)
                                                            escaneos = lista
                                                            // Detiene el escaneo al encontrar un QR válido.
                                                            escaneo = false
                                                        }
                                                    }
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("Scanner", "Error: ${e.message}")
                                            }
                                            .addOnCompleteListener {
                                                // Cierra la imagen para que el analizador reciba la siguiente.
                                                imageProxy.close()
                                            }
                                    } else {
                                        imageProxy.close()
                                    }
                                }
                            }

                            // Selecciona la cámara trasera por defecto.
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            try {
                                // Desvincula todos los casos de uso antes de volver a vincular.
                                cameraProvider.unbindAll()
                                // Vincula los casos de uso al ciclo de vida del Composable.
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

            // --- Lista de Códigos Escaneados ---
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
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = item
                            )
                            // Botón para eliminar un código de la lista.
                            IconButton(
                                onClick = {
                                    val lista = escaneos.toMutableList()
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
