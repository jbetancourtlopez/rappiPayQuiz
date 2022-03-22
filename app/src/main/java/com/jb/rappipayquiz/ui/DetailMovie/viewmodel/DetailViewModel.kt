package com.jb.rappipayquiz.ui.DetailMovie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jb.rappipayquiz.data.MovieRepository
import com.jb.rappipayquiz.domain.interator.GetDetailMovieUseCase
import com.jb.rappipayquiz.domain.model.MovieDetail
import kotlinx.coroutines.launch

class DetailViewModel() : ViewModel() {

    val movieDetail = MutableLiveData<MovieDetail>()
    val isLoading = MutableLiveData<Boolean>()
    var repo: MovieRepository? = null

    fun getDetailMovie(movie_id: Int) {
        val getDetailMovieUseCase = GetDetailMovieUseCase(repo!!)

        viewModelScope.launch {

            isLoading.postValue(true)
            var result = getDetailMovieUseCase?.invoke(movie_id)

            if (result != null) {
                movieDetail.postValue(result!!)
                isLoading.postValue(false)
            }
        }
    }

    fun setRepository(_repo: MovieRepository){
        repo = _repo
    }
}