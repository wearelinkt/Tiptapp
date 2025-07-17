package iq.tiptapp.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import iq.tiptapp.Turquoise
import iq.tiptapp.component.CustomTopAppBar
import iq.tiptapp.help.HelpViewModel
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.continue_text
import tiptapp.composeapp.generated.resources.description
import tiptapp.composeapp.generated.resources.enter_description
import tiptapp.composeapp.generated.resources.enter_title
import tiptapp.composeapp.generated.resources.large_size
import tiptapp.composeapp.generated.resources.medium_size
import tiptapp.composeapp.generated.resources.size
import tiptapp.composeapp.generated.resources.small_size
import tiptapp.composeapp.generated.resources.title
import tiptapp.composeapp.generated.resources.title_description
import tiptapp.composeapp.generated.resources.xlarge_size

@Composable
fun DetailScreen(
    viewModel: HelpViewModel,
    onBackClicked: () -> Unit
) {
    val sizeOptions = listOf("S", "M", "L", "XL")

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            CustomTopAppBar(Res.string.title_description, onBackClicked)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.title),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                TextField(
                    value = viewModel.title,
                    onValueChange = { viewModel.setAddTitle(it) },
                    singleLine = true,
                    placeholder = {
                        Text(
                            stringResource(Res.string.enter_title),
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.description), fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                TextField(
                    value = viewModel.description,
                    onValueChange = { viewModel.setAddDescription(it) },
                    placeholder = {
                        Text(
                            stringResource(Res.string.enter_description),
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(120.dp)
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.size), fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // Size Options
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    sizeOptions.forEach { size ->
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .border(
                                    width = 2.dp,
                                    color = if (viewModel.selectedSize == size) Turquoise else Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.setSize(size) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = size, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                viewModel.selectedSize?.let {
                    Text(
                        text = stringResource(
                            when (it) {
                                "S" -> Res.string.small_size
                                "M" -> Res.string.medium_size
                                "L" -> Res.string.large_size
                                "XL" -> Res.string.xlarge_size
                                else -> throw RuntimeException("Unknown size state")
                            }
                        ), style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Turquoise),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(Res.string.continue_text))
        }
    }
}