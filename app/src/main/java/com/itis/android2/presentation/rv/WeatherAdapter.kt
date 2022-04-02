package com.itis.android2.presentation.rv

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.itis.android2.domain.converters.WeatherDataConverter
import com.itis.android2.domain.models.WeatherSimple

class WeatherAdapter(
    private val dataConverter: WeatherDataConverter,
    private val action: (Int) -> (Unit)
) : ListAdapter<WeatherSimple, WeatherHolder>(WeatherDiffUtilsCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherHolder = WeatherHolder.create(parent, dataConverter, action)

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<WeatherSimple>?) {
        super.submitList(
            if (list == null) null
            else ArrayList(list)
        )
    }
}

class WeatherDiffUtilsCallback : DiffUtil.ItemCallback<WeatherSimple>() {

    override fun areItemsTheSame(
        oldItem: WeatherSimple,
        newItem: WeatherSimple
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: WeatherSimple,
        newItem: WeatherSimple
    ): Boolean = oldItem == newItem
}
