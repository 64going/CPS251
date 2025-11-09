package com.example.test

// Core Android imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.api.RetrofitInstance.omdbApiService

/*
1. Explain the role of Retrofit in this project. How does it simplify interacting with the OMDB API compared to making raw HTTP requests?
2. What is the purpose of the data classes (e.g., OmdbMovieDetailResponse, MovieSearchItem) in this project? How do they relate to the JSON responses from the OMDB API?
3. Why is a MovieRepository included in this project's architecture? What are the benefits of having a separate repository layer for data fetching, especially in a larger application?
4. What are the primary responsibilities of the MovieViewModel? How does it help to separate concerns between the UI and the data layer, and what advantages does this provide?
5. Describe how navigation between the MovieSearchScreen and MovieDetailsScreen is handled in this project. How are arguments (like imdbId) passed between composables during navigation?
*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                   LoginApp()
                }
            }
        }
    }
    @Composable
    fun LoginApp() {
        val viewModel: MovieViewModel by viewModels {
            MovieViewModel.provideFactory(MovieRepository(omdbApiService))
        }
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "movieSearch"
        ) {
            composable("movieSearch"){
                MovieSearchScreen(navController, movieViewModel = viewModel)
            }
            composable(route = "movie_details/{imdbId}"){ backStackEntry ->
                val imdbId = backStackEntry.arguments?.getString("imdbId")
                MovieDetailsScreen(navController, movieViewModel = viewModel,imdbId)
            }
        }
    }
}
