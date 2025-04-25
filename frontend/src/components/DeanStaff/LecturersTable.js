import React, { useState, useEffect } from 'react';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';

function LecturersTable({ data }) {
  const columnMapping = {
    "ФИО": "name",
    "Факультет": "faculty",
    "Почта": "email"
  };

  const [newItem, setNewItem] = useState(
    Object.keys(columnMapping).reduce((acc, key) => ({ ...acc, [columnMapping[key]]: '' }), {})
  );

  const [faculties, setFaculties] = useState([]);

    useEffect(() => {
    request('/api/get_faculties')
        .then(response => setFaculties(response.data))
        .catch(error => console.error("Ошибка загрузки факультетов:", error));
    }, []);


  const [passwordPopup, setPasswordPopup] = useState(null);
  const [generatedPassword, setGeneratedPassword] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewItem(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleAdd = async () => {
    try {
      const savedItem = await request('/api/add_lecturer', 'POST', newItem);
      setPasswordPopup(savedItem.generatedPassword);
      if (savedItem.generatedPassword) {
        setGeneratedPassword(savedItem.generatedPassword);
      }
    } catch (error) {
      console.error("Ошибка при добавлении:", error);
      alert(error.message);
    }
  };

  return (
    <div className="table-container">
      <h1 className="table-title">Преподаватели</h1>

      <Table data={data} columnMapping={columnMapping} />

      <div className="add-form">
      {data.headers.filter(header => header !== "#").map(header => (
        header === "Факультет" ? (
            <select key={header} name="facultyId" value={newItem.facultyId || ""} onChange={handleChange}>
            <option value="">Выберите факультет</option>
            {faculties.map(faculty => (
                <option key={faculty.id} value={faculty.id}>{faculty.name}</option>
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
        ))}
        <button className="button add-button" onClick={handleAdd}>Добавить</button>
      </div>

      {generatedPassword && (
        <div className="password-info">
          <p>Сгенерированный пароль: <strong>{generatedPassword}</strong></p>
        </div>
      )}
      {passwordPopup && (
        <div className="password-popup show">
          Сгенерированный пароль: {passwordPopup}
          <button className="close-btn" onClick={() => setPasswordPopup(null)}>OK</button>
        </div>
      )}
    </div>
  );
}

export default LecturersTable;
