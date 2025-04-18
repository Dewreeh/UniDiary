package org.repin.service;

import org.repin.dto.request_dto.ScheduleAddDto;
import org.repin.model.GroupSchedule;
import org.repin.model.ScheduleItem;
import org.repin.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SchedulesService {
    private final ScheduleItemRepository scheduleItemRepository;
    private final StudentGroupsRepository studentGroupsRepository;
    private final DisciplineRepository disciplineRepository;
    private final LecturerRepository lecturerRepository;
    private final GroupScheduleRepository groupScheduleRepository;

    SchedulesService(ScheduleItemRepository scheduleItemRepository,
                     StudentGroupsRepository studentGroupsRepository,
                     DisciplineRepository disciplineRepository,
                     LecturerRepository lecturerRepository,
                     GroupScheduleRepository groupScheduleRepository){
        this.scheduleItemRepository = scheduleItemRepository;
        this.studentGroupsRepository = studentGroupsRepository;
        this.disciplineRepository = disciplineRepository;
        this.lecturerRepository = lecturerRepository;
        this.groupScheduleRepository = groupScheduleRepository;
    }


    public ScheduleItem createScheduleItem(ScheduleAddDto dto){

        ScheduleItem scheduleItem = buildScheduleItem(dto);
        ScheduleItem savedScheduleItem  = scheduleItemRepository.save(scheduleItem);

        List<GroupSchedule> groupScheduleList = getGroupSchdeduleList(dto.getGroupsIds(), savedScheduleItem);

        groupScheduleRepository.saveAll(groupScheduleList);

        return savedScheduleItem;
    }

    private ScheduleItem buildScheduleItem(ScheduleAddDto dto){
        return  ScheduleItem.builder()
                .discipline(disciplineRepository.getReferenceById(dto.getDiscipline_id()))
                .lecturer(lecturerRepository.getReferenceById(dto.getLecturerId()))
                .lessonType(dto.getLessonType())
                .weekday(dto.getWeekday())
                .startTime(dto.getStartTime())
                .endTime(dto.getStartTime().plusMinutes(90))
                .build();
    }

    private List<GroupSchedule> getGroupSchdeduleList(List<UUID> groupIds, ScheduleItem schedule){
        return groupIds
                .stream()
                .map(groupId -> new GroupSchedule(studentGroupsRepository.getReferenceById(groupId), schedule))
                .toList();
    }
}
