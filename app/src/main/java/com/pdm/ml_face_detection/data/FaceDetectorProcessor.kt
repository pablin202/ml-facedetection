package com.pdm.ml_face_detection.data

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.pdm.ml_face_detection.domain.FaceDetectorProcessor
import com.pdm.ml_face_detection.domain.FaceResult
import com.pdm.ml_face_detection.domain.HeadPosition

class MLKitFaceDetectorProcessor(
) : FaceDetectorProcessor {

    private val detector: FaceDetector

    init {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(MIN_FACE_SIZE)
            .enableTracking()
            .build()

        detector = FaceDetection.getClient(options)
    }

    @OptIn(ExperimentalGetImage::class)
    override fun processFace(image: ImageProxy, result: (FaceResult) -> Unit) {
        detectInImage(InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees))
            .addOnSuccessListener { faces ->
                if (faces.isNotEmpty()) {
                    result(getFaceResults(faces.first()))
                } else {
                    result(FaceResult())
                }
            }
            .addOnFailureListener { ex: Exception ->
                ex.message?.let { Log.d("Face detection failed %s", it) }
            }
            .addOnCompleteListener {
                // image.close()
            }
    }

    private fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    fun stop() {
        detector.close()
    }

    private fun getFaceResults(face: Face): FaceResult {
        return try {

            var faceResult = FaceResult(faceVisible = true)

            face.smilingProbability?.let {
                faceResult = faceResult.copy(faceNeutralExpression = it <= 0.2f)
            }

            face.leftEyeOpenProbability?.let {
                faceResult = faceResult.copy(leftEyeOpen = it <= 0.6f)
            }

            face.rightEyeOpenProbability?.let {
                faceResult = faceResult.copy(rightEyeOpen = it <= 0.6f)
            }

            face.headEulerAngleY.let {
                when {
                    it < -5.5f -> {
                        faceResult = faceResult.copy(
                            headPosition = HeadPosition(
                                isValid = false,
                                turnLeft = true
                            )
                        )
                    }

                    it > 5.5f -> {
                        faceResult = faceResult.copy(
                            headPosition = HeadPosition(
                                isValid = false,
                                turnRight = true
                            )
                        )
                    }

                    else -> {
                        //
                    }
                }
            }

            if (!faceResult.headPosition.isValid) {
                return faceResult
            }

            face.headEulerAngleX.let {
                when {
                    it < -11.5f -> {
                        faceResult = faceResult.copy(
                            headPosition = HeadPosition(
                                isValid = false,
                                tiltUp = true
                            )
                        )
                    }

                    it > 7.5f -> {
                        faceResult = faceResult.copy(
                            headPosition = HeadPosition(
                                isValid = false,
                                tiltDown = true
                            )
                        )
                    }

                    else -> {
                        //
                    }
                }
            }

            if (!faceResult.headPosition.isValid) {
                return faceResult
            }

            face.headEulerAngleZ.let {
                when {
                    it < -3.5f -> {
                        faceResult = faceResult.copy(
                            headPosition = HeadPosition(
                                isValid = false,
                                rotateLeft = true
                            )
                        )
                    }

                    it > 3.5f -> {
                        faceResult = faceResult.copy(
                            headPosition = HeadPosition(
                                isValid = false,
                                rotateRight = true
                            )
                        )
                    }

                    else -> {
                        faceResult = faceResult.copy(
                            headPosition = HeadPosition(
                                isValid = true,
                            )
                        )
                    }
                }
            }

            faceResult

        } catch (ex: Exception) {
            ex.printStackTrace()
            FaceResult()
        }
    }

    companion object {
        private const val TAG = "FaceDetectorProcessorX"
        private const val MIN_FACE_SIZE = 0.15f
    }

}