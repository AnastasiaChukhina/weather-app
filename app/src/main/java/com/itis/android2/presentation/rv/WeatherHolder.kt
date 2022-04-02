package com.itis.android2.presentation.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.itis.android2.R
import com.itis.android2.databinding.ItemWeatherBinding
import com.itis.android2.domain.models.WeatherSimple
import com.itis.android2.domain.helpers.WeatherDataHandler
import kotlin.math.roundToInt

private const val WARM = 20
private const val NEUTRAL = 10
private const val ZERO = 0
private const val COOL = -10
private const val COLD = -20

class WeatherHolder(
    private val binding: ItemWeatherBinding,
    private val dataHandler: WeatherDataHandler,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WeatherSimple) {
        with(binding) {
            tvCityName.text = item.name
            tvCityTemp.text = dataHandler.convertTempToString(item.temp)
            ivImage.load(dataHandler.setImageIconUrl(item.icon))
            itemView.setOnClickListener {
                action(item.id)
            }
        }
        setBackgroundColor(itemView, item)
    }

    private fun setBackgroundColor(itemView: View, item: WeatherSimple) {
        when (item.temp.roundToInt()) {
            in WARM..Int.MAX_VALUE -> setColor(itemView, R.color.hot)
            in NEUTRAL..WARM -> setColor(itemView, R.color.warm)
            in ZERO..NEUTRAL -> setColor(itemView, R.color.neutral)
            in COOL..ZERO -> setColor(itemView, R.color.cold)
            in COLD..COOL -> setColor(itemView, R.color.frozen)
            else -> setColor(itemView, R.color.extremely_cold)
        }
    }

    private fun setColor(itemView: View, colorId: Int) {
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
            dataHandler: WeatherDataHandler,
            action: (Int) -> Unit
        ) = WeatherHolder(
            ItemWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            dataHandler,
            action
        )
    }
}
