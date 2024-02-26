package com.pdm.ml_face_detection.domain.models

data class HeadPosition(
    val isValid: Boolean = true,
    val rotateLeft: Boolean = false,
    val rotateRight: Boolean = false,
    val tiltUp: Boolean = false,
    val tiltDown: Boolean = false,
    val turnLeft: Boolean = false,
    val turnRight: Boolean = false,
)
