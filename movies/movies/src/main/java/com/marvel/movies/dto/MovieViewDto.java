package com.marvel.movies.dto;

import com.marvel.movies.model.MovieType;
import com.marvel.movies.model.Universe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieViewDto {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private Integer chronologyIndex;
    private Integer adminOrderIndex;
    private MovieType type;
    private Universe universe;

    private boolean watched;
    private Float rating;
}
