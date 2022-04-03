package com.itis.android2.domain.converters

class WindDirectionsConverter {

    fun convertWindToString(data: Int): String {
        return when (data.toDouble()) {
            in 22.5..67.5 -> WindDirection.NE.direction
            in 67.5..112.5 -> WindDirection.E.direction
            in 112.5..157.5 -> WindDirection.SE.direction
            in 157.5..202.5 -> WindDirection.S.direction
            in 202.5..247.5 -> WindDirection.SW.direction
            in 247.5..292.5 -> WindDirection.W.direction
            in 292.5..337.5 -> WindDirection.NW.direction
            else -> WindDirection.N.direction
        }
    }
}

enum class WindDirection(val direction: String) {
    N("северный"),
    NW("северо-западный"),
    W("западный"),
    SW("юго-западный"),
    S("южный"),
    SE("юго-восточный"),
    E("восточный"),
    NE("северо-восточный"),
}
