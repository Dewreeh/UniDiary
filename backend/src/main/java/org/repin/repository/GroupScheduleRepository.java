package org.repin.repository;

import jakarta.persistence.Table;
import org.repin.model.GroupSchedule;
import org.repin.model.GroupScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@Table(name = "group_schedule")
public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, GroupScheduleId> {
}
