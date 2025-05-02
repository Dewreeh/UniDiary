package org.repin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringExclude;

@Entity
@NoArgsConstructor
@Data
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
    @ToStringExclude
    private ScheduleItem scheduleItem;

    public GroupSchedule(StudentGroup group, ScheduleItem scheduleItem) {
        this.id = new GroupScheduleId(group.getId(), scheduleItem.getId());
        this.group = group;
        this.scheduleItem = scheduleItem;
    }


}