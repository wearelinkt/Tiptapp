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
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import iq.tiptapp.help.HelpViewModel
import iq.tiptapp.search.SearchComponent
import iq.tiptapp.ui.AccountScreen
import iq.tiptapp.ui.AdsScreen
import iq.tiptapp.ui.HomeScreen
import iq.tiptapp.ui.MyAdsScreen
import iq.tiptapp.delivery.DeliveryDetailScreen
import iq.tiptapp.delivery.DeliveryScreen
import iq.tiptapp.help.DetailScreen
import iq.tiptapp.help.HelpScreen
import iq.tiptapp.location.LocationScreen
import iq.tiptapp.map.MapScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tiptapp.composeapp.generated.resources.Res
import tiptapp.composeapp.generated.resources.account
import tiptapp.composeapp.generated.resources.ads
import tiptapp.composeapp.generated.resources.create_ad
import tiptapp.composeapp.generated.resources.drop_off
import tiptapp.composeapp.generated.resources.drop_off_location
import tiptapp.composeapp.generated.resources.home
import tiptapp.composeapp.generated.resources.my_ads
import tiptapp.composeapp.generated.resources.pick_up
import tiptapp.composeapp.generated.resources.pick_up_location

@Composable
fun TiptappApp() {
    val appState = rememberTiptappAppState()
    val viewModel: HelpViewModel = koinViewModel<HelpViewModel>()
    Scaffold(
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                TiptappBottomBar(
                    tabs = appState.bottomBarTabs,
                    currentRoute = appState.currentRoute!!,
                    navigateToRoute = appState::navigateToBottomBarRoute,
                    appState.navController,
                    viewModel
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
            createAdScreens(appState.navController, viewModel)
        }
    }
}

@Composable
private fun TiptappBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    navController: NavController,
    viewModel: HelpViewModel
) {
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
                        Text(
                            text = stringResource(section.title),
                            fontSize = 11.sp,
                            color = if (selected) Color.DarkGray else Color.Gray
                        )
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
                    onClick = {
                        if (section == HomeSections.HELP_SECTION) {
                            viewModel.reset()
                            navController.navigate(CREATE_ADD_ROUTE)
                        } else {
                            navigateToRoute(section.route)
                        }
                    }

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
            // This route should not be reached; clicking it navigates directly to CREATE_ADD_ROUTE
            // But we define it to satisfy NavHost
        }
        composable(route = HomeSections.MY_ADS_SECTION.route) {
            MyAdsScreen()
        }
        composable(route = HomeSections.ACCOUNT_SECTION.route) {
            AccountScreen()
        }
    }
}

