package com.pdm.ml_face_detection.presentation.components

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.pdm.ml_face_detection.data.MLKitFaceDetectorProcessor
import com.pdm.ml_face_detection.domain.models.FaceResult
import com.pdm.ml_face_detection.presentation.ImageAnalyzer

@Composable
fun CameraPreview(
    modifier: Modifier,
    camera: Int = CameraSelector.LENS_FACING_BACK,
    onResult: (FaceResult) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val analyzer = remember {
        ImageAnalyzer(
            MLKitFaceDetectorProcessor(),
            onResults = {
                onResult(it)
            }
        )
    }

    AndroidView(
        factory = { ctx ->
            val executor = ContextCompat.getMainExecutor(ctx)

            val previewView = PreviewView(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(camera)
                    .build()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setImageQueueDepth(10)
                    .build()
                    .apply {
                        setAnalyzer(executor, analyzer)
                    }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            }, executor)
            previewView
        }, modifier = modifier
    )
}

