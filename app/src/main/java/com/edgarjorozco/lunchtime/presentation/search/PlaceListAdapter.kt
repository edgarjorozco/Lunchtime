package com.edgarjorozco.lunchtime.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edgarjorozco.lunchtime.databinding.ListItemPlaceBinding
import com.edgarjorozco.lunchtime.domain.Place

class PlaceListAdapter(private val onFavoriteChange: (place: Place) -> Unit):
    ListAdapter<Place, PlaceListAdapter.PlaceViewHolder>(Companion) {
    class PlaceViewHolder(val binding: ListItemPlaceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPlaceBinding.inflate(inflater,parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.binding.shouldLoadPhoto = true
        holder.binding.place = place
        holder.binding.favoriteButton.setOnClickListener {
            place.isFavorite = !place.isFavorite
            holder.binding.invalidateAll()
            onFavoriteChange.invoke(place)
        }
    }

    companion object: DiffUtil.ItemCallback<Place>(){
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.placeId == newItem.placeId
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }
}