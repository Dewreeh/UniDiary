import React, { useState, useEffect } from 'react';
import { request } from '../../api/api';
import { useParams } from 'react-router-dom';
import * as formatters from '../General/formatters'
import '../index.css';

function AttendanceMarking() {
  const { scheduleId, date } = useParams();
  const [students, setStudents] = useState([]);
  const [groupId, setGroup] = useState(null);
  const [scheduleItem, setScheduleItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [attendance, setAttendance] = useState({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
     
        const scheduleResponse = await request(`/api/get_schedule_item?scheduleId=${scheduleId}`);
        setScheduleItem(scheduleResponse);
        
        const groupIdResponse = await request(`/api/get_group_id_by_studentId?studentId=${localStorage.getItem('userId')}`);
        setGroup(groupIdResponse);
        
        const studentsResponse = await request(`/api/get_students_by_group?groupId=${groupIdResponse}`);
        setStudents(studentsResponse.data);
        
        const attendanceResponse = await request(`/api/get_attendance_for_schedule?scheduleId=${scheduleId}&groupId=${groupIdResponse}`);
        
        const initialAttendance = {};
        studentsResponse.data.forEach(student => {
          //Если есть данные о посещаемости - используем их, иначе по умолчанию true
          const attendanceRecord = attendanceResponse.students.find(a => a.studentId === student.id);
          initialAttendance[student.id] = attendanceRecord ? attendanceRecord.attendanceStatus : false;
        });
        
        setAttendance(initialAttendance);
        
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [scheduleId]);

  const handleAttendanceChange = (studentId) => {
    setAttendance(prev => ({
      ...prev,
      [studentId]: !prev[studentId]
    }));
  };

  const submitAttendance = async () => {
    try {
      setSubmitting(true);
      
      const formattedDate = date;
      const formattedTime = padTimeWithSeconds(scheduleItem.startTime);
      const timestamp = `${formattedDate}T${formattedTime}`;
      
      console.log(timestamp);

      const attendanceList = Object.keys(attendance).map(studentId => ({
        studentId,
        attendanceStatus: attendance[studentId] === null ? false : attendance[studentId]
      }));
      
      const requestData = {
        groupId: groupId,
        scheduleItemId: scheduleId,
        timestamp: timestamp,
        attendanceList: attendanceList
      };

      await request('/api/mark_attendance', 'POST', requestData);
      
      alert('Посещаемость успешно сохранена!');
      
    } catch (error) {

    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error) return <div className="error">Ошибка: {error}</div>;
  if (!scheduleItem) return <div>Занятие не найдено</div>;

  return (
    <div className="table-container">
      <h2 className="table-title">Заполнение посещаемости</h2>
      <div className="schedule-info">
        <h3>
          {scheduleItem.disciplineName} - {" " + formatters.formatWeekday(scheduleItem.weekday)}, 
          {" " + formatters.formatTime(scheduleItem.startTime) + ", " + date} ({formatters.formatWeekType(scheduleItem.weekType)})
        </h3>
        <p>Преподаватель: {scheduleItem.lecturerName}</p>
      </div>

      <table className="table">
        <thead className='custom-row'>
          <tr>
            <th>Студент</th>
            <th>Статус</th>
          </tr>
        </thead>
        <tbody>
          {students.map(student => (
            <tr className='custom-row' key={student.id}>
              <td>{student.name}</td>
              <td>
                <label className="attendance-switch">
                  <input
                    type="checkbox"
                    checked={attendance[student.id] || false}
                    onChange={() => handleAttendanceChange(student.id)}
                  />
                  <span className="slider round"></span>
                  <span className="status-text">
                    {attendance[student.id] ? 'Присутствие' : 'Отсутствие'}
                  </span>
                </label>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <button 
        onClick={submitAttendance} 
        disabled={submitting}
        className="button"
      >
        {submitting ? 'Сохранение...' : 'Сохранить посещаемость'}
      </button>
    </div>
  );
}


  function padTimeWithSeconds(timeString) {
    if (!timeString) return '00:00:00';
    
    const parts = timeString.split(':');
    if (parts.length === 2) {
      return `${timeString}:00`; 
    }
    return timeString.length === 5 ? `${timeString}:00` : timeString;
  }

export default AttendanceMarking;