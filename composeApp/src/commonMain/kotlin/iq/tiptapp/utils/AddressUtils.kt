package iq.tiptapp.utils

import dev.jordond.compass.Place
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.GeocoderResult
import dev.jordond.compass.geocoder.mobile.MobilePlatformGeocoder
import io.github.aakira.napier.Napier

suspend fun getAddress(latitude: Double, longitude: Double): String? {
    return lookupCoordinates(latitude, longitude)?.let { ads ->
        buildString {
            listOfNotNull(
                ads.street,
                ads.country,
                ads.postalCode
            ).joinTo(this, separator = ", ")
        }
    }
}

private suspend fun lookupCoordinates(latitude: Double, longitude: Double): Place? {
    val geocoder = Geocoder(MobilePlatformGeocoder())
    val result: GeocoderResult<Place> = geocoder.reverse(latitude, longitude)
    when (result) {
        is GeocoderResult.Error -> Napier.w("Error: $result")
        is GeocoderResult.Success -> Napier.d("Success: ${result.getFirstOrNull()}")
    }
    return result.getFirstOrNull()
}
