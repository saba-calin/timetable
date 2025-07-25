package com.project.timetable.course;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM session WHERE formation LIKE CONCAT('%', :groupName, '%')
    """, nativeQuery = true)
    void deleteByGroupName(@Param("groupName") String groupName);

    @Query(value = """
        SELECT *
        FROM session
        WHERE (formation = :semiGroup OR formation = :group) AND day = :day
        ORDER BY CAST(SPLIT_PART(hour, '-', 1) AS INTEGER) ASC
    """, nativeQuery = true)
    List<CourseEntity> getCoursesByDayAndGroupAndSemigroup(@Param("day") String day,
                                               @Param("group") String group,
                                               @Param("semiGroup") String semiGroup);

    @Query(value = """
        SELECT *
        FROM session
        WHERE formation LIKE CONCAT('%', :group, '%') AND day = :day
        ORDER BY CAST(SPLIT_PART(hour, '-', 1) AS INTEGER) ASC
    """, nativeQuery = true)
    List<CourseEntity> getCoursesByDayAndGroup(@Param("day") String day,
                                               @Param("group") String group);
}
