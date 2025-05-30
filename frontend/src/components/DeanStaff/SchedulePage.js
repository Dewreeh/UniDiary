import React, { useState, useEffect } from 'react';
import '../index.css';
import { request } from '../../api/api';
import * as formatters from '../General/formatters'
import Table from '../Table';

function ScheduleTable({ title }) {
  const headers = [
    "День недели", 
    "Тип недели", 
    "Дисциплина", 
    "Преподаватель", 
    "Группы", 
    "Тип занятия", 
    "Начало"
  ];

  const columnMapping = {
    "Дисциплина": "disciplineName",
    "Преподаватель": "lecturerName",
    "Группы": "groups",
    "День недели": "weekday",
    "Тип недели": "weekType",
    "Тип занятия": "lessonType",
    "Начало": "startTime",
  };

  const [schedule, setSchedule] = useState({ headers, data: [] });
  const [lecturers, setLecturers] = useState([]);
  const [disciplines, setDisciplines] = useState([]);
  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [dropdownActive, setDropdownActive] = useState(false);
  
  const [filters, setFilters] = useState({
    groupId: '',
    weekday: '',
    lecturerId: '',
    disciplineId: ''
  });

  const [newItem, setNewItem] = useState({
    lecturerId: '',
    disciplineId: '',
    groupIds: [],
    weekday: 'MONDAY',
    weekType: 'BOTH',
    lessonType: 'LECTURE',
    startTime: '',
    endTime: ''
  });

  const buildParams = () => {
    const userId = localStorage.getItem('userId');
    let params = userId ? `?userId=${userId}` : '';
    
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
      const params = buildParams();
      
      const [schedule, lecturersRes, disciplinesRes, groupsRes] = await Promise.all([
        request(`/api/get_schedules_for_faculty${params}`),
        request(`/api/get_lecturers${params}`),
        request('/api/get_disciplines'),
        request(`/api/get_groups_by_dean_staff${params}`)
      ]);
      
      const formattedData = Array.isArray(schedule) 
        ? schedule.map(item => ({
            ...item,
            groups: item.groups?.map(g => g.name).join(', ') || '',
            weekday: formatters.formatWeekday(item.weekday),
            weekType: formatters.formatWeekType(item.weekType),
            lessonType: formatters.formatLessonType(item.lessonType),
            startTime: formatters.formatTime(item.startTime),
            endTime: formatters.formatTime(item.endTime)
          }))
        : [];

      setSchedule({ headers, data: formattedData });
      setLecturers(Array.isArray(lecturersRes?.data) ? lecturersRes.data : []);
      setDisciplines(Array.isArray(disciplinesRes?.data) ? disciplinesRes.data : []);
      setGroups(Array.isArray(groupsRes?.data) ? groupsRes.data : []);
      
    } catch (err) {
      console.error("Ошибка загрузки данных:", err);
      setError(err.message);
      setSchedule({ headers, data: [] });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [filters]); 

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (dropdownActive && !e.target.closest('.groups-dropdown')) {
        setDropdownActive(false);
      }
    };
  
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [dropdownActive]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewItem(prev => ({ ...prev, [name]: value }));
  };

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

  const handleAdd = async () => {
    try {
      const userId = localStorage.getItem('userId');
      const scheduleData = {
        ...newItem,
        startTime: newItem.startTime + ':00',
        userId: userId
      };
     
      const params = userId ? `?userId=${userId}` : '';

      await request(`/api/add_schedule_item${params}`, 'POST', scheduleData);
      
      await fetchData();
      
      setNewItem({
        lecturerId: '',
        disciplineId: '',
        groupIds: [],
        weekday: 'MONDAY',
        weekType: 'BOTH',
        lessonType: 'LECTURE',
        startTime: '',
        endTime: ''
      });
      
    } catch (error) {
      console.error("Ошибка при добавлении:", error);
      alert(error.message);
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error) return <div className="error">Ошибка: {error}</div>;

  return (
    <div className="table-container">
      <h1 className="table-title">{title || "Расписание занятий"}</h1>

      
      <div className="filters-section">
        <select 
          name="groupId" 
          value={filters.groupId}
          onChange={handleFilterChange}
        >
          <option value="">Все группы</option>
          {groups.map(group => (
            <option key={group.id} value={group.id}>{group.name}</option>
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
          name="lecturerId" 
          value={filters.lecturerId}
          onChange={handleFilterChange}
        >
          <option value="">Все преподаватели</option>
          {lecturers.map(lecturer => (
            <option key={lecturer.id} value={lecturer.id}>{lecturer.name}</option>
          ))}
        </select>

        <select 
          name="disciplineId" 
          value={filters.disciplineId}
          onChange={handleFilterChange}
        >
          <option value="">Все дисциплины</option>
          {disciplines.map(discipline => (
            <option key={discipline.id} value={discipline.id}>{discipline.name}</option>
          ))}
        </select>

        <button onClick={resetFilters} className="reset-filters-btn">
          Сбросить фильтры
        </button>
      </div>

      <Table data={schedule} columnMapping={columnMapping} />
      
      <div className="add-form-many">
        <select 
          name="lecturerId" 
          value={newItem.lecturerId} 
          onChange={handleChange}
          required
        >
          <option value="">Выберите преподавателя</option>
          {lecturers.map(lecturer => (
            <option key={lecturer.id} value={lecturer.id}>
              {lecturer.name}
            </option>
          ))}
        </select>
  
        <select 
          name="disciplineId" 
          value={newItem.disciplineId} 
          onChange={handleChange}
          required
        >
          <option value="">Выберите дисциплину</option>
          {disciplines.map(discipline => (
            <option key={discipline.id} value={discipline.id}>
              {discipline.name}
            </option>
          ))}
        </select>
  
        <div className={`groups-dropdown ${dropdownActive ? 'active' : ''}`}>
          <div 
            className="groups-trigger"
            onClick={() => setDropdownActive(!dropdownActive)}
          >
            <span>
              {newItem.groupIds.length > 0 
                ? `Группы (${newItem.groupIds.length})` 
                : "Выберите группы"}
            </span>
          </div>
          
          <div className="groups-list">
            {groups.map(group => (
              <label key={group.id} className="group-item">
                <input
                  type="checkbox"
                  checked={newItem.groupIds.includes(group.id)}
                  onChange={() => {
                    const updatedGroupIds = newItem.groupIds.includes(group.id)
                      ? newItem.groupIds.filter(id => id !== group.id)
                      : [...newItem.groupIds, group.id];
                    setNewItem(prev => ({ ...prev, groupIds: updatedGroupIds }));
                  }}
                />
                {group.name}
              </label>
            ))}
          </div>
        </div>
  
        <select name="weekday" value={newItem.weekday} onChange={handleChange}>
          <option value="MONDAY">Понедельник</option>
          <option value="TUESDAY">Вторник</option>
          <option value="WEDNESDAY">Среда</option>
          <option value="THURSDAY">Четверг</option>
          <option value="FRIDAY">Пятница</option>
          <option value="SATURDAY">Суббота</option>
        </select>
  
        <select name="weekType" value={newItem.weekType} onChange={handleChange}>
          <option value="BOTH">Обе</option>
          <option value="HIGH">Верхняя</option>
          <option value="LOW">Нижняя</option>
        </select>
  
        <select name="lessonType" value={newItem.lessonType} onChange={handleChange}>
          <option value="LECTURE">Лекция</option>
          <option value="PRACTICE">Практика</option>
        </select>
  
        <input
          type="time"
          name="startTime"
          value={newItem.startTime}
          onChange={handleChange}
          required
        />
        <button className="button add-button" onClick={handleAdd}>
          Добавить занятие
        </button>
      </div>
    </div>
  );
}



export default ScheduleTable;