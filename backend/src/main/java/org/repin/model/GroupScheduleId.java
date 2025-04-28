package org.repin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@Data
public class GroupScheduleId implements Serializable {
    @Column(name = "group_id", columnDefinition = "uuid")
    private UUID groupId;

    @Column(name = "schedule_id", columnDefinition = "uuid")
    private UUID scheduleItemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupScheduleId that = (GroupScheduleId) o;
        return groupId.equals(that.groupId) &&
                scheduleItemId.equals(that.scheduleItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, scheduleItemId);
    }


    public GroupScheduleId(UUID groupId, UUID scheduleItemId) {
        this.groupId = groupId;
        this.scheduleItemId = scheduleItemId;
    }


}