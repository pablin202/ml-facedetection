package com.pdm.ml_face_detection.presentation

import com.pdm.ml_face_detection.domain.FaceResult

data class UIState(
    val faceResult: FaceResult = FaceResult()
)
