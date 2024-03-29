package com.pdm.ml_face_detection.presentation

import androidx.camera.core.CameraSelector
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm.ml_face_detection.domain.models.FaceResult
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state: MutableState<UIState> = mutableStateOf(UIState())

    val uiState: State<UIState> = _state

    fun updateFaceDetection(faceResult: FaceResult) {
        viewModelScope.launch {
            _state.value = _state.value.copy(faceResult = faceResult)
        }
    }

    fun rotateCamera() {
        if (_state.value.camera == CameraSelector.LENS_FACING_BACK) {
            _state.value = _state.value.copy(camera = CameraSelector.LENS_FACING_FRONT)
        } else {
            _state.value = _state.value.copy(camera = CameraSelector.LENS_FACING_BACK)
        }
    }
}