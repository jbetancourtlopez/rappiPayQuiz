package com.jb.rappipayquiz.ui.home

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.jb.rappipayquiz.R
import com.jb.rappipayquiz.core.BaseActivity
import com.jb.rappipayquiz.data.MovieRepository
import com.jb.rappipayquiz.databinding.ActivityMainBinding
import com.jb.rappipayquiz.domain.model.Movie
import com.jb.rappipayquiz.ui.adapter.MovieAdapter
import com.jb.rappipayquiz.ui.home.viewmodel.MoviesViewModel
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.getSystemService
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.jb.rappipayquiz.ui.DetailMovie.DetailMovieActivity
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class MainActivity : BaseActivity(), MovieAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private val moviesViewModel: MoviesViewModel by viewModels()

    var tab_current = tabs_category.POPULAR.ordinal

    var lst_movies: List<Movie> = emptyList()

    val adapter = MovieAdapter(this, lst_movies)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var repo = MovieRepository(this)
        moviesViewModel.setRepository(repo)

        selected_tab(tab_current)

        setup_toolbar()

        setup_recyclerview()

        binding.contentTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab select
                if (tab != null) {
                    tab_current = tab.position
                    selected_tab(tab.position)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })
    }

    private fun setup_toolbar() {
        setSupportActionBar(binding.toolbarContent.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.tab_popular)
        }
    }

    fun selected_tab (tab : Int){
        moviesViewModel.query = ""
        load_movies(tab)

        when (tab) {
            tabs_category.POPULAR.ordinal -> {
                supportActionBar?.title = getString(R.string.tab_popular)
            }
            tabs_category.TOP.ordinal -> {
                supportActionBar?.title = getString(R.string.tab_top_rate)
            }

        }
    }

    fun load_movies(tab : Int){
        tab_current = tab
        moviesViewModel.tab_selected = tab

        moviesViewModel.refreshMovies()

        moviesViewModel.movieResponse.observe(this, Observer {
            render_movies(it)
        })

        moviesViewModel.isLoading.observe(this, Observer {
             binding.loading.isVisible = it
        })
    }

    fun setup_recyclerview(){
        adapter.setOnMovieClickListener(this)

        var layout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.layoutManager = layout

        // Decorador
        var itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(getDrawable(R.drawable.divider)!!)

        if (binding.recyclerView.itemDecorationCount == 0) {
            binding.recyclerView.addItemDecoration(itemDecoration)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(scrollListener)
    }

    //Scroll for pagination
    private val scrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {

                    Log.d("JB22", "Scroll Tab:" + tab_current.toString() + " Page:" + moviesViewModel.debug())

                    if(!moviesViewModel.isLastPage()){
                        moviesViewModel.tab_selected = tab_current
                        moviesViewModel.fetchNextMovies()
                    }

                }
            }
        }

     @SuppressLint("ResourceAsColor")
     fun render_movies(lst_movie_: List<Movie>) {

         binding.txtEmpty.visibility = View.GONE
        if (lst_movie_.isEmpty()) {
            OnFailed()
            return
        }

         if (moviesViewModel.isFirstPage()) {
             adapter.clear()
         }else{
             binding.recyclerView.scrollToPosition(adapter.itemCount -1);
         }

         adapter.fillList(lst_movie_)
    }

     fun OnFailed() {
         //binding.recyclerView.adapter = null
         binding.txtEmpty.visibility = View.VISIBLE
    }

    override fun onItemClick(movie: Movie, container: View) {
        if (checkForInternet(this)) {
            val intent = DetailMovieActivity.getIntent(movie.id, movie.poster_path, this)

            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                container,
                container.transitionName
            )
            startActivity(intent, options.toBundle())
        }else{
            Toast.makeText(this, "No cuenta con internet para ir al detalle, solo puede ver offline la lista de peliculas", Toast.LENGTH_LONG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.movie_menu_top, menu)
        setUpSearchViewListener(menu)
        return true
    }

    private fun setUpSearchViewListener(menu: Menu) {
        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setBackgroundColor(Color.WHITE)

        searchView.apply {

            queryHint = context.getString(R.string.query_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {

                        moviesViewModel.query = query
                        load_movies(tabs_category.SEARCH.ordinal)
                    }
                    onActionViewCollapsed()
                    searchItem.collapseActionView()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}

enum class tabs_category {
    POPULAR, TOP, SEARCH
}

