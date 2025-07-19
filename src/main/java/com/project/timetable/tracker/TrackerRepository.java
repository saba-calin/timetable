package com.project.timetable.tracker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackerRepository extends JpaRepository<TrackerEntity, Long> {
    Optional<TrackerEntity> findByGroupName(String groupName);
}
