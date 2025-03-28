import React, { useState } from 'react';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';

function FacultyTable({ title, data, onAdd }) {
  const columnMapping = {
    "Название": "name",
    "Почта": "email",
    "Номер телефона": "phoneNumber",
  };

  const [newItem, setNewItem] = useState(
    Object.keys(columnMapping).reduce((acc, key) => ({ ...acc, [columnMapping[key]]: '' }), {})
  );

 
  const handleChange = (e) => {
    setNewItem({ ...newItem, [e.target.name]: e.target.value });
  };

  
  const handleAdd = async () => {
    const filteredNewItem = Object.keys(newItem)
      .filter(key => key !== '#')
      .reduce((acc, key) => ({ ...acc, [key]: newItem[key] }), {});
  
  
    try {
      const savedItem = await request('/api/add_faculty', 'POST', filteredNewItem);
      setNewItem(data.headers.reduce((acc, key) => ({ ...acc, [columnMapping[key]]: '' }), {}));
    } catch (error) {
      console.error("Ошибка при добавлении:", error);
      alert(error.message);
    }
  };
  

  return (
    <div className="table-container">
      <h1 className="table-title">Факультеты</h1>
      <Table data={data} columnMapping={columnMapping}></Table>
      <div className="add-form">
        {data.headers.filter(header => header !== "#").map(header => (
          <input
            key={header}
            type="text"
            name={columnMapping[header]}
            placeholder={header}
            value={newItem[columnMapping[header]] || ""}
            onChange={handleChange}
          />
        ))}
        <button className="button add-button" onClick={handleAdd}>Добавить</button>
      </div>
    </div>
  );
}

export default FacultyTable;
