package com.project.timetable.tracker;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackerService {

    private final TrackerRepository trackerRepository;

    public void addTracker(TrackerEntity trackerEntity) {
        this.trackerRepository.save(trackerEntity);
    }

    public void deleteByGroupName(String groupName) {
        this.trackerRepository.deleteByGroupName(groupName);
    }

    public boolean hasTimetableChanged(String checksum, String group) {
        Optional<TrackerEntity> optionalTracker = this.trackerRepository.findByGroupName(group);
        if (optionalTracker.isEmpty()) {
            return true;
        }

        TrackerEntity trackerEntity = optionalTracker.get();
        return !trackerEntity.getChecksum().equals(checksum);
    }
}
