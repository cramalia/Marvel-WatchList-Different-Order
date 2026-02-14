package com.marvel.movies.controller;

import com.marvel.movies.service.CountdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class CountdownController {
    private final CountdownService countdownService;

    @GetMapping("/countdown")
    public Map<String,Long> countdown(){
        return Map.of("daysUntilDoomsday",countdownService.daysUntilDoomsday());
    }
}
