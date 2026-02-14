package com.marvel.movies.repository;

import com.marvel.movies.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findAllByOrderByReleaseDateAsc();

    List<Movie> findAllByOrderByChronologyIndexAsc();

    List<Movie> findAllByAdminOrderIndexIsNotNullOrderByAdminOrderIndexAsc();

}