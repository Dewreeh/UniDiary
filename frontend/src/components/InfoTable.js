import React, { useState } from 'react';
import './index.css';

function InfoTable({ title, data = [], onAdd, headers = [] }) {
  // Создаем объект с пустыми значениями для нового элемента
  const [newItem, setNewItem] = useState(
    headers.reduce((acc, key) => ({ ...acc, [key]: '' }), {})
  );

  const handleChange = (e) => {
    setNewItem({ ...newItem, [e.target.name]: e.target.value });
  };

  const handleAdd = () => {
    if (Object.values(newItem).some(value => value.trim() === '')) {
      alert("Заполните все поля!");
      return;
    }

    // Если data пустой, превращаем его в массив
    onAdd([...data, { ...newItem, id: data.length + 1 }]);

    // Очищаем форму
    setNewItem(headers.reduce((acc, key) => ({ ...acc, [key]: '' }), {}));
  };

  return (
    <div className="table-container">
      <h1 className="table-title">{title}</h1>

      <table className="table table-hover">
        <thead className="custom-thead">
          <tr>
            {
              headers.map((header) => <th key={header}>{header}</th>)
            }
            
          </tr>
        </thead>
        <tbody className="custom-row">
          {data && data.length > 0 ? (
            data.map((item, index) => (
              <tr key={index} className={`custom-row r${index + 1}`}>
                {headers.map((header) => (
                  <td key={header}>{item[header]}</td>
                ))}
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={headers.length}>Нет данных</td>
            </tr>
          )}
        </tbody>
      </table>

      {/* Форма для добавления */}
      {headers.length > 0 && (
        <div className="add-form">
          {headers.map((header) => (
            <input
              key={header}
              type="text"
              name={header}
              placeholder={header}
              value={newItem[header]}
              onChange={handleChange}
            />
          ))}
          <button className="button add-button" onClick={handleAdd}>
            Добавить
          </button>
        </div>
      )}
    </div>
  );
}

export default InfoTable;
