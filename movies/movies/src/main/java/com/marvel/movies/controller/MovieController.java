package com.marvel.movies.controller;

import com.marvel.movies.dto.MovieViewDto;
import com.marvel.movies.model.Movie;
import com.marvel.movies.model.WatchedMovie;
import com.marvel.movies.repository.MovieRepository;
import com.marvel.movies.repository.WatchedMovieRepository;
import com.marvel.movies.service.CountdownService;
import com.marvel.movies.service.MovieViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieRepository movieRepository;
    private final WatchedMovieRepository watchedMovieRepository;
    private final CountdownService countdownService;
    private final MovieViewService movieViewService;

    @GetMapping("/mapping")
    public List<Movie>  getAllMovies(){
        return movieRepository.findAll();
    }

    @GetMapping("/watch-orders")
    public List<MovieViewDto> getWatchOrder(@RequestParam String mode) {

        List<Movie> base = switch (mode.toUpperCase()) {
            case "RELEASE" -> movieRepository.findAllByOrderByReleaseDateAsc();
            case "CHRONOLOGICAL" -> movieRepository.findAllByOrderByChronologyIndexAsc();
            case "ADMIN" -> movieRepository.findAllByAdminOrderIndexIsNotNullOrderByAdminOrderIndexAsc();
            default -> movieRepository.findAll();
        };

        return movieViewService.toView(base);
    }
//    @PostMapping("/watched/{moviedId}")
//    public WatchedMovie markWatched(@PathVariable Long moviedId){
//        Movie movie= movieRepository.findById(moviedId).orElseThrow();
//        WatchedMovie wm = WatchedMovie.builder().movie(movie).watched(true).rating(null).build();
//        return watchedMovieRepository.save(wm);
//    }
//

}
