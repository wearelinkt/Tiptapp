package iq.tiptapp.help

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kashif.cameraK.permissions.Permissions
import com.kashif.cameraK.permissions.providePermissions
import iq.tiptapp.camera.CameraPermissionViewModel
import iq.tiptapp.camera.CameraScreen
import iq.tiptapp.camera.isTiramisuOrHigher
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.camera_permission_denied
import tiptapp.composeapp.generated.resources.storage_permission_denied

@Composable
fun HelpScreen(
    viewModel: CameraPermissionViewModel = koinViewModel<CameraPermissionViewModel>(),
    onContinueClick: () -> Unit
) {
    val permissions: Permissions = providePermissions()
    CameraPermissionContent(permissions, viewModel, onContinueClick)
}

@Composable
private fun CameraPermissionContent(
    permissions: Permissions,
    viewModel: CameraPermissionViewModel,
    onContinueClick: () -> Unit
) {
    when (val state = viewModel.cameraPermissionState.collectAsState().value) {
        is CameraPermissionViewModel.PermissionState.Dialog -> {
            permissions.RequestCameraPermission(
                onGranted = {
                    viewModel.setCameraState(CameraPermissionViewModel.PermissionState.Granted)
                },
                onDenied = {
                    viewModel.setCameraState(CameraPermissionViewModel.PermissionState.Denied(Res.string.camera_permission_denied))
                }
            )
        }

        is CameraPermissionViewModel.PermissionState.Granted -> {
            if (isTiramisuOrHigher()) {
                CameraScreen(onContinueClick)
            } else {
                StoragePermissionContent(permissions, viewModel, onContinueClick)
            }

        }

        is CameraPermissionViewModel.PermissionState.Denied -> {
            PermissionDenied(state.reason)
        }
    }
}

@Composable
private fun StoragePermissionContent(
    permissions: Permissions,
    viewModel: CameraPermissionViewModel,
    onContinueClick: () -> Unit
) {
    when (val state = viewModel.storagePermissionState.collectAsState().value) {
        is CameraPermissionViewModel.PermissionState.Dialog -> {
            permissions.RequestStoragePermission(
                onGranted = {
                    viewModel.setStorageState(CameraPermissionViewModel.PermissionState.Granted)
                },
                onDenied = {
                    viewModel.setStorageState(CameraPermissionViewModel.PermissionState.Denied(Res.string.storage_permission_denied))
                }
            )
        }

        is CameraPermissionViewModel.PermissionState.Granted -> {
            CameraScreen(onContinueClick)
        }

        is CameraPermissionViewModel.PermissionState.Denied -> {
            PermissionDenied(state.reason)
        }
    }
}

@Composable
private fun PermissionDenied(stringResource: StringResource) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            stringResource(stringResource),
            color = Color.DarkGray,
            modifier = Modifier.padding(8.dp),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )
    }
}