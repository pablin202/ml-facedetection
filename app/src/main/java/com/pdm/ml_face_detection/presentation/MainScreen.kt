package com.pdm.ml_face_detection.presentation

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.pdm.ml_face_detection.data.MLKitFaceDetectorProcessor
import com.pdm.ml_face_detection.domain.FaceResult
import com.pdm.ml_face_detection.presentation.components.CameraPreview

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val analyzer = remember {
        ImageAnalyzer(
            faceDetectorProcessor = MLKitFaceDetectorProcessor(),
            onResults = {
                viewModel.updateFaceDetection(it)
            }
        )
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                analyzer
            )
        }
    }

    MainScreenContent(viewModel.uiState.value.faceResult.faceVisible) {
        viewModel.updateFaceDetection(it)
    }
}


@Composable
fun MainScreenContent(
    faceVisible: Boolean,
    onResult: (FaceResult) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CameraPreview(Modifier.fillMaxSize()) {
            onResult(it)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (faceVisible) "" else "NO FACE",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}