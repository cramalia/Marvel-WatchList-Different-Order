package com.marvel.movies.service;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CountdownService {
    public long daysUntilDoomsday(){
        LocalDate doomsday=LocalDate.of(2026,12,18);
        return ChronoUnit.DAYS.between(LocalDate.now(),doomsday);
    }
}
