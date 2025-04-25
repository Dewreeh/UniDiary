import React, { useState, useEffect } from 'react';
import Table from '../Table';
import { request } from '../../api/api';

function HeadmanTable({ data }) {
  const columnMapping = {
    "ФИО": "name",
    "Группа": "studentGroup",
    "Почта": "email"
  };

  const [students, setStudents] = useState([]);
  const [selectedStudentId, setSelectedStudentId] = useState("");

  useEffect(() => {
    loadStudents();
  }, []);

  const loadStudents = async () => {
    try {
      const response = await request(`/api/get_students${localStorage.getItem('userRole') === 'ROLE_DEAN_STAFF' ? '?userId=' + localStorage.getItem('userId') : ''}`);
      setStudents(response.data);
    } catch (error) {
      console.error("Ошибка загрузки студентов:", error);
      alert("Не удалось загрузить студентов");
    }
  };

  const handleAdd = async () => {
    if (!selectedStudentId) {
      alert("Выберите студента");
      return;
    }
    
    try {
      const response = await request(`/api/add_headman?studentId=${selectedStudentId}`, "POST");
      alert("Староста успешно добавлен!");
      setSelectedStudentId("");
    } catch (error) {
      console.error("Ошибка при добавлении старосты:", error);
      alert(error.response?.data?.message || "Ошибка при добавлении");
    }
  };

  return (
    <div className="table-container">
      <h1 className="table-title">Старосты</h1>

      <Table data={data} columnMapping={columnMapping} />

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

        <button
          className="button add-button"
          onClick={handleAdd}
          disabled={!selectedStudentId}
        >
          Добавить
        </button>
      </div>
    </div>
  );
}

export default HeadmanTable;
