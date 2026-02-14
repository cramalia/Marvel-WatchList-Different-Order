package com.marvel.movies.controller;

import com.marvel.movies.model.Movie;
import com.marvel.movies.model.WatchedMovie;
import com.marvel.movies.repository.MovieRepository;
import com.marvel.movies.repository.WatchedMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class WatchedController {
    private final MovieRepository movieRepository;
    private final WatchedMovieRepository watchedMovieRepository;

    @GetMapping("/watched")
    public List<WatchedMovie> watchedList(){
        return watchedMovieRepository.findAllByWatchedTrue();
    }

    @PostMapping("/watched/{movieId}")
    public WatchedMovie markWatched(@PathVariable Long movieId){
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        WatchedMovie wm = watchedMovieRepository.findByMovieId(movieId).orElse(WatchedMovie.builder().movie(movie).build());
        wm.setWatched(true);
        return watchedMovieRepository.save(wm);
    }

    @DeleteMapping("/watched/{movieId}")
    public WatchedMovie unwatch(@PathVariable Long movieId){
        WatchedMovie wm = watchedMovieRepository.findByMovieId(movieId).orElseThrow();
        wm.setWatched(false);
        return watchedMovieRepository.save(wm);
    }

    @PutMapping("/watched/{movieId}/rating")
    public WatchedMovie setRating(@PathVariable Long movieId, @RequestParam Float value) {
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        WatchedMovie wm = watchedMovieRepository.findByMovieId(movieId)
                .orElse(WatchedMovie.builder().movie(movie).build());

        wm.setRating(value);
        wm.setWatched(true);
        return watchedMovieRepository.save(wm);
    }


    @GetMapping("/progress")
    public Map<String, Object> progress() {
        long total = movieRepository.count();
        long watched = watchedMovieRepository.findAllByWatchedTrue().size();

        double percent = total == 0 ? 0.0 : (watched * 100.0) / total;

        return Map.of(
                "watched", watched,
                "total", total,
                "percent", Math.round(percent * 100.0) / 100.0
        );
    }
}
