import React, { useState } from 'react';
import '../index.css';
import { request } from '../../api/api';

function FacultyTable({ title, data, onAdd }) {
  // Маппинг заголовков (русские -> английские)
  const columnMapping = {
    "Название": "name",
    "Почта": "email",
    "Номер телефона": "phone_number",
  };

  // Начальное состояние newItem
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
      <h1 className="table-title">{title}</h1>
      <table className="table table-hover">
        <thead className="custom-thead">
          <tr>
            {data.headers.map((header) => <th key={header}>{header}</th>)}
          </tr>
        </thead>
        <tbody className="custom-row">
          {data.data && data.data.length > 0 ? (
            data.data.map((item, index) => (
              <tr key={index} className={`custom-row r${index + 1}`}>
                {data.headers.map((header) => (
                  <td key={header}>
                    {header === "#" ? index + 1 : item[columnMapping[header]]}
                  </td>
                ))}
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={data.headers.length}>Нет данных</td>
            </tr>
          )}
        </tbody>
      </table>
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
