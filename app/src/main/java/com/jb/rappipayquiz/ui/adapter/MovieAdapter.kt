package com.jb.rappipayquiz.ui.adapter

import android.accounts.AccountManager.get
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.jb.rappipayquiz.R
import com.jb.rappipayquiz.data.remote.api.ApiClient
import com.jb.rappipayquiz.databinding.ItemMovieBinding
import com.jb.rappipayquiz.domain.model.Movie
import com.squareup.picasso.Picasso


class MovieAdapter(val _context: Context?, var items: List<Movie> = ArrayList()) : RecyclerView.Adapter<ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movie: Movie, container: View) // pass ImageView to make shared transition
    }

    var context = _context

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]

        holder.tv_title.text = movie.title
        Picasso.get().load(ApiClient.POSTER_BASE_URL + movie.poster_path).placeholder(context!!.getResources().getDrawable(R.drawable.ic_neflix)).into(holder.iv_poster);
        holder.tv_released.text = movie.release_date
        holder.tv_overview.text = movie.overview

        holder.iv_poster.transitionName = movie.id.toString()

        holder.cl_container.setOnClickListener {
            onItemClickListener?.onItemClick(movie, holder.iv_poster)


        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnMovieClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun fillList(items: List<Movie>) {
        this.items += items
        notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
    }
}

class ViewHolder (binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    val tv_title: TextView = binding.tvTitle
    val tv_released: TextView = binding.tvReleased
    val tv_overview: TextView = binding.tvOverview
    val iv_poster: ImageView = binding.ivPoster
    val cl_container : ConstraintLayout = binding.clContainer

}