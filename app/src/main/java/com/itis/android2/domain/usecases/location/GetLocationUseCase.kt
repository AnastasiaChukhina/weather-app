package com.itis.android2.domain.usecases.location

import com.itis.android2.data.api.response.Coord
import com.itis.android2.domain.repositories.LocationRepository

class GetLocationUseCase(
    private val locationRepository: LocationRepository,
) {

    operator fun invoke(): Coord {
        return locationRepository.getCoordinates() ?: locationRepository.getDefaultCoordinates()
    }
}