private fun NavGraphBuilder.createAdScreens(
    navController: NavController,
    viewModel: HelpViewModel
) {
    composable(
        route = CREATE_ADD_ROUTE
    ) {
        HelpScreen { navController.navigate(PICK_UP_LOCATION) }
    }
    composable(route = PICK_UP_LOCATION) {
        LocationScreen(Res.string.pick_up, { navController.navigateUp() },
            { navController.navigate(PICK_UP_ADDRESS_SEARCH_ROUTE) }) { lat, lng ->
            viewModel.onMarkerPickUpClicked(lat, lng)
            navController.navigate(PICK_UP_ROUTE)
        }
    }
    composable(route = PICK_UP_ADDRESS_SEARCH_ROUTE) {
        SearchComponent({ lat, lng ->
            navController.popBackStack()
            viewModel.onMarkerPickUpClicked(lat, lng)
            navController.navigate(PICK_UP_ROUTE)
        }, onDone = { navController.popBackStack() })
    }
    composable(route = PICK_UP_ROUTE) {
        MapScreen(
            Res.string.pick_up,
            Res.string.pick_up_location,
            viewModel.pickUpClickedLocation.collectAsState().value,
            { navController.navigate(PICK_UP_DELIVERY_ROUTE) },
            { navController.navigateUp() }) { lat, lng ->
            viewModel.onMarkerPickUpClicked(lat, lng)
        }
    }
    composable(route = PICK_UP_DELIVERY_ROUTE) {
        DeliveryScreen(Res.string.pick_up,
            { navController.navigateUp() },
            { deliveryItem -> viewModel.setPickUpDeliveryItem(deliveryItem) },
            viewModel.pickUpDeliveryItem.collectAsState().value,
            viewModel.pickUpToggleState.collectAsState().value,
            { state -> viewModel.setPickUpToggleState(state) },
            { navController.navigate(PICK_UP_DELIVERY_DETAIL_ROUTE) },
            { navController.navigate(DROP_OFF_LOCATION) })
    }
    composable(route = PICK_UP_DELIVERY_DETAIL_ROUTE) {
        DeliveryDetailScreen(Res.string.pick_up,
            { navController.navigateUp() },
            viewModel.pickUpFitElevator.collectAsState().value,
            { state -> viewModel.setPickUpFitElevator(state) },
            viewModel.pickUpFloor.collectAsState().value,
            { floor -> viewModel.setPickUpFloor(floor) },
            viewModel.pickUpDoorCode.collectAsState().value,
            { doorCode -> viewModel.setPickUpDoorCode(doorCode) },
            viewModel.pickUpInfo.collectAsState().value,
            { info -> viewModel.setPickUpOtherInfo(info) },
            { navController.navigate(DROP_OFF_LOCATION) })
    }
    composable(route = DROP_OFF_LOCATION) {
        LocationScreen(Res.string.drop_off, { navController.navigateUp() },
            { navController.navigate(DROP_OFF_ADDRESS_SEARCH_ROUTE) }) { lat, lng ->
            viewModel.onMarkerDropOffClicked(lat, lng)
            navController.navigate(DROP_OFF_ROUTE)
        }
    }
    composable(route = DROP_OFF_ADDRESS_SEARCH_ROUTE) {
        SearchComponent({ lat, lng ->
            navController.popBackStack()
            viewModel.onMarkerDropOffClicked(lat, lng)
            navController.navigate(DROP_OFF_ROUTE)
        }, onDone = { navController.popBackStack() })
    }
    composable(route = DROP_OFF_ROUTE) {
        MapScreen(
            Res.string.drop_off,
            Res.string.drop_off_location,
            viewModel.dropOffClickedLocation.collectAsState().value,
            { navController.navigate(DROP_OFF_DELIVERY_ROUTE) },
            { navController.navigateUp() }) { lat, lng ->
            viewModel.onMarkerDropOffClicked(lat, lng)
        }
    }
    composable(route = DROP_OFF_DELIVERY_ROUTE) {
        DeliveryScreen(Res.string.drop_off,
            { navController.navigateUp() },
            { deliveryItem -> viewModel.setDropOffDeliveryItem(deliveryItem) },
            viewModel.dropOffDeliveryItem.collectAsState().value,
            viewModel.dropOffToggleState.collectAsState().value,
            { state -> viewModel.setDropOffToggleState(state) },
            { navController.navigate(DROP_OFF_DELIVERY_DETAIL_ROUTE) },
            { navController.navigate(DETAIL_ROUTE) })
    }
    composable(route = DROP_OFF_DELIVERY_DETAIL_ROUTE) {
        DeliveryDetailScreen(Res.string.drop_off,
            { navController.navigateUp() },
            viewModel.dropOffFitElevator.collectAsState().value,
            { state -> viewModel.setDropOffFitElevator(state) },
            viewModel.dropOffFloor.collectAsState().value,
            { floor -> viewModel.setDropOffFloor(floor) },
            viewModel.dropOffDoorCode.collectAsState().value,
            { doorCode -> viewModel.setDropOffDoorCode(doorCode) },
            viewModel.dropOffInfo.collectAsState().value,
            { info -> viewModel.setDropOffOtherInfo(info) },
            { navController.navigate(DETAIL_ROUTE) })
    }
    composable(route = DETAIL_ROUTE) {
        DetailScreen()
    }
}

enum class HomeSections(
    val route: String,
    val title: StringResource,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
) {
    HOME_SECTION("home", Res.string.home, Icons.Outlined.Home, Icons.Filled.Home),
    ADS_SECTION(
        "ads",
        Res.string.ads,
        Icons.AutoMirrored.Outlined.ListAlt,
        Icons.AutoMirrored.Filled.ListAlt
    ),
    HELP_SECTION("help", Res.string.create_ad, Icons.Filled.Assistant, Icons.Filled.Assistant),
    MY_ADS_SECTION(
        "my_ads",
        Res.string.my_ads,
        Icons.Outlined.CollectionsBookmark,
        Icons.Filled.CollectionsBookmark
    ),
    ACCOUNT_SECTION("account", Res.string.account, Icons.Outlined.Person, Icons.Filled.Person),
}

private const val HOME_ROUTE = "home_route"
private const val CREATE_ADD_ROUTE = "create_ad_route"
private const val PICK_UP_LOCATION = "pick_up_location_route"
private const val PICK_UP_ROUTE = "pick_up_route"
private const val PICK_UP_ADDRESS_SEARCH_ROUTE = "pick_up_address"
private const val PICK_UP_DELIVERY_ROUTE = "pick_up_delivery"
private const val PICK_UP_DELIVERY_DETAIL_ROUTE = "pick_up_delivery_detail"
private const val DROP_OFF_LOCATION = "drop_off_location_route"
private const val DROP_OFF_ROUTE = "drop_off_route"
private const val DROP_OFF_ADDRESS_SEARCH_ROUTE = "drop_off_address"
private const val DROP_OFF_DELIVERY_ROUTE = "drop_off_delivery"
private const val DROP_OFF_DELIVERY_DETAIL_ROUTE = "drop_off_delivery_detail"
private const val DETAIL_ROUTE = "detail_route"