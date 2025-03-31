import React, { useState, useEffect } from 'react';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';

function StudensTable({ title, data, onAdd }) {
  const columnMapping = {
    "ФИО": "name",
    "Почта": "email",
    "Группа": "group",

  };

  const [groups, setGroups] = useState([]);
  const [passwordPopup, setPasswordPopup] = useState(null);
  const [generatedPassword, setGeneratedPassword] = useState(null);
    
    useEffect(() => {
        request(`/api/get_groups${localStorage.getItem('userRole') === 'ROLE_DEAN_STAFF' ? '?userId=' + localStorage.getItem('userId') : ''}`)
          .then(response => {
            console.log("Гурппы с API:", response.data);
            setGroups(response.data);
          })
          .catch(error => console.error("Ошибка загрузки групп:", error));
      }, []);
     


  const [newItem, setNewItem] = useState(
    Object.keys(columnMapping).reduce((acc, key) => ({ ...acc, [columnMapping[key]]: '' }), {})
  );

 
  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewItem(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  
  const handleAdd = async () => {
    const filteredNewItem = Object.keys(newItem)
      .reduce((acc, key) => ({ ...acc, [key]: newItem[key] }), {});


    try {
      const savedItem = await request('/api/add_student', 'POST', filteredNewItem);
      
      setPasswordPopup(savedItem.generatedPassword);

      if (savedItem?.generatedPassword) {
        setGeneratedPassword(savedItem.generatedPassword);
      }

    } catch (error) {
      console.error("Ошибка при добавлении:", error);
      alert(error.message);
    }
  };
  

  return (
    <div className="table-container">
      <h1 className="table-title">Студенты ваших групп</h1>
      <Table data={data} columnMapping={columnMapping}></Table>
      <div className="add-form">
        {data.headers.filter(header => header !== "#").map(header => (
          header === "Группа" ? (
              <select key={header} name="group" value={newItem.group || ""} onChange={handleChange}>
              <option value="">Выберите группу</option>
              {groups.map(group => (
                <option key={group.id} value={String(group.id)}>{group.name}</option>
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
      
        <div> 
          {generatedPassword && (
            <div className="password-info">
              <p>Сгенерированный пароль: <strong>{generatedPassword}</strong></p>
            </div>
          )}
        </div>
        {passwordPopup && (
        <div className="password-popup show">
          Сгенерированный пароль: {passwordPopup}
          <button className="close-btn" onClick={() => setPasswordPopup(null)}>OK</button>
        </div>
      )}
    </div>
  );
}

export default StudensTable;
