package com.pdm.ml_face_detection.presentation

import androidx.camera.core.CameraSelector
import com.pdm.ml_face_detection.domain.models.FaceResult

data class UIState(
    val camera: Int = CameraSelector.LENS_FACING_BACK,
    val faceResult: FaceResult = FaceResult()
)
