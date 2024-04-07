package com.todomanager.todomanager.ui.screen

import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.todomanager.todomanager.R
import com.todomanager.todomanager.ui.theme.Typography
import com.todomanager.todomanager.util.CameraXFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraView {
    @Composable
    fun CameraScreen(getUri: (Uri) -> Unit) {
        val storagePath = LocalContext.current.filesDir.absolutePath // 앱 내부 저장소 경로
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraScope = rememberCoroutineScope()
        val context = LocalContext.current
        val cameraX = remember { CameraXFactory.create() }
        val previewView = remember { mutableStateOf<PreviewView?>(null) }
        val facing = cameraX.getFacingState().collectAsState()
        LaunchedEffect(Unit) {
            cameraScope.launch(Dispatchers.Main) {
                // 호출 순서 주의 - preview 이전에 initialize와 startCamera가 호출되어야 함.
                cameraX.initialize(context = context)
                cameraX.startCamera(lifecycleOwner = lifecycleOwner)
                previewView.value = cameraX.getPreviewView()
            }
        }
        // Composable 소멸 시
        DisposableEffect(facing.value) {
            onDispose {
                cameraX.unBindCamera() // 카메라 리소스 해제
            }
        }
        Box(Modifier.fillMaxSize()) {
            previewView.value?.let { preview ->
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { preview }) {}
            }
            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    cameraX.takePicture(storagePath, getUri)
                }
            ) {
                Text(stringResource(id = R.string.take_picture), style = Typography.displayMedium)
            }
        }
    }
}