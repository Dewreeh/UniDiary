import React, { useState, useEffect } from 'react';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';

function Attendance({ title }) {
  const columnMapping = {
    "Дисциплина": "disciplineName",
    "Преподаватель": "lecturerName",
    "Группы": "groups",
    "Тип недели": "weekType",
    "Тип занятия": "lessonType",
    "Начало": "startTime",
  };

  const [schedule, setSchedule] = useState([]);
  const [lecturers, setLecturers] = useState([]);
  const [disciplines, setDisciplines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [studentGroupId, setStudentGroupId] = useState(null);
  
  const [filters, setFilters] = useState({
    groupId: '',
    weekday: '',
    lecturerId: '',
    disciplineId: ''
  });

  const buildParams = (groupId) => {
    let params = groupId ? `?groupId=${groupId}` : '';
    
    Object.entries(filters).forEach(([key, value]) => {
      if (value) {
        params += `&${key}=${value}`;
      }
    });
    
    return params;
  };

  const fetchStudentGroup = async () => {
    try {
      const userId = localStorage.getItem('userId');
      const response = await request(`/api/get_group_id_by_studentId?studentId=${userId}`);
      setStudentGroupId(response);
      return response;
    } catch (err) {
      console.error("Ошибка при получении группы студента:", err);
    }
  };

  const fetchData = async () => {
    try {
      setLoading(true);
      const groupId = await fetchStudentGroup();
      const params = buildParams(groupId);

      const [scheduleRes, lecturersRes, disciplinesRes] = await Promise.all([
        request(`/api/get_concrete_schedules_for_group_by_filters${params}`),
        request('/api/get_lecturers'),
        request('/api/get_disciplines'),
      ]);


      const formattedSchedule = Array.isArray(scheduleRes) 
        ? scheduleRes.map(day => ({
            date: day.date,
            schedulesInfo: day.schedulesInfo ? day.schedulesInfo.map(item => ({
              ...item,
              groups: item.groups?.map(g => g.name).join(', ') || '',
              weekday: formatWeekday(item.weekday),
              weekType: formatWeekType(item.weekType),
              lessonType: formatLessonType(item.lessonType),
              startTime: formatTime(item.startTime),
              endTime: formatTime(item.endTime)
            })) : null
          }))
        : [];

      setSchedule(formattedSchedule);
      setLecturers(lecturersRes.data || []);
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
    fetchData();
  }, [filters]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const resetFilters = () => {
    setFilters({
      groupId: '',
      weekday: '',
      lecturerId: '',
      disciplineId: ''
    });
  };

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error) return <div className="error">Ошибка: {error}</div>;

  return (
    <div className="table-container">
      <h1 className="table-title">{title || "Ближайшие занятия"}</h1>

      <div className="filters-section">
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
              {new Date(day.date).toLocaleDateString('ru-RU', {
                weekday: 'long',
                day: 'numeric',
                month: 'long'
              })}
            </h2>
            
            {day.schedulesInfo ? (
              <Table 
                data={{ 
                  headers: Object.keys(columnMapping), 
                  data: day.schedulesInfo 
                }} 
                columnMapping={columnMapping}
                getRowLink={schedule => `schedule/${schedule.id}`}
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

function formatWeekday(weekday) {
  const days = {
    MONDAY: 'Понедельник',
    TUESDAY: 'Вторник',
    WEDNESDAY: 'Среда',
    THURSDAY: 'Четверг',
    FRIDAY: 'Пятница',
    SATURDAY: 'Суббота'
  };
  return days[weekday] || weekday;
}

function formatWeekType(weekType) {
  const types = {
    HIGH: 'Верхняя',
    LOW: 'Нижняя',
    BOTH: 'Обе'
  };
  return types[weekType] || weekType;
}

function formatLessonType(lessonType) {
  const types = {
    LECTURE: 'Лекция',
    PRACTICE: 'Практика'
  };
  return types[lessonType] || lessonType;
}

function formatTime(timeString) {
  return timeString ? timeString.slice(0, 5) : '';
}

export default Attendance;