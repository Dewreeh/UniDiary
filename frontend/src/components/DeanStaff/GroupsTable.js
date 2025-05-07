import React, { useState, useEffect, Route, Routes } from 'react';
import { Link } from 'react-router-dom';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';
import ScheduleTable from './SchedulePage';

function GroupsTable({ data }) {
  const columnMapping = {
    "Название": "name",
    "Факультет": "faculty",
    "Специальность": "speciality",
    "Почта группы": "email"
  };

  const [faculties, setFaculties] = useState([]);
  const [specialities, setSpecialities] = useState([]); 


  const [newItem, setNewItem] = useState({
    name: '',
    email: '',
    faculty: null,
    speciality: null 
  });

  useEffect(() => {
    request('/api/get_faculties')
      .then(response => setFaculties(response.data))
      .catch(error => console.error("Ошибка загрузки факультетов:", error));

    const userId = localStorage.getItem('userId');
    if (userId) {
      request(`/api/get_specialities`)
        .then(response => setSpecialities(response.data))
        .catch(error => console.error("Ошибка загрузки специальностей:", error));
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "faculty") {
      const selectedFaculty = faculties.find(faculty => String(faculty.id) === value) || null;
      setNewItem(prevState => ({
        ...prevState,
        faculty: selectedFaculty ? { id: selectedFaculty.id, name: selectedFaculty.name } : null
      }));
    } else if (name === "speciality") {
      const selectedSpeciality = specialities.find(spec => String(spec.id) === value) || null;
      setNewItem(prevState => ({
        ...prevState,
        speciality: selectedSpeciality ? { id: selectedSpeciality.id, name: selectedSpeciality.name } : null
      }));
    } else {
      setNewItem(prevState => ({
        ...prevState,
        [name]: value
      }));
    }
  };

  const handleAdd = async () => {
    try {
      const savedItem = await request('/api/add_group', 'POST', {
        ...newItem,
        faculty: newItem.faculty ? { id: newItem.faculty.id } : null,
        speciality: newItem.speciality ? { id: newItem.speciality.id } : null
      });
      console.log("Группа добавлена:", savedItem);
      alert("Группа успешно добавлена!");
    } catch (error) {
      console.error("Ошибка при добавлении:", error);
      alert(error.message);
    }
  };


  return (
    <div className="table-container">
      <h1 className="table-title">Группы вашего факультета</h1>

      <Table
        data={data} 
        columnMapping={columnMapping}
        getRowLink={(group) => `group/${group.id}`}  
    />

      <div className="add-form">
        <input
          type="text"
          name="name"
          placeholder="Название группы"
          value={newItem.name}
          onChange={handleChange}
        />
        <input
          type="text"
          name="email"
          placeholder="Почта группы"
          value={newItem.groupEmail}
          onChange={handleChange}
        />

        <select name="faculty" value={newItem.faculty?.id || ""} onChange={handleChange}>
          <option value="">Выберите факультет</option>
          {faculties.map(faculty => (
            <option key={faculty.id} value={String(faculty.id)}>{faculty.name}</option>
          ))}
        </select>

        <select name="speciality" value={newItem.speciality?.id || ""} onChange={handleChange}>
          <option value="">Выберите специальность</option>
          {specialities.map(spec => (
            <option key={spec.id} value={String(spec.id)}>{spec.name}</option>
          ))}
        </select>

        <button className="button add-button" onClick={handleAdd}>Добавить</button>
      </div>

    </div>
    
  );
}

export default GroupsTable;
