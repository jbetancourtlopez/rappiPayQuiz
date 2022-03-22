package com.jb.rappipayquiz.ui.DetailMovie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.jb.rappipayquiz.R

import com.jb.rappipayquiz.core.BaseActivity
import com.jb.rappipayquiz.data.MovieRepository
import com.jb.rappipayquiz.data.remote.api.ApiClient
import com.jb.rappipayquiz.databinding.ActivityDetailMovieBinding
import com.jb.rappipayquiz.domain.model.MovieDetail
import com.jb.rappipayquiz.ui.DetailMovie.viewmodel.DetailViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso

private const val KEY_INTENT_EXTRA_MOVIE_ID = "movie_id"
private const val KEY_INTENT_EXTRA_MOVIE_POSTER = "poster"

class DetailMovieActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailMovieBinding

    private val viewModel: DetailViewModel by viewModels()

    var movie_id = 0
    var poster = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        get_bundle()

        setup_transition()

        load_detail()
    }

    private fun get_bundle() {
        val b = intent.extras
        movie_id = b!!.getInt(KEY_INTENT_EXTRA_MOVIE_ID)
        poster = b!!.getString(KEY_INTENT_EXTRA_MOVIE_POSTER, "")
    }

    private fun setup_transition() {
        postponeEnterTransition()
        binding.ivPoster.transitionName = movie_id.toString()
        Picasso.get().load(ApiClient.POSTER_BASE_URL + poster).placeholder(this.getResources().getDrawable(
            R.drawable.ic_neflix)).into(binding.ivPoster);

        startPostponedEnterTransition()
    }

    private fun load_detail() {

        var repo = MovieRepository(this)
        viewModel.setRepository(repo)

        viewModel.getDetailMovie(movie_id)

        viewModel.movieDetail.observe(this, Observer {
            render_detail(it)
        })

        viewModel.isLoading.observe(this, Observer {
            binding.loading.isVisible = it
        })

    }

    fun render_detail(detail : MovieDetail){

        binding.tvTitle.text = detail.title
        binding.tvReleased.text = detail.release_date
        binding.tvVote.text = detail.vote_average.toString()
        binding.tvLang.text = detail.original_language
        binding.tvOverview.text = detail.overview
        binding.labelVote.text = detail.vote_count.toString() + " Votos"

        getLifecycle().addObserver(binding.youtubePlayerView)

        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = "S0Q4gqBUs7c" //change according to your need
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })

        setup_toolbar(detail.title)

    }

    companion object {
        private const val TAG = "DetailMovieActivity"
        fun getIntent(movie_id: Int, poster: String, context: Context): Intent {
            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.putExtra(KEY_INTENT_EXTRA_MOVIE_ID, movie_id)
            intent.putExtra(KEY_INTENT_EXTRA_MOVIE_POSTER, poster)

            return intent
        }
    }

    private fun setup_toolbar(title_: String) {

        setSupportActionBar(binding.toolbarContent.toolbar)

        supportActionBar?.apply {
            title = title_
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}