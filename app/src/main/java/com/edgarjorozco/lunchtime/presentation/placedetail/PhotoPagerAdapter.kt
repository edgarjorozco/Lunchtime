package com.edgarjorozco.lunchtime.presentation.placedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edgarjorozco.lunchtime.databinding.ListItemPhotoBinding
import com.edgarjorozco.lunchtime.models.PlacePhoto

class PhotoPagerAdapter(private val data: List<PlacePhoto>): RecyclerView.Adapter<PhotoPagerAdapter.PhotoViewHolder>() {
    class PhotoViewHolder(val binding: ListItemPhotoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPhotoBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.binding.placePhoto = data[position]
    }

    override fun getItemCount(): Int = data.size
}