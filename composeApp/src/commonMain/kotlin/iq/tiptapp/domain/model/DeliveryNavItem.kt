package iq.tiptapp.domain.model

import org.jetbrains.compose.resources.StringResource

sealed class Destination {
    object DetailScreen : Destination()
    object NextScreen : Destination()
}

data class DeliveryNavItem(
    val label: StringResource,
    val destination: Destination,
    var showCheckbox: Boolean = false
)