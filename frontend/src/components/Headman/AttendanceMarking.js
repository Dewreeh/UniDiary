import React, { useState, useEffect } from 'react';
import { request } from '../../api/api';
import { useParams } from 'react-router-dom';
import '../index.css';

function AttendanceMarking() {
  const { scheduleId } = useParams();
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
        
        const groupId = await request(`/api/get_group_id_by_studentId?studentId=${localStorage.getItem('userId')}`);
        setGroup(groupId);

        const studentsResponse = await request(`/api/get_students_by_group?groupId=${groupId}`);
        setStudents(studentsResponse.data);
        
        const initialAttendance = {};
        studentsResponse.data.forEach(student => {
          initialAttendance[student.id] = true; 
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
      
      const today = new Date();
      const formattedDate = today.toISOString().split('T')[0];
      
      const timestamp = `${formattedDate}T${scheduleItem.startTime}`;

      const attendanceData = Object.keys(attendance).map(studentId => ({
        studentId,
        scheduleItemId: scheduleId,
        attendanceStatus: attendance[studentId],
        timestamp: timestamp
      }));
      
      const attendanceList = {
        attendanceList: attendanceData
      };

      await request('/api/mark_attendance', 'POST', attendanceList);
      
      alert('Посещаемость успешно сохранена!');
      
    } catch (error) {
      console.error('Ошибка при сохранении:', error);
      alert('Произошла ошибка при сохранении');
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
          {scheduleItem.disciplineName} - {formatWeekday(scheduleItem.weekday)}, 
          {formatTime(scheduleItem.startTime)} ({formatWeekType(scheduleItem.weekType)})
        </h3>
        <p>Преподаватель: {scheduleItem.lecturerName}</p>
      </div>

      <table className="table">
        <thead className='custom-row'>
          <tr>
            <th>Студент</th>
            <th>Посещение</th>
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
                    {attendance[student.id] ? 'Присутствует' : 'Отсутствует'}
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
      HIGH: 'Верхняя неделя',
      LOW: 'Нижняя неделя',
      BOTH: 'Обе недели'
    };
    return types[weekType] || weekType;
  }
  
  function formatTime(timeString) {
    return timeString ? timeString.slice(0, 5) : '';
  }

export default AttendanceMarking;