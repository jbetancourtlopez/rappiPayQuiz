package com.jb.rappipayquiz.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jb.rappipayquiz.data.MovieRepository
import com.jb.rappipayquiz.domain.interator.GetPopularMoviesUseCase
import com.jb.rappipayquiz.domain.interator.GetSearchMovieUseCase
import com.jb.rappipayquiz.domain.interator.GetTopRateMoviesUseCase
import com.jb.rappipayquiz.domain.model.Movie
import com.jb.rappipayquiz.domain.model.MoviesResponse
import com.jb.rappipayquiz.ui.home.tabs_category
import kotlinx.coroutines.launch

class MoviesViewModel() : ViewModel() {
    private var currentPage = 1
    private var lastPage = 1

    val movieResponse = MutableLiveData<List<Movie>>()
    val isLoading = MutableLiveData<Boolean>()
    var repo: MovieRepository? = null

    var query: String? = ""
    var tab_selected: Int = 0

    fun fetchMovies() {
        val getPopularMoviesUseCase = GetPopularMoviesUseCase(repo!!)
        val getTopRateMoviesUseCase = GetTopRateMoviesUseCase(repo!!)
        val getSearchMoviesUseCase = GetSearchMovieUseCase(repo!!)

        viewModelScope.launch {

            isLoading.postValue(true)

            var result: MoviesResponse? = null

            if (query != null && query != "") {
                result = getSearchMoviesUseCase?.invoke(currentPage, query!!)
               // query = ""
            } else {
                if(tab_selected == tabs_category.POPULAR.ordinal){
                    result = getPopularMoviesUseCase?.invoke(currentPage)
                }else{
                    result = getTopRateMoviesUseCase?.invoke(currentPage)
                }
            }



            if (result != null) {

                lastPage = result.total_pages

                movieResponse.postValue(result.results)

                isLoading.postValue(false)
            }
        }
    }

    fun setRepository(_repo: MovieRepository){
        repo = _repo
    }

    fun fetchNextMovies() {
        currentPage++
        fetchMovies()
    }

    fun refreshMovies() {
        currentPage = 1
        fetchMovies()
    }

    fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    fun isLastPage(): Boolean {
        return currentPage == lastPage
    }

    fun debug(): String {
        return "currentPage: " + currentPage + " lastPage: " + lastPage
    }
}