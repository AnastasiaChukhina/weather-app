package com.itis.android2.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.itis.android2.R
import com.itis.android2.data.api.response.NearCityWeather
import com.itis.android2.databinding.ItemWeatherBinding
import com.itis.android2.helpers.WeatherDataHandler
import kotlin.math.roundToInt

private const val WEATHER_UPPER = 100
private const val WEATHER_WARM = 20
private const val WEATHER_NEUTRAL_PLUS = 5
private const val WEATHER_ZERO = 0
private const val WEATHER_NEUTRAL_MINUS = -5
private const val WEATHER_COLD = -20
private const val WEATHER_LOWER = -100

class WeatherHolder(
    private val binding: ItemWeatherBinding,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NearCityWeather) {
        with(binding) {
            tvCityName.text = item.name
            tvCityTemp.text = WeatherDataHandler.convertTempToString(item.main.temp)
            ivImage.load(
                WeatherDataHandler.setImageIconUrl(item.weather[0].icon)
            )
        }
        itemView.setOnClickListener {
            action(item.id)
        }
        setBackgroundColor(itemView, item)
    }

    private fun setBackgroundColor(itemView: View, item: NearCityWeather) {
        when (item.main.temp.roundToInt()) {
            in WEATHER_WARM..WEATHER_UPPER -> setColor(itemView, R.color.hot)
            in WEATHER_NEUTRAL_PLUS..WEATHER_WARM -> setColor(itemView, R.color.warm)
            in WEATHER_ZERO..WEATHER_NEUTRAL_PLUS -> setColor(itemView, R.color.neutral)
            in WEATHER_NEUTRAL_MINUS..WEATHER_ZERO -> setColor(itemView, R.color.cold)
            in WEATHER_COLD..WEATHER_NEUTRAL_MINUS -> setColor(itemView, R.color.frozen)
            in WEATHER_LOWER..WEATHER_COLD -> setColor(itemView, R.color.extremely_cold)
        }
    }

    private fun setColor(itemView: View, colorId: Int){
        itemView.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                colorId
            )
        )
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (Int) -> Unit
        ) = WeatherHolder(
            ItemWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            action
        )
    }
}
