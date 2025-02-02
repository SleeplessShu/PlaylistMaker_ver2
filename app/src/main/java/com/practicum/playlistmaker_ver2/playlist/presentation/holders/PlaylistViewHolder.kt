package com.practicum.playlistmaker_ver2.playlist.presentation.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.playlist.data.database.entity.PlaylistEntity

class PlaylistViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    private val playListName: TextView = itemView.findViewById(R.id.playlistName)
    private val image: ImageView = itemView.findViewById(R.id.playListImage)
    private val playListCount: TextView = itemView.findViewById(R.id.countTrack)
    fun bind(item: PlaylistEntity,onItemClickListener : OnItemClickListener?) {
        val countList: String = item.count.toString().plus(" ").plus(checkCount(item.count))

        playListName.text = item.namePlayList
        playListCount.text = countList


        Glide.with(itemView)
            .load(item.image)
            .placeholder(R.drawable.placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.placeholder_corner_radius))
            )
            .into(image)

        itemView.setOnClickListener{
            onItemClickListener?.onItemClick(item)
        }
    }
    fun interface OnItemClickListener {
        fun onItemClick(item: PlaylistEntity)
    }

    fun checkCount(count:Int): String{
        var word: String
        val countTrack = count % 100 / 10
        if (countTrack == 1){
            word = "Треков"
        }
        when(count % 10){
            1 -> word = "Трек"
            2,3,4 ->  word ="Трека"
            else -> word ="Треков"
        }
        return word
    }
}