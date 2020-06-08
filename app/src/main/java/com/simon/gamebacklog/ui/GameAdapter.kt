package com.simon.gamebacklog.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simon.gamebacklog.R
import com.simon.gamebacklog.model.Game
import kotlinx.android.synthetic.main.item_game.view.*
import java.text.SimpleDateFormat

class GameAdapter(private val games: List<Game>) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        )
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game) {
            val releaseDate = SimpleDateFormat("dd MMMM yyyy").format(game.release)

            itemView.tvGameTitle.text = game.title
            itemView.tvGamePlatform.text = game.platform
            itemView.tvGameDate.text = releaseDate
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])
    }
}