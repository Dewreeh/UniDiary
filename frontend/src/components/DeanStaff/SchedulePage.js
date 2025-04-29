import React, { useState, useEffect } from 'react';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';

function ScheduleTable({ title }) {
  const headers = [
    "Дисциплина", 
    "Преподаватель", 
    "Группы", 
    "День недели", 
    "Тип недели", 
    "Тип занятия", 
    "Начало", 
    "Конец"
  ];

  const columnMapping = {
    "Дисциплина": "disciplineName",
    "Преподаватель": "lecturerName",
    "Группы": "groups",
    "День недели": "weekday",
    "Тип недели": "weekType",
    "Тип занятия": "lessonType",
    "Начало": "startTime",
    "Конец": "endTime"
  };

  const [schedule, setSchedule] = useState({ headers, data: [] });
  const [lecturers, setLecturers] = useState([]);
  const [disciplines, setDisciplines] = useState([]);
  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [dropdownActive, setDropdownActive] = useState(false);
  
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const userId = localStorage.getItem('userId');
        const param = userId ? `?userId=${userId}` : '';
        
        const [scheduleRes, lecturersRes, disciplinesRes, groupsRes] = await Promise.all([
          request(`/api/get_schedules_for_faculty${param}`),
          request(`/api/get_lecturers${param}`),
          request('/api/get_disciplines'),
          request(`/api/get_groups${param}`)
        ]);
        
        
        const formattedData = Array.isArray(scheduleRes?.data) 
          ? scheduleRes.data.map(item => ({
              ...item,
              groups: item.groups?.map(g => g.name).join(', ') || '',
              weekday: formatWeekday(item.weekday),
              weekType: formatWeekType(item.weekType),
              lessonType: formatLessonType(item.lessonType),
              startTime: formatTime(item.startTime),
              endTime: formatTime(item.endTime)
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

    fetchData();
  }, []);

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (dropdownActive && !e.target.closest('.groups-dropdown')) {
        setDropdownActive(false);
      }
    };
  
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [dropdownActive]);

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

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewItem(prev => ({ ...prev, [name]: value }));
  };



  const handleAdd = async () => {
    
    try {
      const userId = localStorage.getItem('userId');
      const scheduleData = {
        ...newItem,
        startTime: newItem.startTime + ':00',
        userId: userId
      };
     
      const param = userId ? `?userId=${userId}` : '';

      await request(`/api/add_schedule_item${param}`, 'POST', scheduleData);
      
      const response = await request(`/api/get_schedules_for_faculty${param}`);
      
      const formattedData = Array.isArray(response?.data) 
        ? response.data.map(item => ({
            ...item,
            groups: item.groups?.map(g => g.name).join(', ') || '',
            weekday: formatWeekday(item.weekday),
            weekType: formatWeekType(item.weekType),
            lessonType: formatLessonType(item.lessonType),
            startTime: formatTime(item.startTime),
            endTime: formatTime(item.endTime)
          }))
        : [];

      setSchedule({ headers, data: formattedData });
      
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
  
      {schedule.data && schedule.data.length > 0 ? (
        <Table data={schedule} columnMapping={columnMapping} />
      ) : (
        <div className="no-data">Нет данных для отображения</div>
      )}
      
      
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
          <option value="BOTH">Каждую неделю</option>
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
    BOTH: 'Каждую неделю',
    EVEN: 'Четная неделя',
    ODD: 'Нечетная неделя'
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

export default ScheduleTable;