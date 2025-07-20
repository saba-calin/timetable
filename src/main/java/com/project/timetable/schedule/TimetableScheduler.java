package com.project.timetable.schedule;

import com.project.timetable.parser.ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TimetableScheduler {

    private final ParserService parserService;

    @Value("${timetable.url}")
    private String url;

    @Scheduled(cron = "0 0 * * * *")
    public void fetchTimetableHourly() throws Exception {
        List<String> timetablePages = this.parserService.scrapeMainPage(this.url + "index.html");
        timetablePages = timetablePages.stream().map(link -> this.url + link).toList();
        this.parserService.scrapeTimetablePages(timetablePages);
    }
}
