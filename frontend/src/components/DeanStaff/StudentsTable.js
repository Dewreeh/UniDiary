import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';

function StudensTable({ title, onAdd }) {
  const columnMapping = {
    "ФИО": "name",
    "Почта": "email",
    "Группа": "studentGroup",
  };

  const [groups, setGroups] = useState([]);
  const [passwordPopup, setPasswordPopup] = useState(null);
  const [generatedPassword, setGeneratedPassword] = useState(null);
  const [data, setData] = useState([]);
  const { section, groupId } = useParams();
  const [loading, setLoading] = useState(true);

  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
        const groupsResponse = await request(
          `/api/get_groups${
            localStorage.getItem('userRole') === 'ROLE_DEAN_STAFF' 
              ? '?userId=' + localStorage.getItem('userId') 
              : ''
          }`
        );
        setGroups(groupsResponse.data);

        let studentsUrl;
        if (groupId) {
          studentsUrl = `/api/get_students_by_group?groupId=${groupId}`;
        } else if (section === 'studenty') {
          studentsUrl = `/api/get_students${
            localStorage.getItem('userRole') === 'ROLE_DEAN_STAFF' ? '?userId=' + localStorage.getItem('userId') : ''
          }`;
        } else {
          throw new Error('Неизвестный раздел');
        }

        const studentsResponse = await request(studentsUrl);
        setData(studentsResponse.data);

      } catch (error) {
        console.error("Ошибка загрузки данных:", error);
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [section, groupId]); 

  const [newItem, setNewItem] = useState(
    Object.keys(columnMapping).reduce((acc, key) => ({ 
      ...acc, 
      [columnMapping[key]]: '' 
    }), {})
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


      const studentsUrl = section === 'group' && groupId
        ? `/api/get_students_by_group?groupId=${groupId}`
        : `/api/get_students${
            localStorage.getItem('userRole') === 'ROLE_DEAN_STAFF'
              ? '?userId=' + localStorage.getItem('userId')
              : ''
          }`;
      
      const updatedData = await request(studentsUrl);
      setData(updatedData.data);

    } catch (error) {
      console.error("Ошибка при добавлении:", error);
      alert(error.message);
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error) return <div className="error">Ошибка: {error}</div>;

  return (
    <div className="table-container">
      <h1 className="table-title">
        {section === 'group' ? 'Студенты группы' : 'Список студентов'}
      </h1>

      <Table 
        data={data}
        columnMapping={columnMapping} 
      />
      
      <div className="add-form">
        {Object.keys(columnMapping).filter(header => header !== "#").map(header => (
          header === "Группа" ? (
            <select 
              key={header} 
              name="studentGroup" 
              value={newItem.studentGroup || ""} 
              onChange={handleChange}
            >
              <option value="">Выберите группу</option>
              {groups.map(group => (
                <option key={group.id} value={String(group.id)}>
                  {group.name}
                </option>
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

export default StudensTable;