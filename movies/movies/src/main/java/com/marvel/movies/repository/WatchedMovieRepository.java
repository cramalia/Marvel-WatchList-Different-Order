package com.marvel.movies.repository;

import com.marvel.movies.model.WatchedMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchedMovieRepository extends JpaRepository<WatchedMovie,Long> {
    Optional<WatchedMovie> findByMovieId(Long movieId);
    List<WatchedMovie> findAllByWatchedTrue();
}
