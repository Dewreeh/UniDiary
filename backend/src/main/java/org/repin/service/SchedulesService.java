package org.repin.service;

import org.repin.dto.request_dto.ScheduleAddDto;
import org.repin.model.GroupSchedule;
import org.repin.model.ScheduleItem;
import org.repin.model.Semester;
import org.repin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class SchedulesService {
    private final ScheduleItemRepository scheduleItemRepository;
    private final StudentGroupsRepository studentGroupsRepository;
    private final DisciplineRepository disciplineRepository;
    private final LecturerRepository lecturerRepository;
    private final GroupScheduleRepository groupScheduleRepository;
    private final SemesterRepository semesterRepository;

    @Autowired
    SchedulesService(ScheduleItemRepository scheduleItemRepository,
                     StudentGroupsRepository studentGroupsRepository,
                     DisciplineRepository disciplineRepository,
                     LecturerRepository lecturerRepository,
                     GroupScheduleRepository groupScheduleRepository,
                     SemesterRepository semesterRepository){
        this.scheduleItemRepository = scheduleItemRepository;
        this.studentGroupsRepository = studentGroupsRepository;
        this.disciplineRepository = disciplineRepository;
        this.lecturerRepository = lecturerRepository;
        this.groupScheduleRepository = groupScheduleRepository;
        this.semesterRepository = semesterRepository;
    }


    @Transactional
    public ScheduleItem createScheduleItem(ScheduleAddDto dto){

        Optional<ScheduleItem> existedScheduleItem = checkExistance(dto);

        //На тот случаей, если сотрудник деканата создаст занятие со своими группами, при этом занятие уже существует (его создал сотрудник другого факультета, добавив туда свои группы)
        //Тогда не создаем новое занятие, а добавляем группы второго сотрудника с существующему
        if(existedScheduleItem.isPresent()){
            addGroupsToExistedLesson(existedScheduleItem.get().getId(), dto.getGroupsIds());
            return existedScheduleItem.get();
        }

        ScheduleItem scheduleItem = buildScheduleItem(dto);
        ScheduleItem savedScheduleItem  = scheduleItemRepository.save(scheduleItem);

        List<GroupSchedule> groupScheduleList = getGroupSchdeduleList(dto.getGroupsIds(), savedScheduleItem);

        groupScheduleRepository.saveAll(groupScheduleList);

        return savedScheduleItem;
    }

    private ScheduleItem buildScheduleItem(ScheduleAddDto dto){
        return  ScheduleItem.builder()
                .discipline(disciplineRepository.getReferenceById(dto.getDisciplineId()))
                .lecturer(lecturerRepository.getReferenceById(dto.getLecturerId()))
                .lessonType(dto.getLessonType())
                .weekday(dto.getWeekday())
                .startTime(dto.getStartTime())
                .endTime(dto.getStartTime().plusMinutes(90))
                .semester(semesterRepository.findByIsCurrentTrue())
                .build();
    }

    private List<GroupSchedule> getGroupSchdeduleList(List<UUID> groupIds, ScheduleItem schedule){
        return groupIds
                .stream()
                .map(groupId -> new GroupSchedule(studentGroupsRepository.getReferenceById(groupId), schedule))
                .toList();
    }


    public void addGroupsToExistedLesson(UUID scheduleItemId, List<UUID> groupsId){
        groupsId.stream()
                .map(groupId -> groupScheduleRepository.addGroupsToLesson(scheduleItemId, groupId))
                .toList();
    }

    private Optional<ScheduleItem> checkExistance(ScheduleAddDto dto){
        Optional<ScheduleItem> scheduleItem = scheduleItemRepository.checkExistance(dto.getLecturerId(), dto.getDisciplineId(), dto.getWeekday(), dto.getStartTime());

        return scheduleItem;
    }
}
