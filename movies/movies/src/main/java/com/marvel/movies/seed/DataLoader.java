package com.marvel.movies.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marvel.movies.model.Movie;
import com.marvel.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() > 0) return;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ClassPathResource resource = new ClassPathResource("movies.json");

        try (InputStream is = resource.getInputStream()) {
            List<MovieSeedDto> dtos = mapper.readValue(is, new TypeReference<List<MovieSeedDto>>() {
            });
            List<Movie> movies = dtos.stream()
                    .map(dto -> Movie.builder()
                            .title(dto.getTitle())
                            .releaseDate(dto.getReleaseDate())
                            .chronologyIndex(dto.getChronologyIndex())
                            .adminOrderIndex(dto.getAdminOrderIndex())
                            .type(dto.getType())
                            .universe(dto.getUniverse())
                            .build())
                    .toList();
            movieRepository.saveAll(movies);
        }
    }
}
