package org.repin.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import net.bytebuddy.utility.nullability.MaybeNull;
import org.repin.enums.LessonType;
import org.repin.enums.WeekType;
import org.repin.enums.Weekday;

import java.time.LocalTime;
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

    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester")
    private Semester semester;

    @ManyToMany
    @JoinTable(
            name = "group_schedule",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<StudentGroup> groups = new HashSet<>();
}