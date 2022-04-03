package com.itis.android2.presentation.rv

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.itis.android2.databinding.ItemWeatherBinding
import com.itis.android2.domain.models.WeatherSimple
import com.itis.android2.domain.converters.WeatherDataConverter
import kotlin.math.roundToInt

private const val WARM = 20
private const val NEUTRAL = 10
private const val ZERO = 0
private const val COOL = -10
private const val COLD = -20

class WeatherHolder(
    private val binding: ItemWeatherBinding,
    private val dataConverter: WeatherDataConverter,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WeatherSimple) {
        with(binding) {
            tvCityName.text = item.name
            tvCityTemp.text = dataConverter.convertTempToString(item.temp)
            ivImage.load(dataConverter.setImageIconUrl(item.icon))
            itemView.setOnClickListener {
                action(item.id)
            }
        }
        setBackgroundColor(itemView, item)
    }

    private fun setBackgroundColor(itemView: View, item: WeatherSimple) {
        val result = when (item.temp.roundToInt()) {
            in WARM..Int.MAX_VALUE -> WeatherItemColor.RED
            in NEUTRAL..WARM -> WeatherItemColor.ORANGE
            in ZERO..NEUTRAL -> WeatherItemColor.GREEN
            in COOL..ZERO -> WeatherItemColor.LIGHT_BLUE
            in COLD..COOL -> WeatherItemColor.BLUE
            else -> WeatherItemColor.VIOLET
        }

        setColor(itemView, result)
    }

    private fun setColor(itemView: View, color: WeatherItemColor) {
        itemView.setBackgroundColor(
            Color.parseColor(color.colorHex)
        )
    }

    companion object {
        fun create(
            parent: ViewGroup,
            dataConverter: WeatherDataConverter,
            action: (Int) -> Unit
        ) = WeatherHolder(
            ItemWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            dataConverter,
            action
        )
    }
}
