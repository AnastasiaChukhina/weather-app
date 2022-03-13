package com.itis.android2.domain.repositories

import com.itis.android2.data.api.response.Coord

interface LocationRepository {
    fun getCoordinates(): Coord?
    fun getDefaultCoordinates(): Coord
}
