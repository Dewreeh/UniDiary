import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';

function DisciplinesTable({ data }) {
  const columnMapping = {
    "Название": "name",
    "Факультет": "faculty" 
  };

  const [faculties, setFaculties] = useState([]);
  const [newItem, setNewItem] = useState({
    name: '',
    facultyId: null
  });

  useEffect(() => {
    loadFaculties();
  }, []);

  const loadFaculties = async () => {
    try {
      const response = await request('/api/get_faculties');
      setFaculties(response.data);
    } catch (error) {
      console.error("Ошибка загрузки факультетов:", error);
      alert("Не удалось загрузить факультеты");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewItem(prev => ({
      ...prev,
      [name]: name === 'facultyId' ? (value || null) : value
    }));
  };

  const handleAdd = async () => {
    if (!newItem.name || !newItem.facultyId) {
      alert("Заполните все обязательные поля");
      return;
    }

    try {
      const savedItem = await request('/api/add_discipline', 'POST', {
        name: newItem.name,
        facultyId: newItem.facultyId // Отправляем только ID
      });
      
      console.log("Дисциплина добавлена:", savedItem);
      alert("Дисциплина успешно добавлена!");
      
      // Сброс формы
      setNewItem({
        name: '',
        facultyId: null
      });
      
    } catch (error) {
      console.error("Ошибка при добавлении:", error);
      alert(error.response?.data?.message || "Ошибка при добавлении дисциплины");
    }
  };

  return (
    <div className="table-container">
      <h1 className="table-title">Дисциплины</h1>

      <Table 
        data={data}
        columnMapping={columnMapping}
      />

      <div className="add-form">
        <input
          type="text"
          name="name"
          placeholder="Название дисциплины"
          value={newItem.name}
          onChange={handleChange}
          required
        />

        <select 
          name="facultyId" 
          value={newItem.facultyId || ""}
          onChange={handleChange}
          required
        >
          <option value="">Выберите факультет</option>
          {faculties.map(faculty => (
            <option key={faculty.id} value={faculty.id}>
              {faculty.name}
            </option>
          ))}
        </select>

        <button 
          className="button add-button" 
          onClick={handleAdd}
          disabled={!newItem.name || !newItem.facultyId}
        >
          Добавить
        </button>
      </div>
    </div>
  );
}

export default DisciplinesTable;