package com.project.timetable.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public void addCourse(CourseEntity courseEntity) {
        this.courseRepository.save(courseEntity);
    }
}
