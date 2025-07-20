package com.project.timetable.course;

import com.project.timetable.dto.CourseDto;
import com.project.timetable.dto.RequestDto;
import com.project.timetable.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public void addCourse(CourseEntity courseEntity) {
        this.courseRepository.save(courseEntity);
    }

    public void deleteByGroupName(String groupName) {
        this.courseRepository.deleteByGroupName(groupName);
    }

    public List<CourseDto> getCoursesByGroupAndDay(RequestDto requestDto) {
        String day = requestDto.getDay();
        String semiGroup = requestDto.getSemiGroup();
        String group = semiGroup.contains("/") ? semiGroup.split("/")[0] : semiGroup;

        List<CourseEntity> courses = this.courseRepository.getCoursesByDayAndGroup(day, group, semiGroup);
        return this.courseMapper.entityToDto(courses);
    }
}
