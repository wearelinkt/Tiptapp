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
import androidx.compose.ui.unit.dp
import com.kashif.cameraK.controller.CameraController
import com.kashif.cameraK.enums.CameraLens
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.enums.ImageFormat
import com.kashif.cameraK.enums.QualityPrioritization
import com.kashif.cameraK.enums.TorchMode
import com.kashif.cameraK.result.ImageCaptureResult
import com.kashif.cameraK.ui.CameraPreview
import com.kashif.imagesaverplugin.ImageSaverConfig
import com.kashif.imagesaverplugin.ImageSaverPlugin
import com.kashif.imagesaverplugin.rememberImageSaverPlugin
import io.github.aakira.napier.Napier
import iq.tiptapp.Turquoise
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.continue_text
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun CameraScreen(onContinueClick: () -> Unit) {
    val cameraController = remember { mutableStateOf<CameraController?>(null) }
    val imageSaverPlugin = rememberImageSaverPlugin(
        config = ImageSaverConfig(
            isAutoSave = false,
            prefix = "MyApp",
            directory = Directory.PICTURES,
            customFolderName = "Tiptapp"
        )
    )

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
                            .size(72.dp)
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
            onClick = { onContinueClick.invoke() },
            //enabled = imageSlots[selectedSlot] != null,
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
                    Napier.d("Image saved at: $path")
                }
            }
            return bitmap
        }

        is ImageCaptureResult.Error -> {
            Napier.d("Image Capture Error: ${result.exception.message}")
            return null
        }
    }
}

private const val maxSlots = 4