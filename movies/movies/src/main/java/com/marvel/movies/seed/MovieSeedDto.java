package com.marvel.movies.seed;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.marvel.movies.model.MovieType;
import com.marvel.movies.model.Universe;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MovieSeedDto {
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    private Integer chronologyIndex;
    private Integer adminOrderIndex;

    private MovieType type;
    private Universe universe;

}
