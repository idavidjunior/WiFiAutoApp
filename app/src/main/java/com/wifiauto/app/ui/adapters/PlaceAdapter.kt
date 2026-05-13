package com.wifiauto.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wifiauto.app.data.PlaceEntity
import com.wifiauto.app.databinding.ItemPlaceBinding

class PlaceAdapter(
    private val onEditClick: (PlaceEntity) -> Unit,
    private val onDeleteClick: (PlaceEntity) -> Unit
) : ListAdapter<PlaceEntity, PlaceAdapter.PlaceViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaceViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceEntity) {
            binding.tvNome.text = place.nome
            binding.tvRede.text = "Rede: ${place.ssid ?: "Qualquer"}"
            binding.tvRaio.text = "Raio: ${place.raio}m"
            binding.chipAprendiz.visibility = if (place.aprendiz) View.VISIBLE else View.GONE
            binding.btnEditar.setOnClickListener { onEditClick(place) }
            binding.btnExcluir.setOnClickListener { onDeleteClick(place) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PlaceEntity>() {
        override fun areItemsTheSame(oldItem: PlaceEntity, newItem: PlaceEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PlaceEntity, newItem: PlaceEntity): Boolean =
            oldItem == newItem
    }
}
