package com.marvel.movies.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate releaseDate;
    private Integer chronologyIndex;
    private Integer adminOrderIndex;

    @Enumerated(EnumType.STRING)
    private MovieType type;

    @Enumerated(EnumType.STRING)
    private Universe universe;


}
