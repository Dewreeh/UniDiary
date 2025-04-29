package org.repin.service;

import jakarta.persistence.EntityNotFoundException;
import org.repin.dto.request_dto.ScheduleAddDto;
import org.repin.dto.request_dto.StudentGroupDto;
import org.repin.dto.response_dto.ScheduleResponseDto;
import org.repin.model.*;
import org.repin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class SchedulesService {
    private final ScheduleItemRepository scheduleItemRepository;
    private final StudentGroupsRepository studentGroupsRepository;
    private final DisciplineRepository disciplineRepository;
    private final LecturerRepository lecturerRepository;
    private final GroupScheduleRepository groupScheduleRepository;
    private final SemesterRepository semesterRepository;
    private final DeanStaffRepository deanStaffRepository;

    @Autowired
    SchedulesService(ScheduleItemRepository scheduleItemRepository,
                     StudentGroupsRepository studentGroupsRepository,
                     DisciplineRepository disciplineRepository,
                     LecturerRepository lecturerRepository,
                     GroupScheduleRepository groupScheduleRepository,
                     SemesterRepository semesterRepository,
                     DeanStaffRepository deanStaffRepository){
        this.scheduleItemRepository = scheduleItemRepository;
        this.studentGroupsRepository = studentGroupsRepository;
        this.disciplineRepository = disciplineRepository;
        this.lecturerRepository = lecturerRepository;
        this.groupScheduleRepository = groupScheduleRepository;
        this.semesterRepository = semesterRepository;
        this.deanStaffRepository = deanStaffRepository;
    }


    @Transactional
    public ScheduleItem createScheduleItem(ScheduleAddDto dto){
        //Проверка, нет ли такого занятия
        Optional<ScheduleItem> existingSchedule = scheduleItemRepository.checkExistance(
                dto.getLecturerId(),
                dto.getDisciplineId(),
                dto.getWeekday(),
                dto.getStartTime(),
                semesterRepository.findByIsCurrentTrue().getId()
        );

        //На тот случаей, если сотрудник деканата создаст занятие со своими группами, при этом занятие уже существует (его создал сотрудник другого факультета, добавив туда свои группы)
        //Тогда не создаем новое занятие, а добавляем группы второго сотрудника с существующему
        if(existingSchedule.isPresent()){
            return addGroupsToExistedLesson(existingSchedule.get().getId(), dto.getGroupsIds());
        }

        //создаём новое
        ScheduleItem scheduleItem = buildScheduleItem(dto);
        ScheduleItem savedScheduleItem  = scheduleItemRepository.save(scheduleItem);

        List<GroupSchedule> groupScheduleList = getGroupSchdeduleList(dto.getGroupsIds(), savedScheduleItem);

        groupScheduleRepository.saveAll(groupScheduleList); //TODO пересмотреть, возможно можно сохранять данные в промежуточную таблицу средствами hibernate (через set<group> в сущности ScheduleItem)

        return savedScheduleItem;
    }

    private ScheduleItem buildScheduleItem(ScheduleAddDto dto){
        Discipline discipline = disciplineRepository.findById(dto.getDisciplineId())
                .orElseThrow(() -> new EntityNotFoundException());
        Lecturer lecturer = lecturerRepository.findById(dto.getLecturerId())
                .orElseThrow(() -> new EntityNotFoundException());
        Semester semester = semesterRepository.findByIsCurrentTrue();

        return  ScheduleItem.builder()
                .discipline(discipline)
                .lecturer(lecturer)
                .lessonType(dto.getLessonType())
                .weekday(dto.getWeekday())
                .weekType(dto.getWeekType())
                .startTime(dto.getStartTime())
                .endTime(dto.getStartTime().plusMinutes(90))
                .semester(semester)
                .build();
    }

    private List<GroupSchedule> getGroupSchdeduleList(List<UUID> groupIds, ScheduleItem schedule){
        return groupIds
                .stream()
                .map(groupId -> new GroupSchedule(studentGroupsRepository.findById(groupId).get(), schedule))
                .toList();
    }


    @Transactional
    private ScheduleItem addGroupsToExistedLesson(UUID scheduleItemId, List<UUID> groupIds){
        Set<UUID> existingGroupIds = groupScheduleRepository
                .findByScheduleItemId(scheduleItemId)
                .stream()
                .map(gs -> gs.getGroup().getId())
                .collect(Collectors.toSet());

        List<StudentGroup> groupsToAdd = groupIds.stream()
                .filter(groupId -> !existingGroupIds.contains(groupId))
                .map(groupId -> studentGroupsRepository.findById(groupId)
                        .orElseThrow(() -> new EntityNotFoundException("")))
                .toList();

        ScheduleItem scheduleItem = scheduleItemRepository.findById(scheduleItemId).orElseThrow();

        List<GroupSchedule> newGroupSchedules = groupsToAdd.stream()
                .map(group -> new GroupSchedule(group, scheduleItem))
                .toList();

        groupScheduleRepository.saveAll(newGroupSchedules);

        return scheduleItem;
    }

    public List<ScheduleResponseDto> getSchedulesByFaculty(UUID staffId) {
        UUID facultyId = deanStaffRepository.findFacultyByStaffId(staffId)
                .orElseThrow(() -> new EntityNotFoundException());

        List<ScheduleItem> items = scheduleItemRepository.findByFacultyIdWithGroups(facultyId);



        Map<UUID, ScheduleItem> scheduleItems = items.stream()
                .collect(Collectors.toMap(
                        ScheduleItem::getId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));


        return scheduleItems.values().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private ScheduleResponseDto mapToResponseDto(ScheduleItem item) {
        return ScheduleResponseDto.builder()
                .id(item.getId())
                .groups(item.getGroups().stream()
                        .map(group -> new StudentGroup(group.getId(), group.getName()))
                        .collect(Collectors.toList()))
                .lecturerId(item.getLecturer().getId())
                .lecturerName(item.getLecturer().getName())
                .disciplineId(item.getDiscipline().getId())
                .disciplineName(item.getDiscipline().getName())
                .weekday(item.getWeekday())
                .weekType(item.getWeekType())
                .lessonType(item.getLessonType())
                .startTime(item.getStartTime())
                .endTime(item.getEndTime())
                .build();
    }
}
