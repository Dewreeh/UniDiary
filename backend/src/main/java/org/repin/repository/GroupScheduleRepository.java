package org.repin.repository;

import jakarta.persistence.Table;
import org.repin.model.GroupSchedule;
import org.repin.model.GroupScheduleId;
import org.repin.model.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@Table(name = "group_schedule")
public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, GroupScheduleId> {

    @Query("INSERT INTO GroupSchedule gs (gs.group, gs.scheduleItem) VALUES (:groupId, :scheduleItemId)")
    Optional<GroupSchedule> addGroupsToLesson(UUID groupId, UUID scheduleId);
}
