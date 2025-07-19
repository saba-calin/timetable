package com.project.timetable.parser;

import com.project.timetable.course.CourseEntity;
import com.project.timetable.course.CourseService;
import com.project.timetable.tracker.TrackerEntity;
import com.project.timetable.tracker.TrackerService;
import com.project.timetable.util.ChecksumUtil;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ParserService {

    private final CourseService courseService;
    private final TrackerService trackerService;

    public List<String> scrapeMainPage(String url) throws Exception {
        List<String> timetableLinks = new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        Elements rows = document.select("a");
        for (Element row : rows) {
            timetableLinks.add(row.attr("href"));
        }

        return timetableLinks;
    }

    public void scrapeTimetablePages(List<String> urls) throws Exception {
        List<CompletableFuture<Void>> futures = urls.stream()
                .map(url -> CompletableFuture.runAsync(() -> {
                    try {
                        scrapeTimetablePage(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void scrapeTimetablePage(String url) throws Exception {
        Document document = Jsoup.connect(url).get();

        Elements groupHeaders = document.select("h1");
        Elements groupTimetables = document.select("tbody");

        List<String> groups = groupHeaders.stream().skip(1).map(e -> e.text().substring(6)).toList();  // skip the general header

        for (int i = 0; i < groupTimetables.size(); i++) {
            String checksum = ChecksumUtil.sha256(groupTimetables.get(i).text());
            if (!this.trackerService.hasTimetableChanged(checksum, groups.get(i))) {
                continue;
            }

            TrackerEntity trackerEntity = TrackerEntity.builder()
                    .groupName(groups.get(i))
                    .checksum(checksum)
                    .build();
            this.trackerService.addTracker(trackerEntity);


            Elements trs = new Elements(groupTimetables.get(i).select("tr").stream().skip(1).toList());  // skip the table header
            for (Element tr : trs) {
                Elements tds = tr.select("td");

                String day = tds.get(0).text();
                String hour = tds.get(1).text();
                String frequency = tds.get(2).text();
                String room = tds.get(3).text();
                String formation = tds.get(4).text();
                formation = formation.contains(groups.get(i)) ? formation : groups.get(i);
                String type = tds.get(5).text();
                String course = tds.get(6).text();
                String professor = tds.get(7).text();

                CourseEntity courseEntity = CourseEntity.builder()
                        .day(day)
                        .hour(hour)
                        .frequency(frequency)
                        .room(room)
                        .formation(formation)
                        .type(type)
                        .course(course)
                        .professor(professor)
                        .build();
                this.courseService.addCourse(courseEntity);
            }
        }
    }
}
