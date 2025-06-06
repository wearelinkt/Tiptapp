package iq.tiptapp.ui.help

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kashif.cameraK.controller.CameraController
import com.kashif.cameraK.enums.CameraLens
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.enums.ImageFormat
import com.kashif.cameraK.enums.QualityPrioritization
import com.kashif.cameraK.enums.TorchMode
import com.kashif.cameraK.permissions.Permissions
import com.kashif.cameraK.permissions.providePermissions
import com.kashif.cameraK.result.ImageCaptureResult
import com.kashif.cameraK.ui.CameraPreview
import com.kashif.imagesaverplugin.ImageSaverConfig
import com.kashif.imagesaverplugin.ImageSaverPlugin
import com.kashif.imagesaverplugin.rememberImageSaverPlugin
import iq.tiptapp.Turquoise
import iq.tiptapp.permissions.isTiramisuOrHigher
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.camera_permission_denied
import tiptapp.composeapp.generated.resources.continue_text
import tiptapp.composeapp.generated.resources.storage_permission_denied
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun HelpScreen(
    viewModel: PermissionViewModel = koinViewModel<PermissionViewModel>(),
) {
    val permissions: Permissions = providePermissions()

    CameraPermissionContent(permissions, viewModel)
}

@Composable
private fun CameraPermissionContent(
    permissions: Permissions,
    viewModel: PermissionViewModel,
) {
    val state by viewModel.cameraPermissionState.collectAsState()

    when (state) {
        is PermissionViewModel.PermissionState.Dialog -> {
            permissions.RequestCameraPermission(
                onGranted = {
                    viewModel.setCameraState(PermissionViewModel.PermissionState.Granted)
                },
                onDenied = {
                    viewModel.setCameraState(PermissionViewModel.PermissionState.Denied(""))
                }
            )
        }

        is PermissionViewModel.PermissionState.Granted -> {
            if (isTiramisuOrHigher()) {
                CameraScreen()
            } else {
                StoragePermissionContent(permissions, viewModel)
            }

        }

        is PermissionViewModel.PermissionState.Denied -> {
            PermissionDenied(Res.string.camera_permission_denied)
        }
    }
}

@Composable
private fun StoragePermissionContent(
    permissions: Permissions,
    viewModel: PermissionViewModel,
) {
    val state by viewModel.storagePermissionState.collectAsState()

    when (state) {
        is PermissionViewModel.PermissionState.Dialog -> {
            permissions.RequestStoragePermission(
                onGranted = {
                    viewModel.setStorageState(PermissionViewModel.PermissionState.Granted)
                },
                onDenied = {
                    viewModel.setStorageState(PermissionViewModel.PermissionState.Denied(""))
                }
            )
        }

        is PermissionViewModel.PermissionState.Granted -> {
            CameraScreen()
        }

        is PermissionViewModel.PermissionState.Denied -> {
            PermissionDenied(Res.string.storage_permission_denied)

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

@Composable
fun CameraScreen() {
    val cameraController = remember { mutableStateOf<CameraController?>(null) }
    val imageSaverPlugin = rememberImageSaverPlugin(
        config = ImageSaverConfig(
            isAutoSave = false,
            prefix = "MyApp",
            directory = Directory.PICTURES,
            customFolderName = "Tiptapp"
        )
    )

    val maxSlots = 4
    val imageSlots = remember { mutableStateListOf<ImageBitmap?>(null, null, null, null) }
    var selectedSlot by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // Space for button
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Camera Preview
            Box(
                Modifier
                    .height(425.dp)
                    .aspectRatio(3f / 4f)
            ) {
                imageSlots[selectedSlot]?.let { imageSlot ->
                    Image(
                        bitmap = imageSlot,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )

                    IconButton(
                        onClick = {
                            imageSlots[selectedSlot] = null
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                } ?: run {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        cameraConfiguration = {
                            setCameraLens(CameraLens.BACK)
                            setFlashMode(FlashMode.OFF)
                            setImageFormat(ImageFormat.JPEG)
                            setDirectory(Directory.PICTURES)
                            setTorchMode(TorchMode.OFF)
                            setQualityPrioritization(QualityPrioritization.NONE)
                            addPlugin(imageSaverPlugin)
                        },
                        onCameraControllerReady = {
                            cameraController.value = it
                        }
                    )
                }
            }

            // Image Slots
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 0 until maxSlots) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .border(
                                width = if (i == selectedSlot) 2.dp else 1.dp,
                                color = if (i == selectedSlot) Turquoise else Color.Gray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedSlot = i },
                        contentAlignment = Alignment.Center
                    ) {
                        val bmp = imageSlots[i]
                        bmp?.let {
                            Image(
                                bitmap = bmp,
                                contentDescription = "Image $i",
                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: run {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }

            // Capture Button
            cameraController.value?.let { controller ->
                BottomControls(
                    modifier = Modifier
                        .padding(top = 48.dp)
                        .align(Alignment.CenterHorizontally),
                    onCapture = {
                        scope.launch {
                            imageSlots[selectedSlot] = handleImageCapture(
                                controller,
                                imageSaverPlugin
                            )
                        }
                    },
                    enabled = imageSlots[selectedSlot] == null,
                )
            }
        }

        // Continue button anchored to bottom
        Button(
            onClick = { /* Navigate or next step */ },
            enabled = imageSlots[selectedSlot] != null,
            colors = ButtonDefaults.buttonColors(containerColor = Turquoise),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(stringResource(Res.string.continue_text))
        }
    }
}


@Composable
private fun BottomControls(
    modifier: Modifier = Modifier,
    onCapture: () -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FilledTonalButton(
            onClick = onCapture,
            enabled = enabled,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = Turquoise
            )
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Capture",
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
private suspend fun handleImageCapture(
    cameraController: CameraController,
    imageSaverPlugin: ImageSaverPlugin,
): ImageBitmap? {
    when (val result = cameraController.takePicture()) {
        is ImageCaptureResult.Success -> {
            val bitmap = result.byteArray.decodeToImageBitmap()

            if (!imageSaverPlugin.config.isAutoSave) {
                val customName = "Manual_${Uuid.random().toHexString()}"
                imageSaverPlugin.saveImage(
                    byteArray = result.byteArray,
                    imageName = customName
                )?.let { path ->
                    println("Image saved at: $path")
                }
            }
            return bitmap
        }

        is ImageCaptureResult.Error -> {
            println("Image Capture Error: ${result.exception.message}")
            return null
        }
    }
}