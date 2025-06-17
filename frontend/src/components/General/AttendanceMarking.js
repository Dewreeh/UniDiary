import React, { useState, useEffect } from 'react';
import { request } from '../../api/api';
import { useParams } from 'react-router-dom';
import * as formatters from './formatters';
import '../index.css';

function AttendanceMarking() {
  const { scheduleId, date } = useParams();
  const userRole = localStorage.getItem('userRole');
  const [groupsData, setGroupsData] = useState([]);
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

        if (userRole === 'ROLE_HEADMAN') {
          const groupIdResponse = await request(`/api/get_group_id_by_studentId?studentId=${localStorage.getItem('userId')}`);
          const studentsResponse = await request(`/api/get_students_by_group?groupId=${groupIdResponse}`);
          
          console.log('aaaa');
          const attendanceResponse = await request(`/api/get_attendance_for_schedule?scheduleId=${scheduleId}&groupId=${groupIdResponse}&date=${date}`);
          
          const initialAttendance = {};
          studentsResponse.data.forEach(student => {
            const attendanceRecord = attendanceResponse.students.find(a => a.studentId === student.id);
            initialAttendance[student.id] = attendanceRecord ? attendanceRecord.attendanceStatus : false;
          });
          
          setAttendance(initialAttendance);
          setGroupsData([{
            groupId: groupIdResponse,
            groupName: studentsResponse.groupName || 'Моя группа',
            students: studentsResponse.data
          }]);
          
        } else {

          const groups = scheduleResponse.groups || [];
          const [groupsStudents, attendanceResponses] = await Promise.all([
            Promise.all(groups.map(group => 
              request(`/api/get_students_by_group?groupId=${group.id}`)
            )),
            Promise.all(groups.map(group => 
              request(`/api/get_attendance_for_schedule?scheduleId=${scheduleId}&groupId=${group.id}&date=${date}`)
            ))
          ]);
          
          const groupsWithStudents = groups.map((group, index) => ({
            groupId: group.id,
            groupName: group.name,
            students: groupsStudents[index].data
          }));
          
          setGroupsData(groupsWithStudents);
          
          const initialAttendance = {};
          groupsWithStudents.forEach((group, groupIndex) => {
            group.students.forEach(student => {
              const attendanceRecord = attendanceResponses[groupIndex].students.find(a => a.studentId === student.id);
              initialAttendance[student.id] = attendanceRecord ? attendanceRecord.attendanceStatus : false;
            });
          });
          
          setAttendance(initialAttendance);
        }

      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [scheduleId, userRole]);

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


      const saveRequests = groupsData.map(group => {
        const groupAttendanceList = group.students.map(student => ({
          studentId: student.id,
          attendanceStatus: attendance[student.id] === null ? false : attendance[student.id]
        }));
        
        return request('/api/mark_attendance', 'POST', {
          groupId: group.groupId,
          scheduleItemId: scheduleId,
          timestamp: timestamp,
          attendanceList: groupAttendanceList
        });
      });

      await Promise.all(saveRequests);
      alert('Посещаемость успешно сохранена!');
      
      
    } catch (error) {
      console.error("Ошибка сохранения:", error);
    } finally {
      setSubmitting(false);
    }
  };

  const padTimeWithSeconds = (timeString) => {
    if (!timeString) return '00:00:00';
    const parts = timeString.split(':');
    if (parts.length === 2) return `${timeString}:00`; 
    return timeString.length === 5 ? `${timeString}:00` : timeString;
  };

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error) return <div className="error">Ошибка: {error}</div>;
  if (!scheduleItem) return <div>Занятие не найдено</div>;
  if (groupsData.length === 0) return <div>Нет данных о группах</div>;

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

      {groupsData.map(groupData => (
        <div key={groupData.groupId} className="group-attendance-section">
          <h3 className="group-title">Группа: {groupData.groupName}</h3>
          <table className="table">
            <thead className='custom-row'>
              <tr>
                <th>Студент</th>
                <th>Статус</th>
              </tr>
            </thead>
            <tbody>
              {groupData.students.map(student => (
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
        </div>
      ))}

      <button 
        onClick={submitAttendance} 
        disabled={submitting || groupsData.length === 0}
        className="button save-button"
      >
        {submitting ? 'Сохранение...' : 'Сохранить посещаемость'}
      </button>
    </div>
  );
}

export default AttendanceMarking;