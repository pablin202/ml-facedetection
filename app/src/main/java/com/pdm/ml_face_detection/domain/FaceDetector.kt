package com.pdm.ml_face_detection.domain

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy

interface FaceDetectorProcessor {
    fun processFace(image: ImageProxy, result: (FaceResult) -> Unit)
}