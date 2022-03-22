package com.jb.rappipayquiz.domain.interator

import com.jb.rappipayquiz.data.MovieRepository
import com.jb.rappipayquiz.domain.model.Movie
import com.jb.rappipayquiz.domain.model.MoviesResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetPopularMoviesUseCaseTest{

    @RelaxedMockK
    private lateinit var mMovieRepository: MovieRepository

    lateinit var mGetPopularMoviesUseCase: GetPopularMoviesUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        mGetPopularMoviesUseCase = GetPopularMoviesUseCase(mMovieRepository)
    }

    @Test
    // Cuando la api regrese vacio, se debe de tomar la informacion de la bd local
    fun `when api return empty then get from database`() = runBlocking {
        // Given
        coEvery { mMovieRepository.getPopularMoviesRemote(1) } returns MoviesResponse(0, 0, 0, emptyList())

        // When
        mGetPopularMoviesUseCase(1)

        // Then
        coVerify(exactly = 1) {
            mMovieRepository.getPopularMoviesLocal(1, 0)
        }
    }

    @Test
    // Cuando la api regrese datos, se debe de tomar la informacion de la api e insertar los datos en la bd local
    fun `when api return data then get from api`() = runBlocking {
        // Given
        var lst_movie:List<Movie> = listOf(Movie(7.1,1,true,"image.jpg",100,true,"n_a","es","RappiPay","RappiPay",0.0,"lorem","22 Marzo 2022"))

        var mMovieResponse = MoviesResponse(0, 0, 0, lst_movie)
        coEvery { mMovieRepository.getPopularMoviesRemote(1) } returns mMovieResponse

        // When
        val response = mGetPopularMoviesUseCase(1)

        // Then
        coVerify(exactly = 1) {
            mMovieRepository.insertAllMovies(response.results, 0)
        }

        coVerify(exactly = 0) {
            mMovieRepository.getPopularMoviesLocal(1, 0)
        }
    }


}