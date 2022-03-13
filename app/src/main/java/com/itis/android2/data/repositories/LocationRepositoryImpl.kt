package com.itis.android2.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.itis.android2.data.api.response.Coord
import com.itis.android2.domain.repositories.LocationRepository

private const val DEFAULT_LATITUDE = 55.7887
private const val DEFAULT_LONGITUDE = 49.1221

class LocationRepositoryImpl(
    private val context: Context
) : LocationRepository {

    private var coordinates: Coord? = null

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    override fun getCoordinates(): Coord? {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                coordinates = Coord(location.latitude, location.longitude)
            }
        }
        return coordinates
    }

    override fun getDefaultCoordinates(): Coord {
        return Coord(
            DEFAULT_LATITUDE,
            DEFAULT_LONGITUDE
        )
    }
}
