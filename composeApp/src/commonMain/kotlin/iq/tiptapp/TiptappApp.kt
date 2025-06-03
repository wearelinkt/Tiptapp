package iq.tiptapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Assistant
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import iq.tiptapp.ui.AccountScreen
import iq.tiptapp.ui.AdsScreen
import iq.tiptapp.ui.HelpScreen
import iq.tiptapp.ui.HomeScreen
import iq.tiptapp.ui.MyAdsScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.account
import tiptapp.composeapp.generated.resources.ads
import tiptapp.composeapp.generated.resources.create_ad
import tiptapp.composeapp.generated.resources.home
import tiptapp.composeapp.generated.resources.my_ads

@Composable
fun TiptappApp() {
    val appState = rememberTiptappAppState()
    Scaffold(
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                TiptappBottomBar(
                    tabs = appState.bottomBarTabs,
                    currentRoute = appState.currentRoute!!,
                    navigateToRoute = appState::navigateToBottomBarRoute,
                )
            }
        },
    ) { padding ->
        NavHost(
            navController = appState.navController,
            startDestination = HOME_ROUTE,
            modifier = Modifier.padding(padding),
        ) {
            navigationScreens()
        }
    }
}

@Composable
private fun TiptappBottomBar(tabs: Array<HomeSections>, currentRoute: String, navigateToRoute: (String) -> Unit) {
    val currentSection = tabs.first { it.route == currentRoute }

    Box(
        Modifier.navigationBarsPadding(),
    ) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colorScheme.background,
            elevation = 8.dp,
        ) {
            tabs.forEach { section ->
                val selected = section == currentSection
                BottomNavigationItem(
                    label = {
                        Text(text = stringResource(section.title),
                            fontSize = 11.sp,
                            color = if (selected) Color.DarkGray else Color.Gray)
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) section.selectedIcon else section.unselectedIcon,
                            contentDescription = stringResource(section.title),
                            tint = if (selected) Color.DarkGray else Color.Gray
                        )
                    },
                    selected = selected,
                    unselectedContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.disabled),
                    selectedContentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = { navigateToRoute(section.route) },
                )
            }
        }
    }
}

private fun NavGraphBuilder.navigationScreens() {
    navigation(
        route = HOME_ROUTE,
        startDestination = HomeSections.HOME_SECTION.route,
    ) {
        composable(route = HomeSections.HOME_SECTION.route) {
            HomeScreen()
        }
        composable(route = HomeSections.ADS_SECTION.route) {
            AdsScreen()
        }
        composable(route = HomeSections.HELP_SECTION.route) {
            HelpScreen()
        }
        composable(route = HomeSections.MY_ADS_SECTION.route) {
            MyAdsScreen()
        }
        composable(route = HomeSections.ACCOUNT_SECTION.route) {
            AccountScreen()
        }
    }
}

enum class HomeSections(
    val route: String,
    val title: StringResource,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
) {
    HOME_SECTION("home", Res.string.home, Icons.Outlined.Home, Icons.Filled.Home),
    ADS_SECTION("ads", Res.string.ads , Icons.AutoMirrored.Outlined.ListAlt, Icons.AutoMirrored.Filled.ListAlt),
    HELP_SECTION("help", Res.string.create_ad, Icons.Outlined.Assistant, Icons.Filled.Assistant),
    MY_ADS_SECTION("my_ads", Res.string.my_ads, Icons.Outlined.CollectionsBookmark, Icons.Filled.CollectionsBookmark),
    ACCOUNT_SECTION("account", Res.string.account, Icons.Outlined.Person, Icons.Filled.Person),
}

private const val HOME_ROUTE = "home_route"