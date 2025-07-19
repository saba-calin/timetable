package com.project.timetable.course;

import com.project.timetable.dto.CourseDto;
import com.project.timetable.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

//    @GetMapping
//    public ResponseEntity<List<CourseDto>> getCourses(@RequestBody RequestDto requestDto) {
//
//    }
}
