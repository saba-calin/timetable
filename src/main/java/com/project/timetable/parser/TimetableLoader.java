package com.project.timetable.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TimetableLoader implements CommandLineRunner {

    private final ParserService parserService;

    @Value("${timetable.url}")
    private String url;

    @Override
    public void run(String... args) throws Exception {
        List<String> timetablePages = this.parserService.scrapeMainPage(this.url + "index.html");
        timetablePages = timetablePages.stream().map(link -> this.url + link).toList();
        this.parserService.scrapeTimetablePages(timetablePages);
    }
}
