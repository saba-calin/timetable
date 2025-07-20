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

    @Query("""
        SELECT s
        FROM session s
        WHERE (s.formation = :group OR s.formation = :semiGroup) AND s.day = :day
        ORDER BY s.hour ASC
    """)
    List<CourseEntity> getCoursesByDayAndGroup(@Param("day") String day,
                                               @Param("group") String group,
                                               @Param("semiGroup") String semiGroup);
}
