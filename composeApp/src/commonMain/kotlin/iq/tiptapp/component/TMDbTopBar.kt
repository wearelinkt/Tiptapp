package component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import iq.tiptapp.LightGray
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.app_name

@Composable
fun CustomTopAppBar(title: StringResource, onBackClicked: () -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(LightGray)
        ) {
            IconButton(
                onClick = onBackClicked,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        HorizontalDivider(
            color = Color.Black.copy(alpha = 0.15f),
            thickness = 1.dp,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiptappAppBar(content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
                modifier = Modifier.shadow(4.dp)
            )
        }, content = { padding ->
            content(padding)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiptappAppBarWithNavigation(
    title: StringResource,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(title)) },
                modifier = Modifier.shadow(4.dp),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }, content = { padding ->
            content(padding)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TMDbTopBar(scrollContent: @Composable (PaddingValues) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopBar(stringResource(Res.string.app_name), scrollBehavior)
        }
    ) {
        scrollContent(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TMDbDetailTopBar(title: String, onNavigationIconClick: () -> Unit) {
    Surface {
        CenterAlignedTopBar(title) {
            IconButton(
                onNavigationIconClick
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CenterAlignedTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            navigationIcon.invoke()
        },
        scrollBehavior = scrollBehavior
    )
}