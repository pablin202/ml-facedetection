package com.pdm.ml_face_detection.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.pdm.ml_face_detection.domain.FaceDetectorProcessor
import com.pdm.ml_face_detection.domain.models.FaceResult

class ImageAnalyzer(
    private val faceDetectorProcessor: FaceDetectorProcessor,
    private val onResults: (FaceResult) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 30 == 0) {
            faceDetectorProcessor.processFace(image) {
                onResults(it)
                image.close()
            }
        } else {
            image.close()
        }
        frameSkipCounter++
    }
}