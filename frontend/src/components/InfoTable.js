import React, { useState } from 'react';
import './index.css';
import { request } from '../api/api';

function InfoTable({ title, data, onAdd, section}) {

  // Создаем объект с пустыми значениями для нового элемента
  const [newItem, setNewItem] = useState(
    data.headers.reduce((acc, key) => ({ ...acc, [key]: '' }), {})
  );

  const handleChange = (e) => {
    setNewItem({ ...newItem, [e.target.name]: e.target.value });
  };


  const handleAdd = async () => {

  const filteredNewItem = Object.keys(newItem)
    .filter(key => key !== '#') // Исключаем поле '#'
    .reduce((acc, key) => ({ ...acc, [key]: newItem[key] }), {});

  console.log('данные:', filteredNewItem);  

  // Проверка на пустые значения
  if (Object.values(filteredNewItem).some(value => !value || (typeof value === 'string' && value.trim() === ''))) {
    alert("Заполните все поля!");
    return;
  }

  // Определяем эндпоинт в зависимости от section
  const sectionToApiMap = {
    fakultety: '/api/add_faculty',
    'sotrudniki-dekanatov': '/api/add_staff',
    statistika: '/api/add_statistics',
  };

  try {
    const endpoint = sectionToApiMap[section];

    if (!endpoint) {
      throw new Error("Неизвестный раздел");
    }

    const savedItem = await request(endpoint, 'POST', filteredNewItem);

    onAdd([...(data.data ?? []), savedItem]);

    // Очищаем форму
    setNewItem(data.headers.reduce((acc, key) => ({ ...acc, [key]: '' }), {}));
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
          {data.headers
            .filter(header => header !== "#") //id не нужно добавлять вручную
            .map(header => (
              <input
                key={header}
                type="text"
                name={header}
                placeholder={header}
                value={newItem[header] || ""}
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
