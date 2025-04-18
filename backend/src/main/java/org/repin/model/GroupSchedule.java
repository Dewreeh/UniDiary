package org.repin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "group_schedule")
public class GroupSchedule {
    @EmbeddedId
    private GroupScheduleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private StudentGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("scheduleItemId")
    @JoinColumn(name = "schedule_id")
    private ScheduleItem scheduleItem;

    public GroupSchedule(StudentGroup group, ScheduleItem scheduleItem) {
        this.group = group;
        this.scheduleItem = scheduleItem;
    }
}
