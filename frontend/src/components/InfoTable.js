import React, { useState } from 'react';
import './index.css';

function InfoTable({ title, data, onAdd}) {

  // Создаем объект с пустыми значениями для нового элемента
  const [newItem, setNewItem] = useState(
    data.headers.reduce((acc, key) => ({ ...acc, [key]: '' }), {})
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
    onAdd([...data.data, { ...newItem, id: data.data.length + 1 }]);

    // Очищаем форму
    setNewItem(data.headers.reduce((acc, key) => ({ ...acc, [key]: '' }), {}));
  };

  return (
    <div className="table-container">
      <h1 className="table-title">{title}</h1>

      <table className="table table-hover">
        <thead className="custom-thead">
          <tr>
            {data.headers.length > 0 ? (
              data.headers.map((header) => <th key={header}>{header}</th>)
            ) : (
              <th>Нет данных</th>
            )}
          </tr>
        </thead>
        <tbody className="custom-row">
          {data.data && data.data.length > 0 ? (
            data.data.map((item, index) => (
              <tr key={index} className={`custom-row r${index + 1}`}>
                {data.headers.map((header) => (
                  <td key={header}>{item[header]}</td>
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

      {/* Форма для добавления */}
      {data.headers.length > 0 && (
        <div className="add-form">
          {data.headers.map((header) => (
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
