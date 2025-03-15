import React, { useState, useEffect } from 'react';
import '../index.css';
import { request } from '../../api/api';

function StaffTable({ title, data, onAdd }) {
  const columnMapping = {
    "ФИО": "name",
    "Почта": "email",
    "Факультет": "faculty"
  };

  const [newItem, setNewItem] = useState(
    Object.keys(columnMapping).reduce((acc, key) => ({ ...acc, [columnMapping[key]]: '' }), {})
  );

  const [faculties, setFaculties] = useState([]);

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
      console.log("Выбранный факультет:", selectedFaculty);
      setNewItem(prevState => ({
        ...prevState,
        faculty: selectedFaculty
      }));
    } else {
      setNewItem(prevState => ({
        ...prevState,
        [name]: value
      }));
    }
  };

  const handleAdd = async () => {
    const filteredNewItem = Object.keys(newItem)
      .reduce((acc, key) => ({ ...acc, [key]: newItem[key] }), {});


    try {
      const savedItem = await request('/api/add_staff_member', 'POST', filteredNewItem);

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
          {data.data.length > 0 ? (
            data.data.map((item, index) => (
              <tr key={index} className={`custom-row r${index + 1}`}>
                {data.headers.map((header) => (
                  <td key={header}>{
                    header === "#" ? index + 1 :
                    header === "Факультет" ? item.faculty?.name : item[columnMapping[header]]
                  }</td>
                ))}
              </tr>
            ))
          ) : (
            <tr><td colSpan={data.headers.length}>Нет данных</td></tr>
          )}
        </tbody>
      </table>
      <div className="add-form">
        {data.headers.filter(header => header !== "#").map(header => (
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
        ))}
        <button className="button add-button" onClick={handleAdd}>Добавить</button>
      </div>
    </div>
  );
}

export default StaffTable;
