package com.pdm.ml_face_detection.presentation

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.pdm.ml_face_detection.domain.models.FaceResult
import com.pdm.ml_face_detection.presentation.components.CameraPreview
import com.pdm.ml_face_detection.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        MainScreenContent(viewModel.uiState.value, {
            viewModel.rotateCamera()
        }) {
            viewModel.updateFaceDetection(it)
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "The camera is important for this app. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Camera permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Text(textToShow, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}

@Composable
fun MainScreenContent(
    state: UIState,
    onRotateCamera: () -> Unit,
    onResult: (FaceResult) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CameraPreview(Modifier.fillMaxSize(), state.camera) {
            onResult(it)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.ellipse_face),
                contentDescription = null
            )
        }

//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = if (state.faceResult.faceVisible) "" else "NO FACE",
//                color = Color.White,
//                style = MaterialTheme.typography.headlineLarge,
//                modifier = Modifier.weight(8f)
//            )
//            Row(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Absolute.Right
//            ) {
//                IconButton(onClick = { onRotateCamera() }) {
//                    Icon(
//                        modifier = Modifier.size(48.dp),
//                        painter = painterResource(id = R.drawable.baseline_cameraswitch_24),
//                        contentDescription = "",
//                        tint = Color.White
//                    )
//                }
//            }
//        }
    }
}

@Preview
@Composable
fun ContentPreview() {
    MainScreenContent(state = UIState(), onRotateCamera = { /*TODO*/ }, onResult = {})
}