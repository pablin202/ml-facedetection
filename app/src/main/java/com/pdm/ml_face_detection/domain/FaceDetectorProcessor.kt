package com.pdm.ml_face_detection.domain

import androidx.camera.core.ImageProxy
import com.pdm.ml_face_detection.domain.models.FaceResult

interface FaceDetectorProcessor {
    fun processFace(image: ImageProxy, result: (FaceResult) -> Unit)
}