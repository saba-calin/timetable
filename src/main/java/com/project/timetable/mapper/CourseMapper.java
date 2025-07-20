package com.project.timetable.mapper;

import com.project.timetable.course.CourseEntity;
import com.project.timetable.dto.CourseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto entityToDto(CourseEntity courseEntity);

    List<CourseDto> entityToDto(List<CourseEntity> courseEntities);
}
