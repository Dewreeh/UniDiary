import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { request } from '../../api/api';
import Table from '../Table';

function GroupStudentsTable() {
  const { groupId } = useParams();
  const columnMapping = {
    "ФИО": "name",
    "Почта": "email",
    "Группа": "studentGroup"
  };

  const [data, setData] = useState({ data: [] });
  const [loading, setLoading] = useState(true);
  const [title, setTitle] = useState(''); 
  const [error, setError] = useState(null);
  const [studentToDelete, setStudentToDelete] = useState('');
  const [students, setStudents] = useState([]);
  const [selectedStudentId, setSelectedStudentId] = useState("");
  const [passwordPopup, setPasswordPopup] = useState(null);
  const [generatedPassword, setGeneratedPassword] = useState(null);
  const [newStudent, setNewStudent] = useState({
    name: '',
    email: ''
  });

  useEffect(() => {
    const fetchStudents = async () => {
      try {
        setLoading(true);
        const response = await request(`/api/get_students_by_group?groupId=${groupId}`);
        setStudents(response.data);
        setData(response);
        
        if (response.data && response.data[0] && response.data[0].studentGroup) {
          setTitle(response.data[0].studentGroup.name || `Группа ${groupId}`);
        } else {
          setTitle(`Группа ${groupId}`);
        }
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchStudents();
  }, [groupId]);

  const handleAddStudent = async () => {
    try {
      const studentData = {
        ...newStudent,
        studentGroup: groupId
      };

      await request('/api/add_student', 'POST', studentData);
      const updatedStudents = await request(`/api/get_students_by_group?groupId=${groupId}`);
      setData(updatedStudents);
      setNewStudent({ name: '', email: '' });

    } catch (error) {
      alert(`Ошибка при добавлении: ${error.message}`);
    }
  };

    const handleMakeHeadman = async () => {
      if (!selectedStudentId) {
        alert("Выберите студента");
        return;
      }
      
      try {
        const response = await request(`/api/add_headman?studentId=${selectedStudentId}`, "POST");
        alert("Староста успешно добавлен!" + ", его сгенерированный пароль: " + response.generatedPassword);
        setSelectedStudentId("");
      } catch (error) {
        alert(error.response?.message || "Ошибка при добавлении");
      }
    };

  const handleDeleteStudent = async () => {
    if (!studentToDelete) {
      alert('Выберите студента для удаления');
      return;
    }

    try {
      await request(`/api/delete_student?studentId=${studentToDelete}`, 'DELETE');
      const updatedStudents = await request(`/api/get_students_by_group?groupId=${groupId}`);
      setData(updatedStudents);
      setStudentToDelete('');
    } catch (error) {
      alert(`Ошибка при удалении: ${error.message}`);
    }
  };

  const handleStudentSelect = (e) => {
    setStudentToDelete(e.target.value);
  };

  if (loading) return <div>Загрузка...</div>;
  if (error) return <div>Ошибка: {error}</div>;

  return (
    <div className="table-container">
      <h1 className="table-title">Студенты группы {title}</h1>

      <Table 
        data={data}
        columnMapping={columnMapping}
      />

      <div className="add-form">
        <input
          type="text"
          name="name"
          placeholder="ФИО"
          value={newStudent.name}
          onChange={(e) => setNewStudent({...newStudent, name: e.target.value})}
        />
        
        <input
          type="email"
          name="email"
          placeholder="Почта"
          value={newStudent.email}
          onChange={(e) => setNewStudent({...newStudent, email: e.target.value})}
        />
        
        <button className="button add-button" onClick={handleAddStudent}>
          Добавить студента
        </button>      
      </div>


      <div className="add-form">
        <select
          value={selectedStudentId}
          onChange={(e) => setSelectedStudentId(e.target.value)}
        >
          <option value="">Выберите студента</option>
          {students.map((student) => (
            <option key={student.id} value={student.id}>
              {student.name} ({student.studentGroup.name})
            </option>
          ))}
        </select>

        <button className="button add-button" onClick={handleMakeHeadman} disabled={!selectedStudentId}>
          Сделать старостой
        </button>
      </div>

      <div className="add-form">
        <select 
          name="studentId" 
          value={studentToDelete} 
          onChange={handleStudentSelect}
        >
          <option value="">Выберите студента</option>
          {data.data.map(student => (
            <option key={student.id} value={student.id}>
              {student.name} ({student.email})
            </option>
          ))}
        </select>

        <button className="button add-button"  onClick={handleDeleteStudent} disabled={!studentToDelete}>
          Удалить студента
        </button>
      </div>

    </div>
  );
}

export default GroupStudentsTable;