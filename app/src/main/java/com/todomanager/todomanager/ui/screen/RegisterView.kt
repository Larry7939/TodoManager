package com.todomanager.todomanager.ui.screen

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coil.compose.rememberAsyncImagePainter
import com.todomanager.todomanager.R
import com.todomanager.todomanager.constant.ArgumentKey.PROFILE_IMAGE_KEY
import com.todomanager.todomanager.constant.Destination.CAMERA
import com.todomanager.todomanager.ui.button.CtaButton
import com.todomanager.todomanager.ui.dialog.PickerDialog
import com.todomanager.todomanager.ui.textfield.InputTextField
import com.todomanager.todomanager.ui.theme.B1
import com.todomanager.todomanager.ui.theme.Typography
import com.todomanager.todomanager.util.Utils.requestPermission


class RegisterView {

    @Composable
    fun RegisterScreen(navController: NavHostController) {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        var nameTextLength by remember { mutableIntStateOf(0) }
        val keyboardController = LocalSoftwareKeyboardController.current
        var isDatePickerDialogVisible by remember { mutableStateOf(false) }
        var date by remember { mutableStateOf("") }
        var isRegisterEnable by remember { mutableStateOf(false) }

        var profileUri: String? by remember { mutableStateOf("") }
        profileUri = navController.currentBackStackEntry?.arguments?.getString(PROFILE_IMAGE_KEY)

        LaunchedEffect(nameTextLength, date) {
            isRegisterEnable = nameTextLength > 0 && date.isNotEmpty()
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
                    hint = stringResource(id = R.string.input_name),
                    maxLength = INPUT_NAME_MAX_LENGTH,
                    focusRequester = focusRequester,
                    focusManager = focusManager
                ) { text ->
                    nameTextLength = text.length
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 5.dp, end = 40.dp),
                    text = "$nameTextLength / $INPUT_NAME_MAX_LENGTH",
                    style = Typography.displaySmall,
                    color = B1
                )
                Spacer(modifier = Modifier.height(15.dp))
                InputTextField().DateTextField(date) {
                    removeInputNameFocus(keyboardController, focusManager)
                    isDatePickerDialogVisible = true
                }
                Spacer(modifier = Modifier.height(190.dp))
                CtaButton().TextButton(
                    stringResource(id = R.string.cta_register),
                    isRegisterEnable
                ) { }
            }
            if (isDatePickerDialogVisible) {
                PickerDialog().CustomDatePickerDialog(
                    isFutureSelectable = false,
                    onDateSelected = { date = it },
                    onDismiss = { isDatePickerDialogVisible = false })
            }
        }
    }

    private fun removeInputNameFocus(
        keyboardController: SoftwareKeyboardController?,
        focusManager: FocusManager
    ) {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

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
                    scaleX = -1f
                }
        }

        Spacer(modifier = Modifier.height(85.dp))
        Box(
            modifier = Modifier
                .wrapContentSize()
                .size(150.dp)
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
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                contentScale = ContentScale.Crop,
                contentDescription = "photo_profile"
            )
        }
    }

    companion object {
        const val INPUT_NAME_MAX_LENGTH = 15
    }
}