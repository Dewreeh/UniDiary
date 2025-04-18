package org.repin.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Data
public class GroupScheduleId implements Serializable {
    private UUID groupId;
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

}