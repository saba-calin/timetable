package com.project.timetable.course;

import com.project.timetable.dto.CourseDto;
import com.project.timetable.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<List<CourseDto>> getCourses(@RequestBody RequestDto requestDto) {
        return ResponseEntity.ok(this.courseService.getCoursesByGroupAndDay(requestDto));
    }
}
