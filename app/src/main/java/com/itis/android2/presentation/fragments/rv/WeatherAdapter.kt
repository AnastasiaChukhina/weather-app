package com.itis.android2.presentation.fragments.rv

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.android2.domain.models.WeatherSimple

class WeatherAdapter(
    private val list: MutableList<WeatherSimple>,
    private val action: (Int) -> (Unit)
) : RecyclerView.Adapter<WeatherHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherHolder = WeatherHolder.create(parent, action)

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
