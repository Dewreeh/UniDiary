import React, { useState, useEffect, useCallback } from 'react';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';
import * as formatters from './formatters';

function ScheduleView({ 
  title = "Расписание",
  userRole = 'ROLE_LECTURER',
  showGroupsFilter = true,
  showLecturerFilter = true,
  defaultFilters = {}
}) {
  const columnMapping = {
    "Дисциплина": "disciplineName",
    ...(userRole === 'ROLE_LECTURER' ? {} : { "Преподаватель": "lecturerName" }),
    "Группы": "groups",
    "Тип недели": "weekType",
    "Тип занятия": "lessonType",
    "Начало": "startTime",
  };

  const [schedule, setSchedule] = useState([]);
  const [groups, setGroups] = useState([]);
  const [lecturers, setLecturers] = useState([]);
  const [disciplines, setDisciplines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [groupId, setGroupId] = useState(null);
  
  const [filters, setFilters] = useState({
    groupId: '',
    weekday: '',
    lecturerId: '',
    disciplineId: '',
    ...defaultFilters
  });

  const fetchHeadmanGroup = useCallback(async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) return null;
      
      const response = await request(`/api/get_group_id_by_studentId?studentId=${userId}`);
      if (response) {
        setGroupId(response);
        setFilters(prev => prev.groupId ? prev : { ...prev, groupId: response });
      }
      return response;
    } catch (err) {
      console.error("Ошибка при получении группы старосты:", err);
      setError("Не удалось определить вашу учебную группу");
      return null;
    }
  }, []);


  const buildParams = useCallback(() => {
    let baseParams = '';
    
    if (userRole === 'ROLE_LECTURER') {
      baseParams = `?lecturerId=${localStorage.getItem('userId')}`;
    } 
    else if (userRole === 'ROLE_HEADMAN' && groupId) {
      baseParams = `?groupId=${groupId}`;
    }
    
    Object.entries(filters).forEach(([key, value]) => {
      if (value && !(key === 'groupId' && baseParams.includes('groupId'))) {
        baseParams += `&${key}=${value}`;
      }
    });
    
    return baseParams;
  }, [filters, groupId, userRole]);


  const fetchScheduleData = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      
      if (userRole === 'ROLE_HEADMAN') {
        const group = await fetchHeadmanGroup();
        if (!group) return;
      }

      const params = buildParams();
      const requests = [
        request(`/api/get_concrete_schedules_by_filters${params}`),
        request('/api/get_disciplines'),
      ];

      if (userRole === 'ROLE_HEADMAN') {
        requests.push(request('/api/get_lecturers'));
      }
      
      if (showGroupsFilter && userRole === 'ROLE_LECTURER') {
        requests.push(request('/api/get_groups'));
      }

      const [scheduleRes, disciplinesRes, ...rest] = await Promise.all(requests);

      if (userRole === 'ROLE_HEADMAN') {
        setLecturers(rest[0].data || []);
      }
      
      if (showGroupsFilter && userRole === 'ROLE_LECTURER') {
        setGroups(rest[0]?.data || []);
      }

      const formattedSchedule = Array.isArray(scheduleRes) 
        ? scheduleRes.map(day => ({
            date: day.date,
            schedulesInfo: day.schedulesInfo?.map(item => ({
              ...item,
              groups: item.groups?.map(g => g.name).join(', ') || '',
              weekday: formatters.formatWeekday(item.weekday),
              weekType: formatters.formatWeekType(item.weekType),
              lessonType: formatters.formatLessonType(item.lessonType),
              startTime: formatters.formatTime(item.startTime),
              endTime: formatters.formatTime(item.endTime)
            })) || null
          }))
        : [];

      setSchedule(formattedSchedule);
      setDisciplines(disciplinesRes.data || []);
      
    } catch (err) {
      console.error("Ошибка загрузки расписания:", err);
      setError(err.message || "Произошла ошибка при загрузке данных");
      setSchedule([]);
    } finally {
      setLoading(false);
    }
  }, [buildParams, fetchHeadmanGroup, showGroupsFilter, userRole]);

  useEffect(() => {

    
    fetchScheduleData();

  }, [fetchScheduleData]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const resetFilters = () => {
    setFilters({
      groupId: userRole === 'ROLE_HEADMAN' ? groupId : '',
      weekday: '',
      lecturerId: '',
      disciplineId: '',
      ...defaultFilters
    });
  };

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error) return <div className="error">Ошибка: {error}</div>;

  return (
    <div className="table-container">
      <h1 className="table-title">{title}</h1>

      <div className="filters-section">
        {showGroupsFilter && userRole === 'ROLE_LECTURER' && (
          <select 
            name="groupId" 
            value={filters.groupId}
            onChange={handleFilterChange}
          >
            <option value="">Все группы</option>
            {groups.map(group => (
              <option key={group.id} value={group.id}>
                {group.name}
              </option>
            ))}
          </select>
        )}

        <select 
          name="weekday" 
          value={filters.weekday}
          onChange={handleFilterChange}
        >
          <option value="">Все дни</option>
          {Object.entries(formatters.weekdays).map(([key, value]) => (
            <option key={key} value={key}>{value}</option>
          ))}
        </select>

        {showLecturerFilter && userRole === 'ROLE_HEADMAN' && (
          <select 
            name="lecturerId" 
            value={filters.lecturerId}
            onChange={handleFilterChange}
          >
            <option value="">Все преподаватели</option>
            {lecturers.map(lecturer => (
              <option key={lecturer.id} value={lecturer.id}>
                {lecturer.name}
              </option>
            ))}
          </select>
        )}

        <select 
          name="disciplineId" 
          value={filters.disciplineId}
          onChange={handleFilterChange}
        >
          <option value="">Все дисциплины</option>
          {disciplines.map(discipline => (
            <option key={discipline.id} value={discipline.id}>
              {discipline.name}
            </option>
          ))}
        </select>

        <button onClick={resetFilters} className="reset-filters-btn">
          Сбросить фильтры
        </button>
      </div>

      <div className="schedule-days">
        {schedule.length > 0 ? (
          schedule.map((day, index) => (
            <div key={index} className="day-schedule">
              <h2 className="day-title">  
                {formatters.formatDate(day.date)}
              </h2>
              {day.schedulesInfo ? (
                <Table 
                  data={{ 
                    headers: Object.keys(columnMapping), 
                    data: day.schedulesInfo 
                  }} 
                  columnMapping={columnMapping}
                  getRowLink={schedule => `schedule/${schedule.id}/${day.date}`}
                />
              ) : (
                <div className="no-classes">Занятий нет</div>
              )}
            </div>
          ))
        ) : (
          <div className="no-data">Нет данных для отображения</div>
        )}
      </div>
    </div>
  );
}

export default ScheduleView;