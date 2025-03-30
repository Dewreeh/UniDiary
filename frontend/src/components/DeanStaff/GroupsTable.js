import React, { useState, useEffect } from 'react';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';

function GroupsTable({ title, data, onAdd }) {
  const columnMapping = {
    "Название": "name",
    "Почта группы": "groupEmail",
    "Факультет": "faculty",
    "Специальность": "speciality"
    
  };

  const [newItem, setNewItem] = useState(
    Object.keys(columnMapping).reduce((acc, key) => ({ ...acc, [columnMapping[key]]: '' }), {})
  );

  const [faculties, setFaculties] = useState([]);
  const [passwordPopup, setPasswordPopup] = useState(null);

  useEffect(() => {
    request('/api/get_faculties')
      .then(response => {
        console.log("Факультеты с API:", response.data);
        setFaculties(response.data);
      })
      .catch(error => console.error("Ошибка загрузки факультетов:", error));
  }, []);


  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "faculty") {
      const selectedFaculty = faculties.find(faculty => String(faculty.id) === value) || null;
      setNewItem(prevState => ({
        ...prevState,
        faculty: selectedFaculty ? { id: selectedFaculty.id, name: selectedFaculty.name } : null
      }));
    } else {
      setNewItem(prevState => ({
        ...prevState,
        [name]: value
      }));
    }
  };
  

  const handleAdd = async () => {
    const userFacultyId = localStorage.getItem('facultyId');
    const filteredNewItem = {
      ...newItem,
      faculty: userFacultyId ? { id: userFacultyId } : newItem.faculty 
    };
  
    try {
      const savedItem = await request('/api/add_group', 'POST', filteredNewItem);
      console.log("Группа добавлена:", savedItem);
    } catch (error) {
      console.error("Ошибка при добавлении:", error);
      alert(error.message);
    }
  };
  

  return (
    <div className="table-container">
      <h1 className="table-title">Ваши группы</h1>
      <Table data={data} columnMapping={columnMapping}></Table>
      <div className="add-form">
      {data.headers.filter(header => header !== "#").map(header => (
      header === "Факультет" && localStorage.getItem('userRole') === "DEAN_STAFF" ? null : (
        header === "Факультет" ? (
          <select key={header} name="faculty" value={newItem.faculty?.id || ""} onChange={handleChange}>
            <option value="">Выберите факультет</option>
            {faculties.map(faculty => (
              <option key={faculty.id} value={String(faculty.id)}>{faculty.name}</option>
            ))}
          </select>
        ) : (
          <input
            key={header}
            type="text"
            name={columnMapping[header]}
            placeholder={header}
            value={newItem[columnMapping[header]] || ""}
            onChange={handleChange}
          />
        )
      )
    ))}

        <button className="button add-button" onClick={handleAdd}>Добавить</button>
      </div>
      
        <div> 
        </div>
    </div>
  );
}

export default GroupsTable;
