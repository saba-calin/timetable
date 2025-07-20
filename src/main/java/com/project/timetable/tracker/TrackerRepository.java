package com.project.timetable.tracker;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackerRepository extends JpaRepository<TrackerEntity, Long> {
    Optional<TrackerEntity> findByGroupName(String groupName);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM tracker WHERE group_name LIKE CONCAT('%', :groupName, '%')
    """, nativeQuery = true)
    void deleteByGroupName(@Param("groupName") String groupName);
}
