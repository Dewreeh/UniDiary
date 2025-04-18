package org.repin.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.repin.enums.LessonType;
import org.repin.enums.WeekType;
import org.repin.enums.Weekday;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "schedules")
@Data
@Builder
public class ScheduleItem {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @Enumerated(EnumType.STRING)
    private Weekday weekday;

    @Enumerated(EnumType.STRING)
    private WeekType weekType;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToMany
    @JoinTable(
            name = "schedule_groups",
            joinColumns = @JoinColumn(name = "schedule_item_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<StudentGroup> groups = new HashSet<>();
}