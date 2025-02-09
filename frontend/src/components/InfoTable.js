import React, { useState } from 'react';
import './index.css';

function InfoTable({ title, data, onAdd }) {
  // Если данных нет, устанавливаем пустой массив, чтобы избежать ошибки
  const validData = data && data.length > 0 ? data : [];

  // Определяем заголовки (если данных нет, берём пустой массив)
  const headers = validData.length > 0 ? Object.keys(validData[0]) : [];

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

    onAdd({ ...newItem, id: validData.length + 1 });

    setNewItem(headers.reduce((acc, key) => ({ ...acc, [key]: '' }), {}));
  };

  // Возвращаем сообщение, если нет данных
  if (validData.length === 0) {
    return <div>Нет данных</div>;
  }

  return (
    <div className="table-container">
      <h1 className="table-title">{title}</h1>

      <table className="table table-hover">
        <thead className="custom-thead">
          <tr>
            {headers.map((header) => (
              <th key={header} scope="col">{header}</th>
            ))}
          </tr>
        </thead>
        <tbody className="custom-row">
          {validData.map((item, index) => (
            <tr key={index} className={`custom-row r${index + 1}`}>
              {headers.map((header) => (
                <td key={header}>{item[header]}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>

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
    </div>
  );
}

export default InfoTable;

