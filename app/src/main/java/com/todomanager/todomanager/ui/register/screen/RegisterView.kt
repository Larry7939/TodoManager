package com.todomanager.todomanager.ui.register.screen

import android.Manifest
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.todomanager.todomanager.R
import com.todomanager.todomanager.constant.Destination
import com.todomanager.todomanager.constant.Destination.CAMERA
import com.todomanager.todomanager.constant.Destination.REGISTER_WITH_ARG
import com.todomanager.todomanager.constant.IOState.FAILURE
import com.todomanager.todomanager.constant.IOState.IDLE
import com.todomanager.todomanager.constant.IOState.SUCCESS
import com.todomanager.todomanager.constant.NavArgKey.PROFILE_IMAGE_KEY
import com.todomanager.todomanager.model.Profile
import com.todomanager.todomanager.ui.button.CtaButton
import com.todomanager.todomanager.ui.dialog.PickerDialog
import com.todomanager.todomanager.ui.screen.RegisterViewModel
import com.todomanager.todomanager.ui.textfield.InputTextField
import com.todomanager.todomanager.ui.theme.B1
import com.todomanager.todomanager.ui.theme.Typography
import com.todomanager.todomanager.util.Utils.requestPermission
import com.todomanager.todomanager.util.Utils.showToast

class RegisterView {

    /**
     * 등록 성공 혹은 실패 이벤트 수행
     *  - 성공 시, 사용자 등록 완료 뷰로 navigate
     *  - 실패 시, 토스트 메시지 노출
     *  - 리컴포지션으로 인한 중복 호출 방지를 위해 setProfileState를 IDLE로 update
     */
    @Composable
    fun AddObserver(navController: NavHostController, registerViewModel: RegisterViewModel) {
        val setProfileState by registerViewModel.setProfileState.collectAsState()
        if (setProfileState == SUCCESS) {
            registerViewModel.setIsRegistered(true) // 등록 여부 플래그 값 로컬 저장
            navController.navigate(Destination.REGISTER_COMPLETE)
            registerViewModel.updateSetProfileState(IDLE)
        } else if (setProfileState == FAILURE) {
            LocalContext.current.showToast(stringResource(id = R.string.register_failure))
            registerViewModel.updateSetProfileState(IDLE)
        }
    }

    /**
     * 사용자 등록 뷰 (앱 설치 후 최초 노출)
     */
    @Composable
    fun RegisterScreen(navController: NavHostController, registerViewModel: RegisterViewModel) {
        AddObserver(navController, registerViewModel)

        // Name TextField Focus 상태 관리 변수
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        var name by rememberSaveable { mutableStateOf("") }
        var nameLength by rememberSaveable { mutableIntStateOf(0) }
        val keyboardController = LocalSoftwareKeyboardController.current
        var isDatePickerDialogVisible by remember { mutableStateOf(false) }
        var date by rememberSaveable { mutableStateOf("") }
        var isRegisterEnable by remember { mutableStateOf(false) } // Register CTA 버튼 enabled 상태

        var profileUri: String? by rememberSaveable { mutableStateOf("") }
        val currentBackStackEntry by navController.currentBackStackEntryAsState()

        // 카메라 뷰로부터 전달된 Argument로 프로필 이미지 Uri 로드
        if (currentBackStackEntry?.destination?.route == REGISTER_WITH_ARG) {
            profileUri =
                navController.currentBackStackEntry?.arguments?.getString(PROFILE_IMAGE_KEY)
        }

        // 이름, 생일, 프로필 사진이 모두 있어야 등록 가능함.
        LaunchedEffect(nameLength, date, profileUri) {
            isRegisterEnable =
                nameLength > 0 && date.isNotEmpty() && profileUri.isNullOrEmpty().not()
        }
        Surface(modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { removeInputNameFocus(keyboardController, focusManager) }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage(LocalContext.current, profileUri) { navController.navigate(CAMERA) }
                Spacer(modifier = Modifier.height(80.dp))
                InputTextField().CustomOutlinedTextField(
                    text = name,
                    hint = stringResource(id = R.string.input_name),
                    maxLength = INPUT_NAME_MAX_LENGTH,
                    focusRequester = focusRequester,
                    focusManager = focusManager
                ) { text ->
                    name = text
                    nameLength = text.length
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 5.dp, end = 40.dp),
                    text = "$nameLength / $INPUT_NAME_MAX_LENGTH",
                    style = Typography.displaySmall,
                    color = B1
                )
                Spacer(modifier = Modifier.height(15.dp))
                InputTextField().DateTextField(
                    stringResource(id = R.string.input_Birthday),
                    date = date
                ) {
                    removeInputNameFocus(keyboardController, focusManager)
                    isDatePickerDialogVisible = true
                }
                Spacer(modifier = Modifier.weight(1f))
                CtaButton().TextButton(
                    stringResource(id = R.string.cta_register),
                    isRegisterEnable
                ) {
                    registerViewModel.setProfile(
                        Profile(
                            uri = profileUri ?: "",
                            name = name,
                            birthday = date
                        )
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
            if (isDatePickerDialogVisible) {
                PickerDialog().CustomDatePickerDialog(
                    pattern = "yyyy.MM.dd",
                    isFutureSelectable = false,
                    isPastSelectable = true,
                    onDateSelected = { date = it },
                    onDismiss = { isDatePickerDialogVisible = false })
            }
        }
    }

    /**
     * TextFiled 제외 다른 뷰 클릭 또는 키보드 완료 버튼 클릭 시 키보드 숨김 및 TextFieldFocus 해제
     */
    private fun removeInputNameFocus(
        keyboardController: SoftwareKeyboardController?,
        focusManager: FocusManager
    ) {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    /**
     * 프로필 이미지 뷰
     * 클릭 시 카메라 뷰로 navigate 동작
     * */
    @Composable
    fun ProfileImage(context: Context, uri: String?, onClick: () -> Unit) {
        val painter = if (uri.isNullOrEmpty()) {
            painterResource(id = R.drawable.ic_photo_profile)
        } else {
            rememberAsyncImagePainter(uri)
        }

        val modifier = if (uri.isNullOrEmpty()) {
            Modifier
        } else {
            Modifier
                .fillMaxSize()

                .graphicsLayer {
                    scaleX = -1f // 전면 카메라 촬영 이미지 좌우 반전
                }
        }

        Spacer(modifier = Modifier.height(85.dp))
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(B1)
                .clickable {
                    requestPermission(context = context, permission = Manifest.permission.CAMERA) {
                        onClick()
                    }
                }
        ) {
            Image(
                modifier = modifier.align(Alignment.Center),
                painter = painter,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }), // 프로필 이미지 흑백 처리
                contentScale = ContentScale.Crop,
                contentDescription = "photo_profile"
            )
        }
    }

    companion object {
        const val INPUT_NAME_MAX_LENGTH = 15
    }
}