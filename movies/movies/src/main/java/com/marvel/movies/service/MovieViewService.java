package com.marvel.movies.service;

import com.marvel.movies.dto.MovieViewDto;
import com.marvel.movies.model.Movie;
import com.marvel.movies.model.WatchedMovie;
import com.marvel.movies.repository.WatchedMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieViewService {

    private final WatchedMovieRepository watchedMovieRepository;

    public List<MovieViewDto> toView(List<Movie> movies) {
        return movies.stream().map(m -> {
            WatchedMovie wm = watchedMovieRepository.findByMovieId(m.getId()).orElse(null);

            return MovieViewDto.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .releaseDate(m.getReleaseDate())
                    .chronologyIndex(m.getChronologyIndex())
                    .adminOrderIndex(m.getAdminOrderIndex())
                    .type(m.getType())
                    .universe(m.getUniverse())
                    .watched(wm != null && wm.isWatched())
                    .rating(wm != null ? wm.getRating() : null)
                    .build();
        }).toList();
    }
}
