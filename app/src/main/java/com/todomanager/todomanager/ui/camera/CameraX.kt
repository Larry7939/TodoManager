package com.todomanager.todomanager.util

import android.content.Context
import android.net.Uri
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.todomanager.todomanager.util.Utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface CameraX {

    fun initialize(context: Context)
    fun startCamera(lifecycleOwner: LifecycleOwner)
    fun takePicture(storagePath: String, getUri: (Uri) -> Unit)
    fun flipCameraFacing()
    fun unBindCamera()
    fun getPreviewView(): PreviewView
    fun getFacingState(): StateFlow<Int>

}

class CameraXImpl : CameraX {

    private val _facing = MutableStateFlow(CameraSelector.LENS_FACING_FRONT) // 전면 카메라 선택

    private lateinit var previewView: PreviewView
    private lateinit var preview: Preview
    private lateinit var cameraProvider: ListenableFuture<ProcessCameraProvider>
    private lateinit var provider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var context: Context
    private lateinit var executor: ExecutorService
    private lateinit var imageCapture: ImageCapture

    override fun initialize(context: Context) {
        previewView = PreviewView(context)
        preview =
            Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
        cameraProvider = ProcessCameraProvider.getInstance(context)
        provider = cameraProvider.get()
        imageCapture = ImageCapture.Builder().build()
        executor = Executors.newSingleThreadExecutor()
        this.context = context
    }

    override fun startCamera(
        lifecycleOwner: LifecycleOwner,
    ) {
        unBindCamera()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(_facing.value).build()
        cameraProvider.addListener({
            CoroutineScope(Dispatchers.Main).launch {
                camera = provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            }
        }, executor)
    }

    /**
     * 사진 촬영 함수
     * - 사진 촬영 및 저장
     * - getUri 콜백을 통해 uri(이미지가 저장된 앱 내부 경로) 반환
     * - 이후 사용자 등록 뷰로 uri 전달 및 navigate
     * */
    override fun takePicture(
        storagePath: String,
        getUri: (Uri) -> Unit
    ) {
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        val photoFile = File(storagePath, fileName)

        val photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )

        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
            launch {
                imageCapture.takePicture(outputFileOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(error: ImageCaptureException) {
                            error.printStackTrace()
                        }

                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            context.showToast("촬영 완료!")
                            devTimberLog { "jh: Capture Success!! Image Saved at  \n [${storagePath}]" }
                            unBindCamera()
                            getUri(photoUri)
                        }
                    })
            }
        }
    }

    /**
     * 카메라 방향 설정 함수
     * */
    override fun flipCameraFacing() {
        if (_facing.value == CameraSelector.LENS_FACING_BACK) {
            _facing.value = CameraSelector.LENS_FACING_FRONT
        } else {
            _facing.value = CameraSelector.LENS_FACING_BACK
        }
    }

    /**
     * 카메라 리소스 해제
     * */
    override fun unBindCamera() {
        provider.unbindAll()
    }

    override fun getPreviewView(): PreviewView = previewView
    override fun getFacingState(): StateFlow<Int> = _facing.asStateFlow()
}