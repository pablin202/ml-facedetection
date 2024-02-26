package com.pdm.ml_face_detection.domain.models

data class FaceResult(
    val faceVisible: Boolean = false,
    val faceNeutralExpression: Boolean = false,
    val leftEyeOpen: Boolean = false,
    val rightEyeOpen: Boolean = false,
    val headPosition: HeadPosition = HeadPosition()
) {
    val requirementsMeet: Boolean
        get() = faceVisible && faceNeutralExpression && leftEyeOpen
                && rightEyeOpen && headPosition.isValid
}
