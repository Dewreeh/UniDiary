import React, { useState, useEffect } from 'react';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';
import * as formatters from '../General/formatters'

function LecturerAttendance({ title }) {
  const columnMapping = {
    "Дисциплина": "disciplineName",
    "Группы": "groups",
    "Тип недели": "weekType",
    "Тип занятия": "lessonType",
    "Начало": "startTime",
  };

  const [schedule, setSchedule] = useState([]);
  const [groups, setGroups] = useState([]);
  const [disciplines, setDisciplines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const lecturerId = localStorage.getItem('userId');
  
  const [filters, setFilters] = useState({
    groupId: '',
    weekday: '',
    disciplineId: ''
  });

  const buildParams = () => {
    let params = lecturerId ? `?lecturerId=${lecturerId}` : '';
    
    Object.entries(filters).forEach(([key, value]) => {
      if (value) {
        params += `&${key}=${value}`;
      }
    });
    
    return params;
  };

  const fetchData = async () => {
    try {
      setLoading(true);
      
      if (!lecturerId) {
        throw new Error('Не удалось определить ID преподавателя');
      }

      const params = buildParams();

      const [scheduleRes, groupsRes, disciplinesRes] = await Promise.all([
        request(`/api/get_concrete_schedules_by_filters${params}`),
        request('/api/get_groups'),
        request('/api/get_disciplines'),
      ]);

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
      setGroups(groupsRes.data || []);
      setDisciplines(disciplinesRes.data || []);
      
    } catch (err) {
      console.error("Ошибка загрузки данных:", err);
      setError(err.message);
      setSchedule([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (lecturerId) {
      fetchData();
    }
  }, [filters, lecturerId]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const resetFilters = () => {
    setFilters({
      groupId: '',
      weekday: '',
      disciplineId: ''
    });
  };

  if (!lecturerId) return <div className="error">Требуется авторизация преподавателя</div>;
  if (loading) return <div className="loading">Загрузка...</div>;
  if (error) return <div className="error">Ошибка: {error}</div>;

  return (
    <div className="table-container">
      <h1 className="table-title">{title || "Мои занятия"}</h1>

      <div className="filters-section">
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

        <select 
          name="weekday" 
          value={filters.weekday}
          onChange={handleFilterChange}
        >
          <option value="">Все дни</option>
          <option value="MONDAY">Понедельник</option>
          <option value="TUESDAY">Вторник</option>
          <option value="WEDNESDAY">Среда</option>
          <option value="THURSDAY">Четверг</option>
          <option value="FRIDAY">Пятница</option>
          <option value="SATURDAY">Суббота</option>
        </select>

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
        {schedule.map((day, index) => (
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
        ))}
      </div>
    </div>
  );
}

export default LecturerAttendance;