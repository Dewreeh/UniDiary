import React, { useState, useEffect } from 'react';
import './index.css';
import { request } from '../api/api';

function InfoTable({ title, data, onAdd, section }) {
  const columnMapping = {
    "#": "id",
    "Название": "name",
    "Почта": "email",
    "Номер телефона": "phone_number",
    "Факультет": "faculty",
    "ФИО": "name"
  };

  const getMappedHeaders = () => 
    data.headers.reduce((acc, key) => ({ ...acc, [columnMapping[key] || key]: '' }), {});

  const [newItem, setNewItem] = useState(getMappedHeaders());

  useEffect(() => {
    setNewItem(getMappedHeaders());
  }, [data.headers]);

 
  const [relatedData, setRelatedData] = useState([]);


  useEffect(() => {
    const relatedEndpoints = {
      'sotrudniki-dekanatov': '/api/get_faculties',
    };

    if (relatedEndpoints[section]) {
      request(relatedEndpoints[section])
        .then(response => {
          console.log("Ответ сервера:", response);
          setRelatedData(Array.isArray(response.data) ? response.data : []);
        })
        .catch(error => console.error("Ошибка загрузки данных:", error));
    }
  }, [section]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    const mappedKey = columnMapping[name] || name;
  
    if (mappedKey === "faculty") {
      const selectedFaculty = relatedData.find(f => String(f.id) === value);
      setNewItem({ ...newItem, [mappedKey]: selectedFaculty || null });
    } else {
      setNewItem({ ...newItem, [mappedKey]: value });
    }
  };

  const handleAdd = async () => {
    const filteredNewItem = Object.keys(newItem)
      .filter(key => key !== '#')
      .reduce((acc, key) => ({ ...acc, [key]: newItem[key] }), {});
  
    const sectionToApiMap = {
      fakultety: '/api/add_faculty',
      'sotrudniki-dekanatov': '/api/add_staff_member',
      statistika: '/api/add_statistics',
    };
  
    try {
      const endpoint = sectionToApiMap[section];
      if (!endpoint) throw new Error("Неизвестный раздел");
  
      const savedItem = await request(endpoint, 'POST', filteredNewItem);
  
      if (savedItem.generatedPassword) {
        alert(`Сгенерированный пароль: ${savedItem.generatedPassword}`);
      }
  
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
                {data.headers.map((header) =>
                  header === "#" ? <td key={header}>{index}</td> : <td key={header}>{item[columnMapping[header]]?.name || item[columnMapping[header]]}</td>
                )}
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
          {data.headers.filter(header => header !== "#").map(header => (
            header === "Факультет" && section === "sotrudniki-dekanatov" ? (
              <select
                key={header}
                name="faculty"
                value={newItem.faculty?.id || ""}
                onChange={handleChange}
              >
                <option value="">Выберите факультет</option>
                {relatedData.map(faculty => (
                  <option key={faculty.id} value={faculty.id}>
                    {faculty.name}
                  </option>
                ))}
              </select>
            ) : (
              <input
                key={header}
                type="text"
                name={header}
                placeholder={header}
                value={newItem[columnMapping[header]] || ""}
                onChange={handleChange}
              />
            )
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
