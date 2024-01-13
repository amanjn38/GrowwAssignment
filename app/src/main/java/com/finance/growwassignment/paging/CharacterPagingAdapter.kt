package com.finance.growwassignment.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.growwassignment.R
import com.finance.growwassignment.models.CharacterResult
import com.finance.growwassignment.utilities.OnItemClickListener

class CharacterPagingAdapter(private val itemClickListener: OnItemClickListener<CharacterResult>) :
    PagingDataAdapter<CharacterResult, CharacterPagingAdapter.CharacterViewHolder>(COMPARATOR) {

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameTextView)
        val gender: TextView = itemView.findViewById(R.id.genderTextView)
        val height: TextView = itemView.findViewById(R.id.heightTextView)
        val mass: TextView = itemView.findViewById(R.id.massTextView)
        val eyeColor: TextView = itemView.findViewById(R.id.eyeColorTextView)

        val root: ConstraintLayout = itemView.findViewById(R.id.parent)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.name.text = item.name
            holder.eyeColor.text = "Eye Color : " + item.eye_color
            holder.gender.text = "Gender : " + item.gender
            holder.mass.text = "Mass : " + item.mass
            holder.height.text = "Height : " + item.height
        }
        holder.root.setOnClickListener {
            if (item != null) {
                itemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.character_item, parent, false)
        return CharacterViewHolder(view)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<CharacterResult>() {
            override fun areItemsTheSame(oldItem: CharacterResult, newItem: CharacterResult): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: CharacterResult, newItem: CharacterResult): Boolean {
                return oldItem == newItem
            }
        }
    }
}
