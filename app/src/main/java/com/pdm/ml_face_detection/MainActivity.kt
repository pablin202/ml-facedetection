package com.pdm.ml_face_detection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pdm.ml_face_detection.presentation.MainScreen
import com.pdm.ml_face_detection.presentation.MainViewModel
import com.pdm.ml_face_detection.ui.theme.MlfacedetectionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val mainViewModel: MainViewModel by viewModels()

        setContent {
            MlfacedetectionTheme {
                MainScreen(viewModel = mainViewModel)
            }
        }
    }

}
